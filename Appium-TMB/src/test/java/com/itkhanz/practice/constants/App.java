package com.itkhanz.practice.constants;

public enum App {
    APIDEMOS("APIDemos"),
    UIKITCATALOG("UIKitCatalog"),
    MAPS("GoogleMaps"),
    IOSMAPS("iOSMaps"),
    MYDEMOAPPANDROID("MyDemoApp-Android"),
    MYDEMOAPPIOS("MyDemoApp-iOS")
    ;
    public String appName;

    App(String appName) {
        this.appName = appName;
    }
}
