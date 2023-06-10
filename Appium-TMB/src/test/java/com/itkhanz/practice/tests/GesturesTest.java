package com.itkhanz.practice.tests;

import com.itkhanz.practice.constants.App;
import com.itkhanz.practice.constants.PlatformOS;
import com.itkhanz.practice.utils.DriverManager;
import com.itkhanz.practice.utils.GestureUtils;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.Test;

public class GesturesTest {
    @AfterTest (alwaysRun = true)
    private void tearDown() {
        DriverManager.shutdownDriver();
    }

    @Test
    public void test_android_setup() {
        AppiumDriver driver = DriverManager.initializeDriver(PlatformOS.ANDROID, App.MYDEMOAPPANDROID, null, null);
        WebElement element = driver.findElement(AppiumBy.xpath("//android.view.ViewGroup[@content-desc=\"container header\"]/android.widget.TextView"));
        Assert.assertEquals(element.getText(), "Products");
    }

    @Test
    public void test_ios_setup() {
        AppiumDriver driver = DriverManager.initializeDriver(PlatformOS.IOS, App.MYDEMOAPPIOS, null, null);
        WebElement element = driver.findElement(AppiumBy.iOSClassChain("**/XCUIElementTypeStaticText[`label == \"Products\"`]"));
        Assert.assertEquals(element.getText(), "Products");
    }

    //performs tap on the hamburger navigation icon to open the side menu
    @Test
    public void test_tap_gesture() {
        AppiumDriver driver = DriverManager.initializeDriver(PlatformOS.ANDROID, App.MYDEMOAPPANDROID, null, null);
        WebElement openMenu = driver.findElement(AppiumBy.accessibilityId("open menu"));
        GestureUtils.tap(driver, openMenu);

        //Assertion
        WebElement logInMenuItem = driver.findElement(AppiumBy.accessibilityId("menu item log in"));
        Assert.assertTrue(logInMenuItem.isDisplayed());
    }

}
