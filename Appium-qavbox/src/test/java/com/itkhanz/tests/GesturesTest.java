package com.itkhanz.tests;

import com.itkhanz.base.AppFactory;
import com.itkhanz.base.DriverManager;
import com.itkhanz.constants.App;
import com.itkhanz.constants.PlatformOS;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.remote.MobileCapabilityType;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Map;

import static com.itkhanz.base.DriverManager.getDriver;
import static com.itkhanz.utils.GestureUtils.*;
import static com.itkhanz.utils.GestureUtils.ScrollDirection.*;
import static com.itkhanz.utils.WaitUtils.waitForElementToBeVisible;


/*
Performing Gestures using Appium's W3C actions with Sequence class
 */
public class GesturesTest extends BaseTest{

    //performs long press on the element which opens up new dialog box
    @Test
    public void test_longPress() {

        //Load the capabilities of application based on platform and App constants
        Map<String, Object> appCapabilities = AppFactory.getAppCapabilities(PlatformOS.ANDROID, App.APIDEMOS, null);

        //Add test-specific custom capabilities to the application
        appCapabilities.put(MobileCapabilityType.PLATFORM_NAME, "android");           //The type of platform hosting the app or browser
        appCapabilities.put(MobileCapabilityType.AUTOMATION_NAME, "uiautomator2");    //The name of the Appium driver to use
        appCapabilities.put("appWaitDuration", 30000);    //The maximum duration to wait until the appWaitActivity is focused in milliseconds (20000 by default)

        //Launch the application with specified capabilities by setting platform specific driver
        AppFactory.launchApp(PlatformOS.ANDROID, appCapabilities);

        getDriver().findElement(AppiumBy.xpath(".//*[@text='Views']")).click();
        getDriver().findElement(AppiumBy.xpath(".//*[@text='Expandable Lists']")).click();
        getDriver().findElement(AppiumBy.xpath(".//*[@text='1. Custom Adapter']")).click();
        WebElement element = getDriver().findElement(AppiumBy.xpath(".//*[@text='People Names']"));
        longPress(element, 3);

        //Alternatively you can also perform longPress using Actions class instead of sequence class
        //new Actions(driver).clickAndHold(element).perform();

        WebElement sampleMenu = getDriver().findElement(AppiumBy.xpath(".//android.widget.TextView[@text='Sample menu']"));
        Assert.assertTrue(sampleMenu.isDisplayed());

        //DriverManager.removeDriver(); //not needed when class extends from BaseTest which closes the driver in after hook
    }

    @Test
    public void test_scrollTillElement() throws InterruptedException {
        //Load the capabilities of application based on platform and App constants
        Map<String, Object> appCapabilities = AppFactory.getAppCapabilities(PlatformOS.IOS, App.IOSCONTACTS, null);

        //Launch the application with specified capabilities by setting platform specific driver
        AppFactory.launchApp(PlatformOS.IOS, appCapabilities);

        //For this test, manually add some contacts just once before running the test for first time so the list becomes longer and scrollable
        By contactLocator = AppiumBy.iOSClassChain("**/XCUIElementTypeCell[`label == \"Olivia\"`]");
        long timeout = 30;
        if (scrollTillElement(contactLocator, DOWN, 0.3, 500, timeout)) {
            WebElement contact = getDriver().findElement(contactLocator);
            Assert.assertTrue(contact.isDisplayed());
        } else {
            Assert.fail("Failed to find the element after scrolling for " + timeout + " seconds");
        }
    }

    @Test
    public void test_swipe_ios() {
        //Load the capabilities of application based on platform and App constants
        Map<String, Object> appCapabilities = AppFactory.getAppCapabilities(PlatformOS.IOS, App.WDIOIOS, null);

        //Launch the application with specified capabilities by setting platform specific driver
        AppFactory.launchApp(PlatformOS.IOS, appCapabilities);

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

    }

    @Test
    public void test_swipe_android() {
        //Load the capabilities of application based on platform and App constants
        Map<String, Object> appCapabilities = AppFactory.getAppCapabilities(PlatformOS.ANDROID, App.WDIOANDROID, null);

        //Launch the application with specified capabilities by setting platform specific driver
        AppFactory.launchApp(PlatformOS.ANDROID, appCapabilities);

        WebElement swipeMenuBtn = waitForElementToBeVisible(getDriver(), AppiumBy.accessibilityId("Swipe"), 5000);
        swipeMenuBtn.click();

        scroll(DOWN, 0.2, 600);    // scroll down (swipe up)

        scroll(RIGHT, 0.5, 600); //scroll in the horizontal direction to right
        scroll(RIGHT, 0.5, 600); //scroll again in the horizontal direction to right

        scroll(LEFT, 0.5, 600); //scroll in the horizontal direction to right
        scroll(LEFT, 0.5, 600); //scroll again in the horizontal direction to right

        scroll(UP, 0.2, 600);    // scroll up (swipe down)

    }
}
