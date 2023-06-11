package com.itkhanz.constants;

public enum App {
    APIDEMOS("APIDemos"),
    UIKITCATALOG("UIKitCatalog"),
    MAPS("GoogleMaps"),
    IOSMAPS("iOSMaps"),
    SAUCELABSANDROID("MyDemoApp-Android"),
    SAUCELABSIOS("MyDemoApp-iOS"),
    WDIOANDROID("Wdio-NativeDemoApp-Android"),
    WDIOIOS("Wdio-NativeDemoApp-Android")
    ;
    public String appName;

    App(String appName) {
        this.appName = appName;
    }
}
