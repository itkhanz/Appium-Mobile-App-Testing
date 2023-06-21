package com.itkhanz.tests.gestures;

import com.itkhanz.base.AppFactory;
import com.itkhanz.base.DriverManager;
import com.itkhanz.constants.App;
import com.itkhanz.constants.PlatformOS;
import com.itkhanz.tests.BaseTest;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.remote.MobileCapabilityType;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Map;

import static com.itkhanz.base.DriverManager.getDriver;
import static com.itkhanz.utils.GestureUtils.ScrollDirection.DOWN;
import static com.itkhanz.utils.GestureUtils.longPress;
import static com.itkhanz.utils.GestureUtils.scrollTillElement;
import static com.itkhanz.utils.WaitUtils.*;

/*
appium-gestures-plugin
Perform gestures with w3c actions
These actions are compatible with both iOS and Android
https://github.com/AppiumTestDistribution/appium-gestures-plugin
 */
public class GesturesPluginTest extends BaseTest {

    @Test
    public void test_swipe() throws InterruptedException {
        //Load the capabilities of application based on platform and App constants
        Map<String, Object> appCapabilities = AppFactory.getAppCapabilities(PlatformOS.ANDROID, App.WDIOANDROID, null);

        //Launch the application with specified capabilities by setting platform specific driver
        AppFactory.launchApp(PlatformOS.ANDROID, appCapabilities);

        WebElement swipeMenuBtn = waitForElementToBeVisible(getDriver(), AppiumBy.accessibilityId("Swipe"), 5000);
        swipeMenuBtn.click();

        RemoteWebElement verticalSlider = (RemoteWebElement) waitForElementToBeClickable(getDriver(), AppiumBy.accessibilityId("Swipe-screen"), 10000);
        RemoteWebElement horizontalSlider = (RemoteWebElement) waitForElementToBeClickable(getDriver(), AppiumBy.accessibilityId("Carousel"), 10000);

        //Swipe Left (scroll right)
        getDriver().executeScript(
                "gesture: swipe",
                Map.of("elementId", horizontalSlider.getId(),
                        "percentage", 50,
                        "direction", "left"
                )
        );

        Thread.sleep(3000);

        //Swipe Up (scroll down)
        getDriver().executeScript(
                "gesture: swipe",
                Map.of("elementId", verticalSlider.getId(),
                        "percentage", 50,
                        "direction", "up"
                )
        );
    }

    @Test
    public void test_longPress() {

        //Load the capabilities of application based on platform and App constants
        Map<String, Object> appCapabilities = AppFactory.getAppCapabilities(PlatformOS.ANDROID, App.APIDEMOS, null);

        //Launch the application with specified capabilities by setting platform specific driver
        AppFactory.launchApp(PlatformOS.ANDROID, appCapabilities);

        getDriver().findElement(AppiumBy.xpath(".//*[@text='Views']")).click();
        getDriver().findElement(AppiumBy.xpath(".//*[@text='Expandable Lists']")).click();
        getDriver().findElement(AppiumBy.xpath(".//*[@text='1. Custom Adapter']")).click();
        WebElement element = getDriver().findElement(AppiumBy.xpath(".//*[@text='People Names']"));

        //long press on element
        getDriver().executeScript(
                "gesture: longPress",
                Map.of("elementId", ((RemoteWebElement)element).getId(),
                        "pressure", 0.5,
                        "duration", "800"
                )
        );

        WebElement sampleMenu = getDriver().findElement(AppiumBy.xpath(".//android.widget.TextView[@text='Sample menu']"));
        Assert.assertTrue(sampleMenu.isDisplayed());
    }

    @Test
    public void test_scrollElementIntoView() throws InterruptedException {
        //Load the capabilities of application based on platform and App constants
        Map<String, Object> appCapabilities = AppFactory.getAppCapabilities(PlatformOS.IOS, App.IOSCONTACTS, null);

        //Launch the application with specified capabilities by setting platform specific driver
        AppFactory.launchApp(PlatformOS.IOS, appCapabilities);

        RemoteWebElement scrollView = (RemoteWebElement) waitForElementToBePresent(getDriver(), AppiumBy.iOSClassChain("**/XCUIElementTypeOther[`name == \"ContactsListView\"`]/XCUIElementTypeCollectionView"), 5000);

        ((JavascriptExecutor)getDriver()).executeScript("gesture: scrollElementIntoView", Map.of("scrollableView", scrollView.getId(),
                "strategy", "-ios class chain",
                "selector", "**/XCUIElementTypeCell[`label == \"Olivia\"`]",
                "percentage", 30,
                "direction", "up",
                "maxCount", 10));

        //For this test, manually add some contacts just once before running the test for first time so the list becomes longer and scrollable
        By contactLocator = AppiumBy.iOSClassChain("**/XCUIElementTypeCell[`label == \"Olivia\"`]");
        WebElement contact = getDriver().findElement(contactLocator);
        Assert.assertTrue(contact.isDisplayed());
    }

    @Test
    public void test_android_scrollElementIntoView() throws InterruptedException {
        //Load the capabilities of application based on platform and App constants
        Map<String, Object> appCapabilities = AppFactory.getAppCapabilities(PlatformOS.ANDROID, App.WDIOANDROID, null);

        //Launch the application with specified capabilities by setting platform specific driver
        AppFactory.launchApp(PlatformOS.ANDROID, appCapabilities);

        WebElement swipeMenuBtn = waitForElementToBeVisible(getDriver(), AppiumBy.accessibilityId("Swipe"), 5000);
        swipeMenuBtn.click();

        RemoteWebElement scrollView = (RemoteWebElement) waitForElementToBePresent(getDriver(), AppiumBy.accessibilityId("Swipe-screen"), 5000);

        getDriver().executeScript("gesture: scrollElementIntoView", Map.of("scrollableView", scrollView.getId(),
                "strategy", "accessibility id",
                "selector", "WebdriverIO logo",
                "percentage", 50,
                "direction", "up",
                "maxCount", 3));

        //For this test, manually add some contacts just once before running the test for first time so the list becomes longer and scrollable
        By logoLocator = AppiumBy.accessibilityId("WebdriverIO logo");
        WebElement logo = getDriver().findElement(logoLocator);
        Assert.assertTrue(logo.isDisplayed());
    }

    @Test
    public void test_DragAndDrop() {
        //Load the capabilities of application based on platform and App constants
        Map<String, Object> appCapabilities = AppFactory.getAppCapabilities(PlatformOS.ANDROID, App.APIDEMOS, null);

        //Launch the application with specified capabilities by setting platform specific driver
        AppFactory.launchApp(PlatformOS.ANDROID, appCapabilities);

        getDriver().findElement(AppiumBy.accessibilityId("Views")).click();
        getDriver().findElement(AppiumBy.accessibilityId("Drag and Drop")).click();
        WebElement elementToDrag = getDriver().findElement(AppiumBy.id("io.appium.android.apis:id/drag_dot_1"));
        WebElement elementToDrop = getDriver().findElement(AppiumBy.id("io.appium.android.apis:id/drag_dot_2"));

        getDriver().executeScript("gesture: dragAndDrop", Map.of("sourceId", ((RemoteWebElement)elementToDrag).getId(), "destinationId", ((RemoteWebElement)elementToDrop).getId()));

        WebElement status = getDriver().findElement(AppiumBy.id("io.appium.android.apis:id/drag_result_text"));
        Assert.assertEquals(status.getText(), "Dropped!");
    }

}
