package com.cloudmade.chameleon;

import com.cloudmade.chameleon.rclass.RClass;
import com.cloudmade.chameleon.rclass.RClassFinder;
import com.cloudmade.chameleon.rclass.RInnerClass;

import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;

@SupportedAnnotationTypes({"com.cloudmade.chameleon.ChameleonThemes"})
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class ThemeElementProviderAnnotationProcessor extends AbstractProcessor {

    private RClassFinder rClassFinder;
    private ThemeElementProviderGenerator themeElementProviderGenerator;
    private ThemeResourceExtractor themeResourceExtractor;
    private ChameleonThemesExtractor chameleonThemesExtractor;
    private ThemesGenerator themesGenerator;
    private JavaDocInfoExtractor javaDocInfoExtractor;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        VelocityEngine velocityEngine = new VelocityEngine();
        velocityEngine.setProperty(RuntimeConstants.RESOURCE_LOADER, "class");
        velocityEngine.setProperty("class.resource.loader.class", ClasspathResourceLoader.class.getName());
        velocityEngine.init();

        ClassGenerator classGenerator = new ClassGenerator(processingEnvironment, velocityEngine);
        rClassFinder = new RClassFinder(processingEnvironment);
        themeElementProviderGenerator = new ThemeElementProviderGenerator(classGenerator);
        themeResourceExtractor = new ThemeResourceExtractor();
        chameleonThemesExtractor = new ChameleonThemesExtractor();
        themesGenerator = new ThemesGenerator(classGenerator);
        javaDocInfoExtractor = new JavaDocInfoExtractor();
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        if (!nothingToDo(set, roundEnvironment)) {
            for (Element element : roundEnvironment.getElementsAnnotatedWith(ChameleonThemes.class)) {
                String[] themeSuffixes = getThemeSuffixes(element);
                Map<ChameleonThemeEntity, List<String>> chameleonThemesMap = chameleonThemesExtractor.extractChameleonThemes(themeSuffixes, getThemeSuffixesAmount(element));
                generateThemeElementProviders(chameleonThemesMap, themeSuffixes, chameleonThemesMap.keySet(), rClassFinder.find(getPackageName(element)), ResourceType.values());
                generateThemes(chameleonThemesMap.keySet());
            }
        }
        return true;
    }

    private void generateThemeElementProviders(Map<ChameleonThemeEntity, List<String>> chameleonThemesMap, String[] themeSuffixes,
                                               Set<ChameleonThemeEntity> chameleonThemeEntitySet, RClass rClass, ResourceType... resourceTypes) {
        for (ResourceType resourceType : resourceTypes) {
            RInnerClass rInnerClass = rClass.get(resourceType);
            Map<String, List<String>> themeResourcesMap = rInnerClass != null ? themeResourceExtractor.getThemeResources(themeSuffixes, rInnerClass.getIdQualifiedNames()) : new HashMap<>();
            Map<String, Map<String, String>> javaDocInfoMap = javaDocInfoExtractor.generateJavaDocInfo(chameleonThemesMap, themeResourcesMap, chameleonThemeEntitySet);
            themeElementProviderGenerator.generateClass(chameleonThemesMap, javaDocInfoMap, themeResourcesMap, resourceType);
        }
    }

    private void generateThemes(Set<ChameleonThemeEntity> chameleonThemeEntitySet) {
        themesGenerator.generate(chameleonThemeEntitySet);
    }

    private boolean nothingToDo(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        return roundEnv.processingOver() || annotations.size() == 0;
    }

    private String[] getThemeSuffixes(Element element) {
        return element.getAnnotation(ChameleonThemes.class).suffixes();
    }

    private int[] getThemeSuffixesAmount(Element element) {
        return element.getAnnotation(ChameleonThemes.class).amount();
    }

    private String getPackageName(Element element) {
        while (element.getKind() != ElementKind.PACKAGE) {
            element = element.getEnclosingElement();
        }
        return ((PackageElement) element).getQualifiedName().toString();
    }
}
