package com.itkhanz.practice.firstappiumproject;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.remote.MobileCapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.MalformedURLException;
import java.net.URL;

public class CreateDriverSession {
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
        DesiredCapabilities caps = getSessionCaps("Android", "pixel_5","UiAutomator2","emulator-5554", appUrl);
        AppiumDriver driver = new AndroidDriver(url, caps);
    }

    private static void createIOSSession(URL url) {
        //For getting iOS udid, run command: xcrun simctl list
        String appUrl = System.getProperty("user.dir") + "/src/main/resources/apps/UIKitCatalog-iphonesimulator.app";
        DesiredCapabilities caps = getSessionCaps("iOS", "iPhone 14","XCUITest","6B4B083D-5F01-4B6D-88D1-175A4AFA3C4F", appUrl);
        AppiumDriver driver = new IOSDriver(url, caps);
    }

    private static DesiredCapabilities getSessionCaps(String platformName, String deviceName, String automationName, String udid, String appPath){
        //https://appium.io/docs/en/2.0/guides/caps/
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability(MobileCapabilityType.PLATFORM_NAME ,platformName);
        caps.setCapability(MobileCapabilityType.DEVICE_NAME ,deviceName);
        caps.setCapability(MobileCapabilityType.AUTOMATION_NAME ,automationName);
        caps.setCapability(MobileCapabilityType.UDID ,udid);
        caps.setCapability(MobileCapabilityType.APP, appPath);
        return caps;
    }
}
