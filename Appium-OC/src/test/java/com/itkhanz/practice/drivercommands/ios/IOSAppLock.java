package com.itkhanz.practice.drivercommands.ios;

import com.google.common.collect.ImmutableMap;
import com.itkhanz.practice.constants.Apps;
import com.itkhanz.practice.constants.Platform;
import com.itkhanz.practice.base.DriverFactory;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.ios.IOSDriver;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.net.MalformedURLException;
/*
https://appium.github.io/appium-xcuitest-driver/4.32.5/execute-methods/#mobile-lock
 */
public class IOSAppLock {
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
    mobile: lock
    Lock the device (and optionally unlock it after a certain amount of time).
    Only simple (e.g. without a password) locks are supported.
     */
    @Test
    public void test_lock() {

        ((IOSDriver) driver).executeScript(
                "mobile: lock", ImmutableMap.of(
                        "seconds", "0"
                ));

        boolean isLocked = (boolean) ((IOSDriver) driver).executeScript("mobile: isLocked");
        Assert.assertTrue(isLocked);
    }

    @Test
    public void test_lockForDuration() {
        ((IOSDriver) driver).executeScript(
                "mobile: lock", ImmutableMap.of(
                        "seconds", "5"
                ));

        boolean isLocked = (boolean) ((IOSDriver) driver).executeScript("mobile: isLocked");

        Assert.assertFalse(isLocked);
    }

}
