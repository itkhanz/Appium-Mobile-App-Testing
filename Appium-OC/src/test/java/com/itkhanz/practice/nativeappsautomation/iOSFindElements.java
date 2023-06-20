package com.itkhanz.practice.nativeappsautomation;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.remote.MobileCapabilityType;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.net.MalformedURLException;
import java.net.URL;

public class iOSFindElements {

    private static AppiumDriver driver;

    private static void initializeDriver(String platformName) throws MalformedURLException {
        URL url = new URL("http://0.0.0.0:4723");   //Appium Server URL and port
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability("newCommandTimeout", 300);

        switch(platformName) {
            case "Android":
                caps.setCapability(MobileCapabilityType.PLATFORM_NAME ,"Android");
                caps.setCapability(MobileCapabilityType.DEVICE_NAME ,"pixel_5");
                caps.setCapability(MobileCapabilityType.AUTOMATION_NAME ,"UiAutomator2");
                caps.setCapability(MobileCapabilityType.UDID ,"emulator-5554");
                caps.setCapability("appPackage", "io.appium.android.apis");
                caps.setCapability("appActivity", "io.appium.android.apis.ApiDemos");
                //No need to reinstall the app with Appium as it is already installed in emulator so APP capability is commented out
                //String appUrlAndroid = System.getProperty("user.dir") + "/src/main/resources/apps/ApiDemos-debug.apk";
                //caps.setCapability(MobileCapabilityType.APP, appUrlAndroid);
                //caps.setCapability("avd", "Pixel_5"); //automatically launches the android emulator with given avdID
                //caps.setCapability("avdLaunchTimeout", 180000);
                driver = new AndroidDriver(url, caps);
                break;
            case "iOS":
                caps.setCapability(MobileCapabilityType.PLATFORM_NAME ,"iOS");
                caps.setCapability(MobileCapabilityType.DEVICE_NAME ,"iPhone 14");
                caps.setCapability(MobileCapabilityType.AUTOMATION_NAME ,"XCUITest");
                caps.setCapability(MobileCapabilityType.UDID ,"6B4B083D-5F01-4B6D-88D1-175A4AFA3C4F");
                caps.setCapability("bundleId", "com.example.apple-samplecode.UICatalog");
                //No need to reinstall the app with Appium as it is already installed in emulator so APP capability is commented out
                //String appUrliOS = System.getProperty("user.dir") + "/src/main/resources/apps/UIKitCatalog-iphonesimulator.app";
                //caps.setCapability(MobileCapabilityType.APP, appUrliOS);
                //caps.setCapability("simulatorStartupTimeout", 180000);
                driver = new IOSDriver(url, caps);
                break;
            default:
                throw new RuntimeException("Unable to create session with platform: " + platformName);
        }

    }

    @BeforeTest
    private void setup() {
        try {
            initializeDriver("iOS");
        } catch (MalformedURLException e) {
            System.out.println("Failed to initialize driver");
        }
    }


    @AfterTest
    private void teardown() {
        //we do not quit the driver session so we can later attach to the session with Appium Inspector
        //driver.quit();
    }

    @Test
    private void test_iOSFindElements() {

        //Locate the element via Accessibility ID (if not set explicitly by developer, then it is same as name attribute which takes value from label attribute)
        WebElement activityIndicatorElement = driver.findElement(AppiumBy.accessibilityId("Activity Indicators"));
        System.out.println(activityIndicatorElement.getText());

        //Locate the element via ID (name attribute)
        activityIndicatorElement = driver.findElement(AppiumBy.id("Activity Indicators"));
        System.out.println(activityIndicatorElement.getText());

        //Locate the element via Name (name attribute)
        activityIndicatorElement = driver.findElement(AppiumBy.name("Activity Indicators"));
        System.out.println(activityIndicatorElement.getText());

        //Locate the element via Class (type attribute)
        activityIndicatorElement = driver.findElements(AppiumBy.className("XCUIElementTypeStaticText")).get(1);
        System.out.println(activityIndicatorElement.getText());

        //Locate the element via XPath
        activityIndicatorElement = driver.findElement(AppiumBy.xpath("//XCUIElementTypeStaticText[@name=\"Activity Indicators\"]"));
        System.out.println(activityIndicatorElement.getText());

    }

    @Test
    public void test_iOSFindElementsUsingPredicateStringsNativeMethod() {
        //https://appium.github.io/appium-xcuitest-driver/4.19/ios-predicate/
        //https://developer.apple.com/library/archive/documentation/Cocoa/Conceptual/Predicates/Articles/pSyntax.html
        //https://github.com/facebookarchive/WebDriverAgent/wiki/Predicate-Queries-Construction-Rules

        //iOS Predicate String is a very good alternative for XPath because of native API
        //Appium does not have to traverse XML to get the xpath so this is much faster
        //In this case, Appium sends the string to XCTest framework which finds the element and return response back to appium

        //Locate the element via ios predicate string
        //find all the elements with type XCUIElementTypeStaticText and name Activity Indicators
        WebElement activityIndicatorElement = driver.findElement(AppiumBy.iOSNsPredicateString("type = \"XCUIElementTypeStaticText\" AND name == \"Activity Indicators\""));
        System.out.println(activityIndicatorElement.getText());

        //Locate the element via ios predicate string
        //find all the elements with label containing Activity Indicators
        activityIndicatorElement = driver.findElement(AppiumBy.iOSNsPredicateString("label CONTAINS \"Activity Indicators\""));
        System.out.println(activityIndicatorElement.getText());

    }



    
}
