package com.cloudmade.chameleon.generating;

import java.util.Iterator;
import java.util.List;

public class ChameleonThemeEntity {

    private String themeName;
    private String themeSuffix;

    private ChameleonThemeEntity(String themeName, String themeSuffix) {
        this.themeName = themeName;
        this.themeSuffix = themeSuffix;
    }

    String getThemeName() {
        return themeName;
    }

    String getThemeSuffix() {
        return themeSuffix;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChameleonThemeEntity that = (ChameleonThemeEntity) o;
        return themeName.equals(that.themeName) && themeSuffix.equals(that.themeSuffix);
    }

    @Override
    public int hashCode() {
        int prime = 31;
        int result = 1;
        result = prime * result + themeName.hashCode();
        result = prime * result + themeSuffix.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "ChameleonThemeEntity{" +
                "themeName='" + themeName + '\'' +
                ", themeSuffix='" + themeSuffix + '\'' +
                '}';
    }

    static ChameleonThemeEntity fromList(List<String> list) {
        return new ChameleonThemeEntity(getThemeName(list), Algorithms.joinString(list));
    }

    private static String getThemeName(List<String> themeSuffixes) {
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
