package com.cloudmade.chameleon.generating;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

class JavaDocInfoExtractor {

    private JavaDocInfoExtractor() {}

    static Map<String, Map<String, String>> generateJavaDocInfo(Map<ChameleonThemeEntity, List<String>> chameleonThemesMap, Map<String, List<String>> themeResourcesMap, Set<ChameleonThemeEntity> themeSuffixes) {
        Map<String, Map<String, String>> javaDocInfoMap = new HashMap<>();
        for (Map.Entry<String, List<String>> entry : themeResourcesMap.entrySet()) {
            Map<String, String> themeResourceMap = new HashMap<>();
            for (ChameleonThemeEntity chameleonThemeEntity : themeSuffixes) {
                themeResourceMap.put(chameleonThemeEntity.getThemeName(), getResourceNameForTheme(entry.getKey(), chameleonThemesMap, themeResourcesMap, chameleonThemeEntity));
            }
            javaDocInfoMap.put(entry.getKey(), themeResourceMap);
        }
        return javaDocInfoMap;
    }

    static private String getResourceNameForTheme(String rawResourceName, Map<ChameleonThemeEntity, List<String>> chameleonThemesMap,
                                                  Map<String, List<String>> themeResourcesMap, ChameleonThemeEntity chameleonThemeEntity) {
        List<String> possibleResourceSuffixes = chameleonThemesMap.get(chameleonThemeEntity);
        List<String> themedResourceNames = themeResourcesMap.get(rawResourceName);
        for (String possibleResourceSuffix : possibleResourceSuffixes) {
            if (themedResourceNames.contains(rawResourceName + possibleResourceSuffix)) {
                return rawResourceName + possibleResourceSuffix;
            }
        }

        System.err.println(String.format("Cannot get theme resource for resource %1$s for theme %2$s", rawResourceName, chameleonThemeEntity.getThemeName()));
        return themedResourceNames.iterator().next();
    }
}
