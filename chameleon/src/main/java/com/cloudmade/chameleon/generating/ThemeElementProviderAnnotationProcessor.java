package com.cloudmade.chameleon.generating;

import com.cloudmade.chameleon.ChameleonThemes;
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
    private ThemesGenerator themesGenerator;

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
        themesGenerator = new ThemesGenerator(classGenerator);
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        if (!nothingToDo(set, roundEnvironment)) {
            for (Element element : roundEnvironment.getElementsAnnotatedWith(ChameleonThemes.class)) {
                String[] themeSuffixes = getThemeSuffixes(element);
                Map<ChameleonThemeEntity, List<String>> chameleonThemesMap = ChameleonThemesExtractor.extractChameleonThemes(themeSuffixes, getThemeSuffixesAmount(element));
                generateThemeElementProviders(chameleonThemesMap, themeSuffixes, chameleonThemesMap.keySet(), getPackageName(element), ResourceType.values());
                generateThemes(chameleonThemesMap.keySet());
            }
        }
        return true;
    }

    private void generateThemeElementProviders(Map<ChameleonThemeEntity, List<String>> chameleonThemesMap, String[] themeSuffixes,
                                               Set<ChameleonThemeEntity> chameleonThemeEntitySet, String packageName, ResourceType... resourceTypes) {
        RClass rClass = rClassFinder.find(packageName);
        for (ResourceType resourceType : resourceTypes) {
            RInnerClass rInnerClass = rClass.get(resourceType);
            Map<String, List<String>> themeResourcesMap = rInnerClass != null ? ThemeResourceExtractor.getThemeResources(themeSuffixes, rInnerClass.getIdQualifiedNames()) : new HashMap<>();
            Map<String, Map<String, String>> javaDocInfoMap = JavaDocInfoExtractor.generateJavaDocInfo(chameleonThemesMap, themeResourcesMap, chameleonThemeEntitySet);
            themeElementProviderGenerator.generateClass(chameleonThemesMap, javaDocInfoMap, themeResourcesMap, resourceType, packageName);
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
