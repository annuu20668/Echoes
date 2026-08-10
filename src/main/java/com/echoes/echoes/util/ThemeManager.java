package com.echoes.echoes.util;

public class ThemeManager {

    public enum Theme {
        MIDNIGHT,
        SOFT_DAY
    }

    private static Theme currentTheme = Theme.MIDNIGHT;


    // =========================================================
    // GET CURRENT THEME
    // =========================================================

    public static Theme getCurrentTheme() {

        return currentTheme;
    }


    // =========================================================
    // SET THEME
    // =========================================================

    public static void setTheme(Theme theme) {

        if (theme == null) {
            return;
        }

        currentTheme = theme;
    }


    // =========================================================
    // CSS FILE
    // =========================================================

    public static String getThemeStylesheet() {

        if (currentTheme == Theme.SOFT_DAY) {

            return ThemeManager.class
                    .getResource("/css/soft-day.css")
                    .toExternalForm();
        }

        return null;
    }
}