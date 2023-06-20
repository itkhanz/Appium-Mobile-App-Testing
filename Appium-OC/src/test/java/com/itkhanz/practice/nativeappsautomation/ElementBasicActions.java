package com.itkhanz.practice.nativeappsautomation;

import com.google.common.collect.ImmutableMap;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.remote.MobileCapabilityType;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebElement;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.net.MalformedURLException;
import java.net.URL;

public class ElementBasicActions {

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
            initializeDriver("Android");
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
    private void test_elementActions() throws InterruptedException {
        //click, sendKeys, clear
        //Workflow: click on Views, then scroll down to TextFields, and then enter text

        By views = AppiumBy.accessibilityId("Views");
        By textFields = AppiumBy.accessibilityId("TextFields");
        By editText = AppiumBy.id("io.appium.android.apis:id/edit");

        driver.findElement(views).click();

        //Swipe
        WebElement element = driver.findElement(AppiumBy.id("android:id/list"));

        driver.executeScript("mobile: swipeGesture", ImmutableMap.of(
                "elementId", ((RemoteWebElement) element).getId(),
                "direction", "up",
                "percent", 0.75
        ));

        driver.findElement(textFields).click();
        driver.findElement(editText).sendKeys("my text");
        Thread.sleep(3000);
        driver.findElement(editText).clear();


    }
    
}
