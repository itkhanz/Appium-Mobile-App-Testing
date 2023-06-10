package com.itkhanz.practice.tests;

import com.itkhanz.practice.constants.App;
import com.itkhanz.practice.constants.PlatformOS;
import com.itkhanz.practice.utils.DriverManager;
import com.itkhanz.practice.utils.GestureUtils;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.Test;

import java.time.Duration;
import java.time.Instant;

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
    public void test_tap() {
        AppiumDriver driver = DriverManager.initializeDriver(PlatformOS.ANDROID, App.MYDEMOAPPANDROID, null, null);
        WebElement openMenu = driver.findElement(AppiumBy.accessibilityId("open menu"));
        GestureUtils.tap(driver, openMenu);

        //Assertion
        WebElement logInMenuItem = driver.findElement(AppiumBy.accessibilityId("menu item log in"));
        Assert.assertTrue(logInMenuItem.isDisplayed());
    }

    //performs double tap on the hamburger navigation icon to open and close the side menu
    @Test
    public void test_doubleTap() {
        AppiumDriver driver = DriverManager.initializeDriver(PlatformOS.ANDROID, App.MYDEMOAPPANDROID, null, null);
        WebElement openMenu = driver.findElement(AppiumBy.accessibilityId("open menu"));
        GestureUtils.doubleTap(driver, openMenu);

        //Since My Demo App does not have any double tap gesture, therefore no assertions are made
    }

    //performs long press on the element which opens up new dialog box
    @Test
    public void test_longPress() {
        AppiumDriver driver = DriverManager.initializeDriver(PlatformOS.ANDROID, App.APIDEMOS, null, null);

        driver.findElement(AppiumBy.xpath(".//*[@text='Views']")).click();
        driver.findElement(AppiumBy.xpath(".//*[@text='Expandable Lists']")).click();
        driver.findElement(AppiumBy.xpath(".//*[@text='1. Custom Adapter']")).click();
        WebElement element = driver.findElement(AppiumBy.xpath(".//*[@text='People Names']"));
        GestureUtils.longPress(driver, element);

        //Alternatively you can also perform longPress using Actions class instead of sequence class
        //new Actions(driver).clickAndHold(element).perform();

        WebElement sampleMenu = driver.findElement(AppiumBy.xpath(".//android.widget.TextView[@text='Sample menu']"));
        Assert.assertTrue(sampleMenu.isDisplayed());
    }

    //performs Zoom In gesture with both fingers on the drawing screen which draws a diagonal line
    @Test
    public void test_ZoomIn() {
        AppiumDriver driver = DriverManager.initializeDriver(PlatformOS.ANDROID, App.MYDEMOAPPANDROID, null, null);
        driver.findElement(AppiumBy.accessibilityId("open menu")).click();
        driver.findElement(AppiumBy.xpath("//android.view.ViewGroup[@content-desc=\"menu item drawing\"]")).click();
        WebElement element = driver.findElement(AppiumBy.xpath("//android.view.ViewGroup[@content-desc=\"drawing screen\"]"));
        GestureUtils.zoomIn(driver, element);
    }

    //performs Zoom Out gesture with both fingers on the drawing screen which draws a diagonal line
    @Test
    public void test_ZoomOut() {
        AppiumDriver driver = DriverManager.initializeDriver(PlatformOS.ANDROID, App.MYDEMOAPPANDROID, null, null);
        driver.findElement(AppiumBy.accessibilityId("open menu")).click();
        driver.findElement(AppiumBy.xpath("//android.view.ViewGroup[@content-desc=\"menu item drawing\"]")).click();
        WebElement element = driver.findElement(AppiumBy.xpath("//android.view.ViewGroup[@content-desc=\"drawing screen\"]"));
        GestureUtils.zoomOut(driver, element);
    }

    // Scrolls to the specific element
    @Test
    public void test_scroll() {
        AppiumDriver driver = DriverManager.initializeDriver(PlatformOS.ANDROID, App.APIDEMOS, null, null);
        driver.findElement(AppiumBy.xpath(".//*[@text='Views']")).click();

        //If element is not found within 30 seconds after continuous scrolling, then we break the loop
        Instant start = Instant.now();
        Instant end = Instant.now();
        long timeElapsed = Duration.between(start, end).toSeconds();

        //Use try-catch to avoid the Exception if element is not found (android generates XML dynamically so if element is not visible, it will not be found)
        boolean isElementVisible;
        By elementToScrollToLocator = AppiumBy.accessibilityId("Tabs");
        try {
            WebElement elementToScrollTo = driver.findElement(elementToScrollToLocator);
            isElementVisible = true;
        } catch (NoSuchElementException e) {
            System.out.println("element not found after " + timeElapsed + " seconds");
            isElementVisible = false;
        }


        //Keep on scrolling until element is found
        while (!isElementVisible && timeElapsed < 30) {
            GestureUtils.scroll(driver, "DOWN"); //scroll from the center of screen
            end = Instant.now();
            timeElapsed = Duration.between(start, end).toSeconds();
            try {
                WebElement elementToScrollTo = driver.findElement(elementToScrollToLocator);
                isElementVisible = true;
            } catch (NoSuchElementException e) {
                System.out.println("element not found after " + timeElapsed + " seconds");
            }

        }

        Assert.assertTrue(isElementVisible);
        Assert.assertTrue(timeElapsed < 30);
        System.out.println("Element scrolled to and found within " + timeElapsed + " seconds");
    }

}
