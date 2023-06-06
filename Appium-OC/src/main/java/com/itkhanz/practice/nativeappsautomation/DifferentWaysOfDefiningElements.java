package com.itkhanz.practice.nativeappsautomation;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.appium.java_client.pagefactory.iOSXCUITFindBy;
import io.appium.java_client.remote.MobileCapabilityType;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.net.MalformedURLException;
import java.net.URL;

public class DifferentWaysOfDefiningElements {
    private static AppiumDriver driver;

    @FindBy(xpath = "//*[@text='Accessibility']")
    @AndroidFindBy(xpath = "//*[@text='Accessibility']")
    @iOSXCUITFindBy(accessibility = "Accessibility")
    private static WebElement accessibilityElementFactory;

    //To initialize the annotated elements,they must be used in conjunction with PageFactory
    public DifferentWaysOfDefiningElements(AppiumDriver driver) {
        PageFactory.initElements(new AppiumFieldDecorator(driver), this);
    }

    public static void main(String[] args) {
        try {
            initializeDriver("Android");
        } catch (MalformedURLException e) {
            System.out.println("Failed to initialize driver");
        }

        //To initialize the constructor
        DifferentWaysOfDefiningElements differentWaysOfDefiningElements = new DifferentWaysOfDefiningElements(driver);

        differentWaysOfDefiningElements.test_FindElementsInDifferentWays();

        //driver.quit();
    }

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

    private void test_FindElementsInDifferentWays() {
        //WebElement, By, MobileBy, @FindBy, @AndroidFindBy, @iOSXCUITFindBy

        //By (lets you define the locator once and reuse it in many places)
        //It only gives us locator strategies only for web elements like XPath, ID, CSSSelector
        //This is recommended for hybrid and web applications
        By accessibilityElement = By.xpath("//*[@text='Accessibility']");
        System.out.println(driver.findElement(accessibilityElement).getText());

        //WebElement
        WebElement accessibilityElementWeb = driver.findElement(accessibilityElement);
        System.out.println(accessibilityElementWeb.getText());

        //For native applications, we can use AppiumBy which gives locators for accessibility ID, ios predicate string, ios Class Chain
        By accessibilityElementNative = AppiumBy.accessibilityId("Accessibility");
        System.out.println(driver.findElement(accessibilityElementNative).getText());

        //If application is developed both for iOS and android, and both have separate locators then we can use factory annotated methods
        //These methods must be defined at class level
        //If the tests are running on android device, appium will automatically use @AndroidFindBy
        //If the tests are running on iOS device, appium will automatically use @iOSXCUITFindBy
        System.out.println(accessibilityElementFactory.getText());
    }


}
