package com.itkhanz.practice.constants;

/**
 * This class stores the Application constants being used in different tests
 * This ENUM Constant is passed to DriverFactory to initialize the session for specific app
 */
public enum Apps {
    APIDEMOS("APIDemos"),
    UIKITCATALOG("UIKitCatalog"),
    MAPS("GoogleMaps")
    ;
    public String appName;

    Apps(String appName) {
        this.appName = appName;
    }

}
