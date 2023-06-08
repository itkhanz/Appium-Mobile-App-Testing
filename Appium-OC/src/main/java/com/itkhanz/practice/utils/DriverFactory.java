package com.itkhanz.practice.utils;

import com.itkhanz.practice.constants.Apps;
import com.itkhanz.practice.constants.Platform;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.remote.MobileCapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class DriverFactory {

    public static AppiumDriver initializeDriver(Platform platform, Apps appName) throws MalformedURLException {
        URL url = new URL("http://0.0.0.0:4723");   //Appium Server URL and port
        Map<String, String> appCapabilities = getAppCapabilities(appName);
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability("newCommandTimeout", 300);

        switch(platform) {
            case ANDROID:
                caps.setCapability(MobileCapabilityType.PLATFORM_NAME ,platform.platformName);
                caps.setCapability(MobileCapabilityType.DEVICE_NAME ,"pixel_5");
                caps.setCapability(MobileCapabilityType.AUTOMATION_NAME ,"UiAutomator2");
                caps.setCapability(MobileCapabilityType.UDID ,"emulator-5554");
                caps.setCapability("appPackage", appCapabilities.get("appPackage"));
                caps.setCapability("appActivity", appCapabilities.get("appActivity"));
                caps.setCapability("autoGrantPermissions", "true");
                //No need to reinstall the app with Appium as it is already installed in emulator so APP capability is commented out
                //String appUrlAndroid = System.getProperty("user.dir") + "/src/main/resources/apps/ApiDemos-debug.apk";
                //caps.setCapability(MobileCapabilityType.APP, appUrlAndroid);
                //caps.setCapability("avd", "Pixel_5"); //automatically launches the android emulator with given avdID
                //caps.setCapability("app", appCapabilities.get("app"));
                //caps.setCapability("avdLaunchTimeout", 180000);
                return new AndroidDriver(url, caps);
            case IOS:
                caps.setCapability(MobileCapabilityType.PLATFORM_NAME ,platform.platformName);
                caps.setCapability(MobileCapabilityType.DEVICE_NAME ,"iPhone 14");
                caps.setCapability(MobileCapabilityType.AUTOMATION_NAME ,"XCUITest");
                caps.setCapability(MobileCapabilityType.UDID ,"6B4B083D-5F01-4B6D-88D1-175A4AFA3C4F");
                caps.setCapability("bundleId", appCapabilities.get("bundleId"));
                caps.setCapability("autoAcceptAlerts", "true");
                //No need to reinstall the app with Appium as it is already installed in emulator so APP capability is commented out
                //String appUrliOS = System.getProperty("user.dir") + "/src/main/resources/apps/UIKitCatalog-iphonesimulator.app";
                //caps.setCapability(MobileCapabilityType.APP, appUrliOS);
                //caps.setCapability("app", appCapabilities.get("app"));
                //caps.setCapability("simulatorStartupTimeout", 180000);
                return new IOSDriver(url, caps);
            default:
                throw new RuntimeException("Unable to create session with platform: " + platform.platformName);
        }
    }


    /**
     * This method generates the app specific capabilities for the appium session
     * @param appName Constant App identifier
     * @return Key,value pair of the app specific capabilities
     */
    private static Map<String, String> getAppCapabilities(Apps appName) {
        switch (appName) {
            case APIDEMOS:
                return Map.of("appPackage","io.appium.android.apis",
                        "appActivity", "io.appium.android.apis.ApiDemos",
                        "appUrl", System.getProperty("user.dir") + "/src/main/resources/apps/UIKitCatalog-iphonesimulator.app");
            case UIKITCATALOG:
                return Map.of("bundleId","com.example.apple-samplecode.UICatalog",
                        "appUrl", System.getProperty("user.dir") + "/src/main/resources/apps/UIKitCatalog-iphonesimulator.app");
            case MAPS:
                return Map.of("appPackage","com.google.android.apps.maps",
                        "appActivity", "com.google.android.maps.MapsActivity");
            case IOSMAPS:
                return Map.of("bundleId","com.apple.Maps");
            default:
                throw new RuntimeException("Invalid app: " + appName);
        }
    }
}
