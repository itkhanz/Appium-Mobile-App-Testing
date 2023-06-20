package com.itkhanz.practice.drivercommands.ios;

import com.google.common.collect.ImmutableMap;
import com.itkhanz.practice.constants.Apps;
import com.itkhanz.practice.constants.Platform;
import com.itkhanz.practice.utils.DriverFactory;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.ios.IOSDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebElement;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.net.MalformedURLException;
import java.util.HashMap;

/*
https://appium.github.io/appium-xcuitest-driver/4.32.5/execute-methods/#mobile-hidekeyboard
https://appium.github.io/appium-xcuitest-driver/4.32.5/execute-methods/#mobile-iskeyboardshown
 */
public class IOSKeyboardInteraction {
    private static AppiumDriver driver;

    @BeforeTest
    public void setup() throws MalformedURLException {
        driver = DriverFactory.initializeDriver(Platform.IOS, Apps.UIKITCATALOG);
    }

    @AfterTest(alwaysRun = true)
    public void tearDown() {
        DriverFactory.shutdownDriver();
    }


    @Test
    public void test_keyboard() {
        WebElement element = driver.findElement(AppiumBy.accessibilityId("Text Fields"));

        driver.executeScript("mobile: scrollToElement", ImmutableMap.of(
                "elementId", ((RemoteWebElement) element).getId()
                )
        );

        element.click();

        driver.findElement(AppiumBy.xpath("//XCUIElementTypeCell[1]//XCUIElementTypeTextField")).click();

        boolean iskeyboardShown = (boolean) driver.executeScript("mobile: isKeyboardShown");
        Assert.assertTrue(iskeyboardShown);

        driver.findElement(AppiumBy.accessibilityId("a")).click();
        driver.findElement(AppiumBy.accessibilityId("b")).click();
        driver.findElement(AppiumBy.accessibilityId("c")).click();
        //driver.findElement(AppiumBy.accessibilityId("Done")).click();

        HashMap<String, String[]> map = new HashMap<String, String[]>();
        String[] keys = {"Done"};
        map.put("keys", keys);
        ((IOSDriver) driver).executeScript("mobile: hideKeyboard", map);

        iskeyboardShown = (boolean) driver.executeScript("mobile: isKeyboardShown");
        Assert.assertFalse(iskeyboardShown);


    }
}
