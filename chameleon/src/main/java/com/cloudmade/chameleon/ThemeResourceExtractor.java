package com.cloudmade.chameleon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

class ThemeResourceExtractor {

    @SuppressWarnings("Java8MapApi")
    Map<String, List<String>> getThemeResources(String[] themeSuffixes, Set<String> resources) {
        Map<String, List<String>> themeResourcesMap = new HashMap<>();
        for (String resource : resources) {
            if (isThemeResource(themeSuffixes, resource)) {
                String rawThemeResource = getRawThemeResource(themeSuffixes, resource);
                List<String> themeResources = themeResourcesMap.get(rawThemeResource);
                if (themeResources == null) {
                    themeResources = new ArrayList<>();
                    themeResourcesMap.put(rawThemeResource, themeResources);
                }
                themeResources.add(resource);
            }
        }
        return themeResourcesMap;
    }

    private String getRawThemeResource(String[] themeSuffixes, String themeResource) {
        int themeSuffixesIndex = Integer.MAX_VALUE;
        for (String themeSuffix : themeSuffixes) {
            int currentThemeSuffixIndex = themeResource.lastIndexOf(themeSuffix);
            if (currentThemeSuffixIndex != -1 && currentThemeSuffixIndex < themeSuffixesIndex) {
                themeSuffixesIndex = currentThemeSuffixIndex;
            }
        }

        return themeResource.substring(0, themeSuffixesIndex);
    }

    private boolean isThemeResource(String[] themeSuffixes, String resource) {
        for (String themeSuffix : themeSuffixes) {
            if (resource.endsWith(themeSuffix)) {
                return true;
            }
        }

        return false;
    }
}
