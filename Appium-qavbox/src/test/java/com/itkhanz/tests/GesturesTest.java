package com.itkhanz.tests;

import com.itkhanz.base.AppFactory;
import com.itkhanz.base.DriverManager;
import com.itkhanz.constants.App;
import com.itkhanz.constants.PlatformOS;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import static com.itkhanz.base.DriverManager.getDriver;
import static com.itkhanz.utils.GestureUtils.*;
import static com.itkhanz.utils.GestureUtils.ScrollDirection.*;
import static com.itkhanz.utils.WaitUtils.waitForElementToBeVisible;


public class GesturesTest{

    @Test
    public void test_swipe_iOSContacts() {
        AppFactory.launchApp(PlatformOS.IOS, App.IOSCONTACTS, null, null);

        scroll(DOWN, 0.3, 500);
        DriverManager.removeDriver(); //not needed when class extends from BaseTest which closes the driver in after hook
    }

    @Test
    public void test_swipe_ios() {

        AppFactory.launchApp(PlatformOS.IOS, App.WDIOIOS, null, null);  //Launch wdio iOS app

        getDriver().findElement(AppiumBy.accessibilityId("Swipe")).click();

        scroll(DOWN, 0.2, 600);    // scroll down (swipe up)

        scroll(RIGHT, 0.5, 600); //scroll in the horizontal direction to right
        scroll(RIGHT, 0.5, 600); //scroll again in the horizontal direction to right
        WebElement jsFoundation = getDriver().findElement(AppiumBy.accessibilityId("JS.FOUNDATION"));
        Assert.assertTrue(jsFoundation.isDisplayed());

        scroll(LEFT, 0.5, 600); //scroll in the horizontal direction to right
        scroll(LEFT, 0.5, 600); //scroll again in the horizontal direction to right
        WebElement fullyOpenSource = getDriver().findElement(AppiumBy.accessibilityId("FULLY OPEN SOURCE"));
        Assert.assertTrue(fullyOpenSource.isDisplayed());

        scroll(UP, 0.2, 600);    // scroll up (swipe down)

        DriverManager.removeDriver(); //not needed when class extends from BaseTest which closes the driver in after hook
    }

    @Test
    public void test_swipe_android() {

        AppFactory.launchApp(PlatformOS.ANDROID, App.WDIOANDROID, null, null);  //Launch wdio android app

        WebElement swipeMenuBtn = waitForElementToBeVisible(getDriver(), AppiumBy.accessibilityId("Swipe"), 5000);
        swipeMenuBtn.click();

        scroll(DOWN, 0.2, 600);    // scroll down (swipe up)

        scroll(RIGHT, 0.5, 600); //scroll in the horizontal direction to right
        scroll(RIGHT, 0.5, 600); //scroll again in the horizontal direction to right

        scroll(LEFT, 0.5, 600); //scroll in the horizontal direction to right
        scroll(LEFT, 0.5, 600); //scroll again in the horizontal direction to right

        scroll(UP, 0.2, 600);    // scroll up (swipe down)

        DriverManager.removeDriver(); //not needed when class extends from BaseTest which closes the driver in after hook
    }
}
