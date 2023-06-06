package com.itkhanz.practice.firstAppiumProject;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.remote.MobileCapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.MalformedURLException;
import java.net.URL;

public class LaunchPreInstalledApp {
    public static void main(String[] args) throws MalformedURLException {
        URL url = new URL("http://0.0.0.0:4723");   //Appium Server URL and port

        //Creating android Session
        createAndroidSession(url);

        //Creating iOS Session
        //createIOSSession(url);
    }

    private static void createAndroidSession(URL url){
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability(MobileCapabilityType.PLATFORM_NAME ,"Android");
        caps.setCapability(MobileCapabilityType.DEVICE_NAME ,"pixel_5");
        caps.setCapability(MobileCapabilityType.AUTOMATION_NAME ,"UiAutomator2");

        //For getting android udid, run command: adb devices
        caps.setCapability(MobileCapabilityType.UDID ,"emulator-5554");

        //For getting the app package and activity, open the app in emulator and run the below command in terminal
        //adb shell "dumpsys activity activities | grep mResumedActivity"
        //or
        //adb shell dumpsys window | grep -E mCurrentFocus
        caps.setCapability("appPackage", "io.appium.android.apis");
        //caps.setCapability("appActivity", "io.appium.android.apis.ApiDemos"); //Launches the app home page
        caps.setCapability("appActivity", "io.appium.android.apis.accessibility.CustomViewAccessibilityActivity");  //launches the app in specified activity state

        //If app is not installed in the emulator then you need to use the app capability and provide the path of the app.
        String appUrl = System.getProperty("user.dir") + "/src/main/resources/apps/ApiDemos-debug.apk";
        caps.setCapability(MobileCapabilityType.APP, appUrl);

        AppiumDriver driver = new AndroidDriver(url, caps);
    }

    private static void createIOSSession(URL url) {
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability(MobileCapabilityType.PLATFORM_NAME ,"iOS");
        caps.setCapability(MobileCapabilityType.DEVICE_NAME ,"iPhone 14");
        caps.setCapability(MobileCapabilityType.AUTOMATION_NAME ,"XCUITest");

        //For getting iOS udid, run command: xcrun simctl list
        caps.setCapability(MobileCapabilityType.UDID ,"6B4B083D-5F01-4B6D-88D1-175A4AFA3C4F");

        //For getting the iOS app Bundle ID, right-click the .app file and choose show Package contents, and then open  the info.plist file in XCode
        //Bundle identifier will be displayed here
        caps.setCapability("bundleId", "com.example.apple-samplecode.UICatalog");

        //If the app is already installed in simulator, then you can skip the app capability
        //String appUrl = System.getProperty("user.dir") + "/src/main/resources/apps/UIKitCatalog-iphonesimulator.app";
        //caps.setCapability(MobileCapabilityType.APP, appUrl);

        AppiumDriver driver = new IOSDriver(url, caps);
    }

}
