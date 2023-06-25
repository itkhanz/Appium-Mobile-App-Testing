package com.itkhanz.enums;

public enum Platform {
    ANDROID("Android-UIAutomator2"),

    ANDROIDESPRESSO("Android-Espresso"),
    IOS("iOS-XCUITest");

    public String platformName;

    Platform(String platformName) { this.platformName = platformName; }
}
