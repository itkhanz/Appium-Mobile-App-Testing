package com.itkhanz.constants;

public enum App {
    APIDEMOS("APIDemos"),
    UIKITCATALOG("UIKitCatalog"),
    MAPS("GoogleMaps"),
    IOSMAPS("iOSMaps"),
    SAUCELABSANDROID("MyDemoApp-Android"),
    SAUCELABSIOS("MyDemoApp-iOS"),
    WDIOANDROID("Wdio-NativeDemoApp-Android"),
    WDIOIOS("Wdio-NativeDemoApp-iOS"),
    IOSCONTACTS("iOSContacts")
    ;
    private String id;  //Identifier for the app (this must be same as   specified in applications.json id)

    App(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
