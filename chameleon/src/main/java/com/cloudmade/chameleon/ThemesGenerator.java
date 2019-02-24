package com.cloudmade.chameleon;

import org.apache.velocity.VelocityContext;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import static com.cloudmade.chameleon.ClassGenerator.GENERATED_PACKAGE;

class ThemesGenerator {

    private static final String CHAMELEON_THEME_ENUM_NAME = "ChameleonTheme";

    private ClassGenerator classGenerator;

    ThemesGenerator(ClassGenerator classGenerator) {
        this.classGenerator = classGenerator;
    }

    void generate(Set<List<String>> themes) {
        VelocityContext velocityContext = new VelocityContext();

        velocityContext.put("package", GENERATED_PACKAGE);
        velocityContext.put("chameleonThemes", getChameleonThemes(themes));

        classGenerator.writeClass(velocityContext, VelocityTemplate.CHAMELEON_THEMES, CHAMELEON_THEME_ENUM_NAME);
    }

    private String getChameleonThemes(Set<List<String>> themeSuffixes) {
        StringBuilder sb = new StringBuilder();
        Iterator<List<String>> iterator = themeSuffixes.iterator();
        while (iterator.hasNext()) {
            List<String> theme = iterator.next();

            VelocityContext velocityContext = new VelocityContext();

            velocityContext.put("themeName", getThemeName(theme));
            velocityContext.put("themeSuffix", Algorithms.joinString(theme));

            sb.append(classGenerator.mergeVelocityContext(velocityContext,
                    VelocityTemplate.CHAMELEON_THEME));
            sb.append(iterator.hasNext() ? "," : ";").append("\n");
        }
        return sb.toString();
    }

    private String getThemeName(List<String> themeSuffixes) {
        StringBuilder sb = new StringBuilder();
        Iterator<String> iterator = themeSuffixes.iterator();
        while (iterator.hasNext()) {
            sb.append(iterator.next().toUpperCase().replaceAll("^[^0-9A-Z]+", ""));
            if (iterator.hasNext()) {
                sb.append("_");
            }
        }
        return sb.toString();
    }
}
