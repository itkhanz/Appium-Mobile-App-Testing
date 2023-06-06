package com.itkhanz.practice.firstappiumproject;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.options.XCUITestOptions;

import java.net.MalformedURLException;
import java.net.URL;

public class CreateDriverSessionUsingOptions {
    public static void main(String[] args) throws MalformedURLException {
        URL url = new URL("http://0.0.0.0:4723");   //Appium Server URL and port

        //Creating android Session
        createAndroidSession(url);

        //Creating iOS Session
        //createIOSSession(url);
    }

    private static void createAndroidSession(URL url){
        //For getting android udid, run command: adb devices
        String appUrl = System.getProperty("user.dir") + "/src/main/resources/apps/ApiDemos-debug.apk";
        UiAutomator2Options options = new UiAutomator2Options()
                .setDeviceName("pixel_5")
                .setAutomationName("UiAutomator2")
                .setUdid("emulator-5554")
                .setApp(appUrl)
                ;
        AppiumDriver driver = new AndroidDriver(url, options);
    }

    private static void createIOSSession(URL url) {
        //For getting iOS udid, run command: xcrun simctl list
        String appUrl = System.getProperty("user.dir") + "/src/main/resources/apps/UIKitCatalog-iphonesimulator.app";
        XCUITestOptions options = new XCUITestOptions()
                .setDeviceName("iPhone 14")
                .setAutomationName("XCUITest")
                .setUdid("6B4B083D-5F01-4B6D-88D1-175A4AFA3C4F")
                .setApp(appUrl)
                ;
        AppiumDriver driver = new IOSDriver(url, options);
    }
}
