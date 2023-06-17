package com.itkhanz.practice.constants;

public enum PlatformOS {
    ANDROID ("Android"),
    IOS("iOS")
    ;
    public String platformName;

    PlatformOS(String platformName) { this.platformName = platformName; }
}
