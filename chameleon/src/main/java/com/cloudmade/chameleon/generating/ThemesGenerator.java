package com.cloudmade.chameleon.generating;

import org.apache.velocity.VelocityContext;

import java.util.Iterator;
import java.util.Set;

import static com.cloudmade.chameleon.generating.ClassGenerator.GENERATED_PACKAGE;

class ThemesGenerator {

    private static final String CHAMELEON_THEME_ENUM_NAME = "ChameleonTheme";

    private ClassGenerator classGenerator;

    ThemesGenerator(ClassGenerator classGenerator) {
        this.classGenerator = classGenerator;
    }

    void generate(Set<ChameleonThemeEntity> chameleonThemeEntitySet) {
        VelocityContext velocityContext = new VelocityContext();

        velocityContext.put("package", GENERATED_PACKAGE);
        velocityContext.put("chameleonThemes", getChameleonThemes(chameleonThemeEntitySet));

        classGenerator.writeClass(velocityContext, VelocityTemplate.CHAMELEON_THEMES, CHAMELEON_THEME_ENUM_NAME);
    }

    private String getChameleonThemes(Set<ChameleonThemeEntity> chameleonThemeEntitySet) {
        StringBuilder sb = new StringBuilder();
        Iterator<ChameleonThemeEntity> iterator = chameleonThemeEntitySet.iterator();
        while (iterator.hasNext()) {
            ChameleonThemeEntity theme = iterator.next();

            VelocityContext velocityContext = new VelocityContext();

            velocityContext.put("themeName", theme.getThemeName());
            velocityContext.put("themeSuffix", theme.getThemeSuffix());

            sb.append(classGenerator.mergeVelocityContext(velocityContext,
                    VelocityTemplate.CHAMELEON_THEME));
            sb.append(iterator.hasNext() ? "," : ";").append("\n");
        }
        return sb.toString();
    }
}
