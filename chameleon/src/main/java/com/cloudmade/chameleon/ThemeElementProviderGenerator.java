package com.cloudmade.chameleon;

import org.apache.velocity.VelocityContext;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.cloudmade.chameleon.ClassGenerator.GENERATED_PACKAGE;

class ThemeElementProviderGenerator {

    private ClassGenerator classGenerator;

    ThemeElementProviderGenerator(ClassGenerator classGenerator) {
        this.classGenerator = classGenerator;
    }

    void generateClass(Map<List<String>, List<String>> themeSuffixesMap, Map<String, List<String>> resourcesMap, ResourceType resourceType) {
        Set<String> resources = resourcesMap.keySet();

        String themeElementProviderClassName = getThemeElementProviderClassName(resourceType);

        VelocityContext velocityContext = new VelocityContext();

        velocityContext.put("package", GENERATED_PACKAGE);
        velocityContext.put("themeElementProviderClassName", themeElementProviderClassName);
        velocityContext.put("themeElementProvideMethods", getThemeElementProvideMethods(resources));
        velocityContext.put("themeSuffixesInitializer", getThemeSuffixesInitializer(themeSuffixesMap));
        velocityContext.put("themeElementResources", getThemeElementResources(resourcesMap));
        velocityContext.put("themeElementProvidersInitializer", getThemeElementProvidersInitializer(resources));
        velocityContext.put("resourceDefType", resourceType.defType);

        classGenerator.writeClass(velocityContext, VelocityTemplate.THEME_ELEMENT_PROVIDER, themeElementProviderClassName);
    }

    private String getThemeSuffixesInitializer(Map<List<String>, List<String>> themeSuffixesMap) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<List<String>, List<String>> entry : themeSuffixesMap.entrySet()) {
            VelocityContext velocityContext = new VelocityContext();

            velocityContext.put("theme", Algorithms.joinString(entry.getKey()));
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

    private String getThemeElementProvideMethods(Set<String> themeResources) {
        StringBuilder themeElementProviderMethodsStringBuilder = new StringBuilder();
        for (String themeResource : themeResources) {
            VelocityContext velocityContext = new VelocityContext();

            velocityContext.put("themeElementProvideMethod", getThemeElementProviderMethodName(themeResource));
            velocityContext.put("themeElementProvideKey", themeResource);

            themeElementProviderMethodsStringBuilder.append(classGenerator.mergeVelocityContext(velocityContext,
                    VelocityTemplate.THEME_ELEMENT_PROVIDE_METHOD)).append("\n");
        }
        return themeElementProviderMethodsStringBuilder.toString();
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
