package com.cloudmade.chameleon.generating;

import org.apache.velocity.VelocityContext;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.cloudmade.chameleon.generating.ClassGenerator.GENERATED_PACKAGE;

class ThemeElementProviderGenerator {

    private ClassGenerator classGenerator;

    ThemeElementProviderGenerator(ClassGenerator classGenerator) {
        this.classGenerator = classGenerator;
    }

    void generateClass(Map<ChameleonThemeEntity, List<String>> chameleonThemesMap, Map<String, Map<String, String>> javaDocInfoMap,
                       Map<String, List<String>> resourcesMap, ResourceType resourceType, String packageName) {
        Set<String> resources = resourcesMap.keySet();

        String themeElementProviderClassName = getThemeElementProviderClassName(resourceType);

        VelocityContext velocityContext = new VelocityContext();

        velocityContext.put("package", GENERATED_PACKAGE);
        velocityContext.put("themeElementProviderClassName", themeElementProviderClassName);
        velocityContext.put("themeElementProvideMethods", getThemeElementProvideMethods(resources, javaDocInfoMap, packageName, resourceType));
        velocityContext.put("themeSuffixesInitializer", getThemeSuffixesInitializer(chameleonThemesMap));
        velocityContext.put("themeElementResources", getThemeElementResources(resourcesMap));
        velocityContext.put("themeElementProvidersInitializer", getThemeElementProvidersInitializer(resources));
        velocityContext.put("resourceDefType", resourceType.defType);

        classGenerator.writeClass(velocityContext, VelocityTemplate.THEME_ELEMENT_PROVIDER, themeElementProviderClassName);
    }

    private String getThemeSuffixesInitializer(Map<ChameleonThemeEntity, List<String>> themeSuffixesMap) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<ChameleonThemeEntity, List<String>> entry : themeSuffixesMap.entrySet()) {
            VelocityContext velocityContext = new VelocityContext();

            velocityContext.put("theme", entry.getKey().getThemeSuffix());
            velocityContext.put("themeSuffixes", Algorithms.joinCollectionToStringWrappedInQuotes(entry.getValue()));

            sb.append(classGenerator.mergeVelocityContext(velocityContext,
                    VelocityTemplate.THEME_SUFFIXES_INITIALIZER)).append("\n");
        }
        return sb.toString();
    }

    private String getThemeElementResources(Map<String, List<String>> resourcesMap) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, List<String>> entry : resourcesMap.entrySet()) {
            VelocityContext velocityContext = new VelocityContext();

            velocityContext.put("themeElementProviderKey", entry.getKey());
            velocityContext.put("themeElementResources", Algorithms.joinCollectionToStringWrappedInQuotes(entry.getValue()));

            sb.append(classGenerator.mergeVelocityContext(velocityContext,
                    VelocityTemplate.THEME_ELEMENT_RESOURCE)).append("\n");
        }
        return sb.toString();
    }

    private String getThemeElementProvideMethods(Set<String> themeResources, Map<String, Map<String, String>> javaDocInfoMap,
                                                 String packageName, ResourceType resourceType) {
        StringBuilder themeElementProviderMethodsStringBuilder = new StringBuilder();
        for (String themeResource : themeResources) {
            VelocityContext velocityContext = new VelocityContext();

            velocityContext.put("themeElementProvideMethod", getThemeElementProviderMethodName(themeResource));
            velocityContext.put("themeElementProvideKey", themeResource);
            velocityContext.put("javaDocInfo", getJavaDocInfo(javaDocInfoMap.get(themeResource), packageName, resourceType));

            themeElementProviderMethodsStringBuilder.append(classGenerator.mergeVelocityContext(velocityContext,
                    VelocityTemplate.THEME_ELEMENT_PROVIDE_METHOD)).append("\n");
        }
        return themeElementProviderMethodsStringBuilder.toString();
    }

    private String getJavaDocInfo(Map<String, String> javaDocInfoMap, String packageName, ResourceType resourceType) {
        StringBuilder javaDocStringBuilder = new StringBuilder();
        Iterator<Map.Entry<String, String>> iterator = javaDocInfoMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, String> entry = iterator.next();

            VelocityContext velocityContext = new VelocityContext();

            velocityContext.put("theme", entry.getKey());
            velocityContext.put("resource", String.format("%s.R.%s.%s" , packageName, resourceType.defType, entry.getValue()));

            javaDocStringBuilder.append(classGenerator.mergeVelocityContext(velocityContext, VelocityTemplate.JAVA_DOC_INFO));
            if (iterator.hasNext()) {
                javaDocStringBuilder.append("\n");
            }
        }

        return javaDocStringBuilder.toString();
    }

    private String getThemeElementProvidersInitializer(Set<String> themeResources) {
        StringBuilder themeElementProvidersInitializerStringBuilder = new StringBuilder();
        for (String themeResource : themeResources) {
            VelocityContext velocityContext = new VelocityContext();

            velocityContext.put("themeElementProviderKey", themeResource);

            themeElementProvidersInitializerStringBuilder.append(classGenerator.mergeVelocityContext(velocityContext,
                    VelocityTemplate.THEME_ELEMENT_PROVIDER_INITIALIZER)).append("\n");
        }
        return themeElementProvidersInitializerStringBuilder.toString();
    }

    private String getThemeElementProviderClassName(ResourceType resourceType) {
        return String.format("Theme%sProvider", Algorithms.capitalizeFirstLetter(resourceType.defType));
    }

    private String getThemeElementProviderMethodName(String themeResource) {
        String[] split = themeResource.split("_");
        StringBuilder themeElementProviderMethodNameStringBuilder = new StringBuilder();
        for (int i = 0; i < split.length; i++) {
            String methodNamePart = i == 0 ? split[i] : Algorithms.capitalizeFirstLetter(split[i]);
            themeElementProviderMethodNameStringBuilder.append(methodNamePart);
        }
        return themeElementProviderMethodNameStringBuilder.toString();
    }
}
