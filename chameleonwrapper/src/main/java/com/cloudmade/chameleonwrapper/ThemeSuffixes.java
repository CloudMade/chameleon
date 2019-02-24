package com.cloudmade.chameleonwrapper;

import com.cloudmade.chameleon.ChameleonThemes;

@ChameleonThemes(suffixes = {
        ThemeSuffixes.DAY_THEME_SUFFIX,
        ThemeSuffixes.NIGHT_THEME_SUFFIX,
        ThemeSuffixes.LEFT_THEME_SUFFIX,
        ThemeSuffixes.RIGHT_THEME_SUFFIX,
        ThemeSuffixes.WINTER_THEME_SUFFIX,
        ThemeSuffixes.SPRING_THEME_SUFFIX,
        ThemeSuffixes.SUMMER_THEME_SUFFIX,
        ThemeSuffixes.AUTUMN_THEME_SUFFIX
}, amount = {2, 2, 4})
class ThemeSuffixes {

    static final String DAY_THEME_SUFFIX = "_day";
    static final String NIGHT_THEME_SUFFIX = "_night";

    static final String LEFT_THEME_SUFFIX = "_left";
    static final String RIGHT_THEME_SUFFIX = "_right";

    static final String WINTER_THEME_SUFFIX = "_winter";
    static final String SPRING_THEME_SUFFIX = "_spring";
    static final String SUMMER_THEME_SUFFIX = "_summer";
    static final String AUTUMN_THEME_SUFFIX = "_autumn";
}
