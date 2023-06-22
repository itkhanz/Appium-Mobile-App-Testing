package com.itkhanz.practice.drivercommands.ios;

import com.google.common.collect.ImmutableMap;
import com.itkhanz.practice.constants.Apps;
import com.itkhanz.practice.constants.Platform;
import com.itkhanz.practice.base.DriverFactory;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.ios.IOSDriver;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;

import static com.itkhanz.practice.constants.Paths.APPS_DIR;

/*
Either you can use the Appium wrapper methods that are same for both andorid and iOS
or you can use the executeScript method to make call to native XCUITest methods
https://appium.github.io/appium-xcuitest-driver/4.30/execute-methods/
 */
public class IOSAppInteractions {
    private static AppiumDriver driver;

    @BeforeTest
    public void setup() throws MalformedURLException {
        driver = DriverFactory.initializeDriver(Platform.IOS, Apps.UIKITCATALOG);
    }

    @AfterTest(alwaysRun = true)
    public void tearDown() {
        DriverFactory.shutdownDriver();
    }

    /*
    mobile: terminateApp
    Terminates the given app on the device under test via XCTest's terminate API.
    If the app is not installed an exception is thrown.
    If the app is not running then nothing is done.
     */
    @Test
    public void test_terminateApp() throws InterruptedException {
        By activityIndicators = AppiumBy.accessibilityId("Activity Indicators");
        driver.findElement(activityIndicators).click();

        //bundleID The bundle identifier of the application to be terminated
        //boolean isTerminated = ((IOSDriver)driver).terminateApp("com.example.apple-samplecode.UICatalog");

        Map<String, Object> params = new HashMap<>();
        params.put("bundleId", "com.example.apple-samplecode.UICatalog");
        boolean isTerminated = (boolean) driver.executeScript("mobile: terminateApp", params);

        Assert.assertTrue(isTerminated);
    }

    /*
    mobile: installApp
    Installs the given application to the device under test.
     */
    @Test
    public void test_installApp() {
        By activityIndicators = AppiumBy.accessibilityId("Activity Indicators");
        driver.findElement(activityIndicators).click();

        String appUrl = APPS_DIR + "UIKitCatalog-iphonesimulator.app";
        //((IOSDriver) driver).installApp(appUrl);

        ((IOSDriver) driver).executeScript(
                "mobile: installApp", ImmutableMap.of(
                        "app", appUrl,
                        "timeoutMs", "50000"
        ));


        boolean isAppInstalled = (boolean) ((IOSDriver) driver).executeScript(
                "mobile: isAppInstalled", ImmutableMap.of(
                        "bundleId", "com.example.apple-samplecode.UICatalog"
                ));

        Assert.assertTrue(isAppInstalled);
    }

    /*
    mobile: removeApp
    Removes the given application from the device under test.
     */
    @Test
    public void test_removeApp() {
        //Uninstall the app
        ((IOSDriver) driver).executeScript(
                "mobile: removeApp", ImmutableMap.of(
                        "bundleId", "com.example.apple-samplecode.UICatalog"
                ));

        //check if app is uninstalled
        boolean isAppInstalled = (boolean) ((IOSDriver) driver).executeScript(
                "mobile: isAppInstalled", ImmutableMap.of(
                        "bundleId", "com.example.apple-samplecode.UICatalog"
                ));

        Assert.assertFalse(isAppInstalled);

        //re-install the app and check if it is reinstalled
        String appUrl = APPS_DIR + "UIKitCatalog-iphonesimulator.app";
        ((IOSDriver) driver).installApp(appUrl);
        boolean isReInstalled = ((IOSDriver) driver).isAppInstalled("com.example.apple-samplecode.UICatalog");
        Assert.assertTrue(isReInstalled);
    }

    /*
    mobile: backgroundApp
    Puts the app to the background and waits the given number of seconds.
    Then restores the app if necessary. The call is blocking.
     */
    @Test
    public void test_runAppInBackground() {
        By activityIndicators = AppiumBy.accessibilityId("Activity Indicators");
        driver.findElement(activityIndicators).click();

        //((IOSDriver) driver).runAppInBackground(Duration.ofMillis(5000));

        ((IOSDriver) driver).executeScript("mobile: backgroundApp", ImmutableMap.of(
                "seconds", "5"
        ));

    }

    /*
    mobile: activateApp
    Puts the given application to foreground if it is running in the background.
    An error is thrown if the app is not installed or is not running.
    Nothing is done if the app is already running in the foreground.
     */
    @Test
    public void test_activateApp() throws InterruptedException {
        By activityIndicators = AppiumBy.accessibilityId("Activity Indicators");
        driver.findElement(activityIndicators).click();
        ((IOSDriver)driver).terminateApp("com.example.apple-samplecode.UICatalog");

        //Get the bundleID of default iOS apps from
        //https://support.apple.com/en-ae/guide/deployment/depece748c41/web

        //opens the calendar app
        ((IOSDriver) driver).activateApp("com.apple.mobilecal");
        Thread.sleep(2500);

        //Opens the UIKitCatalog App
        //((IOSDriver) driver).activateApp("com.example.apple-samplecode.UICatalog");
        ((IOSDriver) driver).executeScript("mobile: activateApp", ImmutableMap.of(
                "bundleId", "com.example.apple-samplecode.UICatalog"
        ));
    }

    /*
    mobile: queryAppState
    Queries the state of an installed application from the device under test.
    An exception will be thrown if the app with given identifier is not installed.
    https://appium.github.io/appium-xcuitest-driver/4.30/execute-methods/#mobile-queryappstate
    https://developer.apple.com/documentation/xctest/xcuiapplicationstate?language=objc
     */
    @Test
    public void test_queryAppState() throws InterruptedException {
        By activityIndicators = AppiumBy.accessibilityId("Activity Indicators");
        driver.findElement(activityIndicators).click();

        var appState1 = ((IOSDriver)driver).queryAppState("com.example.apple-samplecode.UICatalog");
        Assert.assertEquals(appState1.toString(), "RUNNING_IN_FOREGROUND");

        ((IOSDriver)driver).terminateApp("com.example.apple-samplecode.UICatalog");
        Thread.sleep(2500);

        var appState2 = ((IOSDriver)driver).queryAppState("com.example.apple-samplecode.UICatalog");
        Assert.assertEquals(appState2.toString(), "NOT_RUNNING");


        //Using Script
        //An integer number is returned, which encodes the application state.
        //Possible values are described in XCUIApplicationState XCTest documentation topic.
        //https://developer.apple.com/documentation/xctest/xcuiapplicationstate?language=objc
        /*var result = ((IOSDriver) driver).executeScript(
                "mobile: queryAppState", ImmutableMap.of(
                        "bundleId", "com.example.apple-samplecode.UICatalog"
                ));
        System.out.println(result);*/   //Returns 4 i.e. XCUIApplicationStateRunningForeground = 4
    }

}
