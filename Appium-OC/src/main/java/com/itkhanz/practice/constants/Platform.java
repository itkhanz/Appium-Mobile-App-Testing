package com.itkhanz.practice.constants;

public enum Platform {
    ANDROID ("Android"),
    IOS("iOS"),
    CHROME("Chrome"),
    SAFARI("Safari");
    public String platformName;

    Platform(String platformName) {
        this.platformName = platformName;
    }
}
