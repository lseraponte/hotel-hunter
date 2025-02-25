package com.lseraponte.cupidapi.hh.util;

public enum Language {
    ENGLISH("en"),
    FRENCH("fr"),
    SPANISH("es");

    private final String code;

    Language(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static Language fromString(String code) {
        if (code != null) {
            for (Language lang : Language.values()) {
                if (lang.code.equalsIgnoreCase(code)) {
                    return lang;
                }
            }
        }
        return ENGLISH; // Default to ENGLISH
    }
}
