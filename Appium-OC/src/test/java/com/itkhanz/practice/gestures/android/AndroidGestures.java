package com.itkhanz.practice.gestures.android;

import com.google.common.collect.ImmutableMap;
import com.itkhanz.practice.annotations.TestMetaData;
import com.itkhanz.practice.constants.Apps;
import com.itkhanz.practice.constants.Platform;
import com.itkhanz.practice.utils.DriverFactory;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.PullsFiles;
import org.openqa.selenium.By;
import org.openqa.selenium.Rectangle;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebElement;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class AndroidGestures {

    private static AppiumDriver driver;

    @BeforeTest
    public void setup() throws MalformedURLException {
        driver = DriverFactory.initializeDriver(Platform.ANDROID, Apps.APIDEMOS);
        //driver = DriverFactory.initializeDriver(Platform.ANDROID, Apps.MAPS);
    }

    /*@AfterTest
    public void tearDown() {
        driver.quit();
    }*/

    /**
     * This gesture performs long click action on the given element/coordinates.
     * https://github.com/appium/appium-uiautomator2-driver/blob/master/docs/android-mobile-gestures.md#mobile-longclickgesture
     *
     * elementId: The id of the element to be clicked
     * x: The x-offset coordinate
     * y: The y-offset coordinate
     * duration: Click duration in milliseconds. 500 by default.
     */
    @Test
    public void test_longClick() {
        driver.findElement(AppiumBy.accessibilityId("Views")).click();
        driver.findElement(AppiumBy.accessibilityId("Drag and Drop")).click();
        WebElement element = driver.findElement(AppiumBy.id("io.appium.android.apis:id/drag_dot_1"));

        //Click by element ID
        driver.executeScript("mobile: longClickGesture", ImmutableMap.of(
            "elementId", ((RemoteWebElement) element).getId(),
            "duration", 1000
        ));

        //Click by element coordinates (Get the coordinates of elements from Appium Inspector)
        /*driver.executeScript("mobile: longClickGesture", ImmutableMap.of(
                "x", 217,
                "y", 659,
                "duration", 1000
        ));*/
    }

    /**
     * This gesture performs click action on the given element/coordinates.
     * https://github.com/appium/appium-uiautomator2-driver/blob/master/docs/android-mobile-gestures.md#mobile-clickgesture
     */
    @Test
    public void test_clickGesture() {
        WebElement element = driver.findElement(AppiumBy.accessibilityId("Views"));

        driver.executeScript("mobile: clickGesture", ImmutableMap.of(
                "elementId", ((RemoteWebElement) element).getId()
        ));
    }

    /**
     * This gesture performs drag action from the given element/coordinates to the given point.
     * https://github.com/appium/appium-uiautomator2-driver/blob/master/docs/android-mobile-gestures.md#mobile-draggesture
     */
    @Test
    public void test_dragGesture() {
        driver.findElement(AppiumBy.accessibilityId("Views")).click();
        driver.findElement(AppiumBy.accessibilityId("Drag and Drop")).click();
        WebElement elementToDrag = driver.findElement(AppiumBy.id("io.appium.android.apis:id/drag_dot_1"));
        WebElement elementToDrop = driver.findElement(AppiumBy.id("io.appium.android.apis:id/drag_dot_2"));
        //Calculate the middle point of the element to be dropped on
        Rectangle rec = elementToDrop.getRect();
        int px = rec.getX() + rec.getWidth()/2;
        int py = rec.getY() + rec.getHeight()/2;


        driver.executeScript("mobile: dragGesture", ImmutableMap.of(
                "elementId", ((RemoteWebElement) elementToDrag).getId(),
                "endX", px,
                "endY", py
        ));

        WebElement status = driver.findElement(AppiumBy.id("io.appium.android.apis:id/drag_result_text"));
        Assert.assertEquals(status.getText(), "Dropped!");
    }

    /**
     * This gesture performs pinch-open gesture on the given element/area. (Zoom In)
     * https://github.com/appium/appium-uiautomator2-driver/blob/master/docs/android-mobile-gestures.md#mobile-pinchopengesture
     * @throws InterruptedException
     */
    @Test
    public void test_pinchOpenGesture() throws InterruptedException {
        //IMPORTANT: Change the app in BeforeTest to initialize the drivers session for MAPS app
        //Since the locators are not loaded for maps in appium inspector so we are not using elementID to perform this gesture
        Thread.sleep(15000);

        //Co-ordinates can be un-reliable
        /*driver.executeScript("mobile: pinchOpenGesture", ImmutableMap.of(
                "left", 200,
                "top", 470,
                "width", 600,
                "height", 600,
                "percent", 0.75
        ));*/

        WebElement element = driver.findElement(AppiumBy.id("explore_tab_home_bottom_sheet"));
        driver.executeScript("mobile: pinchOpenGesture", ImmutableMap.of(
                "elementId", ((RemoteWebElement) element).getId(),
                "percent", 0.75
        ));

        Thread.sleep(5000);
    }

    /**
     * TThis gesture performs pinch-close gesture on the given element/area. (Zoom Out)
     * https://github.com/appium/appium-uiautomator2-driver/blob/master/docs/android-mobile-gestures.md#mobile-pinchopengesture
     * @throws InterruptedException
     */
    @Test
    @TestMetaData(app = Apps.MAPS, platform = Platform.ANDROID)
    public void test_pinchCloseGesture() throws InterruptedException {
        //IMPORTANT: Change the app in BeforeTest to initialize the drivers session for MAPS app
        //Since the locators are not loaded for maps in appium inspector so we are not using elementID to perform this gesture
        Thread.sleep(15000);
        driver.executeScript("mobile: pinchOpenGesture", ImmutableMap.of(
                "left", 200,
                "top", 470,
                "width", 600,
                "height", 600,
                "percent", 0.75
        ));
        Thread.sleep(5000);
        driver.executeScript("mobile: pinchCloseGesture", ImmutableMap.of(
                "left", 200,
                "top", 470,
                "width", 600,
                "height", 600,
                "percent", 0.75
        ));
        Thread.sleep(5000);
    }

    /**
     * This gesture performs vertical swipe gesture on the given element/area.
     * https://github.com/appium/appium-uiautomator2-driver/blob/master/docs/android-mobile-gestures.md#mobile-swipegesture
     * Swipe. Move one finger across the screen quickly. For example, swipe left on the Home Screen to see more apps.
     *
     * direction: Swipe direction. Mandatory value. Acceptable values are: up, down, left and right (case insensitive)
     *
     */
    @Test
    public void test_SwipeGesture_Vertical() throws InterruptedException {
        driver.findElement(AppiumBy.accessibilityId("Views")).click();
        Thread.sleep(3000);
        WebElement element = driver.findElement(AppiumBy.id("android:id/list"));

        driver.executeScript("mobile: swipeGesture", ImmutableMap.of(
                "left", 100, "top", 500, "width", 200, "height", 1500,
                "elementId", ((RemoteWebElement)element).getId(),
                "direction", "up",
                "percent", 0.75
        ));
    }

    /**
     * This gesture performs horizontal swipe gesture on the given element/area.
     * https://github.com/appium/appium-uiautomator2-driver/blob/master/docs/android-mobile-gestures.md#mobile-swipegesture
     * Swipe. Move one finger across the screen quickly. For example, swipe left on the Home Screen to see more apps.
     *
     * direction: Swipe direction. Mandatory value. Acceptable values are: up, down, left and right (case insensitive)
     *
     */
    @Test
    public void test_SwipeGesture_Horizontal() throws InterruptedException {
        driver.findElement(AppiumBy.accessibilityId("Views")).click();
        driver.findElement(AppiumBy.accessibilityId("Gallery")).click();
        driver.findElement(AppiumBy.accessibilityId("1. Photos")).click();

        WebElement element = driver.findElement(AppiumBy.xpath("//*[@resource-id='io.appium.android.apis:id/gallery']/android.widget.ImageView[1]"));

        driver.executeScript("mobile: swipeGesture", ImmutableMap.of(
                "elementId", ((RemoteWebElement)element).getId(),
                "direction", "left",
                "percent", 0.75
        ));
    }

    /**
     * This gesture performs scroll gesture on the given element/area.
     * https://github.com/appium/appium-uiautomator2-driver/blob/master/docs/android-mobile-gestures.md#mobile-scrollgesture
     * Scroll. Move one finger across the screen without lifting.
     * For example, in Photos, you can drag a list up or down to see more. Swipe to scroll quickly; touch the screen to stop scrolling.
     *
     * direction: Scrolling direction. Mandatory value. Acceptable values are: up, down, left and right (case insensitive)
     * percent: The size of the scroll as a percentage of the scrolling area size. Valid values must be float numbers greater than zero, where 1.0 is 100%. Mandatory value.
     *
     */
    @Test
    public void test_ScrollGesture_Vertical() throws InterruptedException {
        driver.findElement(AppiumBy.accessibilityId("Views")).click();

        driver.executeScript("mobile: scrollGesture", ImmutableMap.of(
                "left", 100, "top", 100, "width", 600, "height", 600,
                "direction", "down",
                "percent", 1.0
        ));
    }

    @Test
    public void test_ScrollGesture_vertical_till_bottom_of_screen(){
        driver.findElement(AppiumBy.accessibilityId("Views")).click();

        WebElement element = driver.findElement(AppiumBy.id("android:id/list"));
        //WebElement element = driver.findElement(AppiumBy.accessibilityId("Animation"));

        boolean canScrollMore = true;
        while (canScrollMore){
            canScrollMore = (Boolean) driver.executeScript("mobile: scrollGesture", ImmutableMap.of(
                    //"left", 100, "top", 100, "width", 600, "height", 600,
                    "elementId", ((RemoteWebElement)element).getId(),
                    "direction", "down",
                    "percent", 1.0
            ));
        }
        System.out.println(canScrollMore);
    }

    /**
     * First we scroll to the end of screen in right direction, and then we scroll back to the end of screen in opposite direction (left).
     * The code is generic and can work for any number of images.
     * @throws InterruptedException
     */
    @Test
    public void test_ScrollGesture_Horizontal() throws InterruptedException {
        driver.findElement(AppiumBy.accessibilityId("Views")).click();
        driver.findElement(AppiumBy.accessibilityId("Gallery")).click();
        driver.findElement(AppiumBy.accessibilityId("1. Photos")).click();

        //Algorithm: Since there were no specific attributes for the images in gallery and the list changes dynamically at the run time
        //therefore i had to write custom logic to compare the elements of the parent list and check if the current images on screen are same
        //then this means we cannot scroll more.
        scrollToTheEndHorizontally("right");
        Thread.sleep(3000);
        scrollToTheEndHorizontally("left");
    }

    private static void scrollToTheEndHorizontally(String scrollDirection) {
        By imageLocator = AppiumBy.xpath("//*[@resource-id='io.appium.android.apis:id/gallery']/android.widget.ImageView");
        List<WebElement> elementList = driver.findElements(imageLocator);
        List<String> elementListIDs = elementList.stream().map(el -> ((RemoteWebElement)el).getId()).collect(Collectors.toList());
        List<String> currentElementListIDs = new ArrayList<String>(elementListIDs);

        do {
            elementListIDs.clear();
            elementListIDs.addAll(currentElementListIDs);
            driver.executeScript("mobile: scrollGesture", ImmutableMap.of(
                    "left", 100, "top", 400, "width", 700, "height", 50,
                    "direction", scrollDirection,
                    "percent", 1
            ));

            currentElementListIDs.clear();
            elementList = driver.findElements(imageLocator);
            currentElementListIDs = elementList.stream().map(el -> ((RemoteWebElement)el).getId()).collect(Collectors.toList());

        } while (!elementListIDs.containsAll(currentElementListIDs));
    }

    /**
     * The following code uses UiScrollable(), scrollIntoView(), UiSelector(), and scrollable() to scroll down to an element until it is visible using Appium.
     * https://developer.android.com/training/testing/other-components/ui-automator
     * https://developer.android.com/reference/androidx/test/uiautomator/UiSelector
     * https://developer.android.com/reference/androidx/test/uiautomator/UiScrollable
     * https://developer.android.com/reference/androidx/test/uiautomator/UiObject
     * https://www.browserstack.com/guide/scroll-in-appium
     *
     */
    @Test
    public void test_scroll_until_element_is_visible() {
        driver.findElement(AppiumBy.accessibilityId("Views")).click();

        //UiScrollable is a UiCollection and provides support for searching for items in scrollable layout elements. This class can be used with horizontally or vertically scrollable controls.
        //scrollIntoView Perform a forward scroll action to move through the scrollable layout element until a visible item that matches the UiObject is found.

        //scroll down to the element and click
        /*driver.findElement(AppiumBy.androidUIAutomator(
                "new UiScrollable(new UiSelector().scrollable(true).instance(0))" +
                                ".scrollIntoView(new UiSelector().textContains(\"WebView\").instance(0))")
                )
                .click();*/

        //You can also choose to just pass the method of UiSelector class without creating the object of it
        driver.findElement(AppiumBy.androidUIAutomator(
                        "new UiScrollable(scrollable(true).instance(0))" +
                                ".scrollIntoView(textContains(\"WebView\").instance(0))")
                )
                .click();
    }

    public WebElement scrollToId_Android(AppiumDriver driver,String id) {
        return driver.findElement(AppiumBy.androidUIAutomator("new UiScrollable(new UiSelector().scrollable(true))"
                + ".scrollIntoView(new UiSelector().resourceIdMatches(\".*id.*\"))"));

    }


        //**********************************************
        //Below are few methods based on UiAutomator2
        //**********************************************
        /*// scrollForward (moves exactly one view)
        public void scrollForward_Android(AppiumDriver driver) {
            driver.findElement(
                    AppiumBy.androidUIAutomator("new UiScrollable(new UiSelector().scrollable(true)).scrollForward()"));
        }

        // scrollToBeginning (moves exactly by one view. 10 scrolls max)
        public void scrollToEnd_Android(AppiumDriver driver,int noOfScrolls) {
            driver.findElement(
                    AppiumBy.androidUIAutomator("new UiScrollable(new UiSelector().scrollable(true)).scrollToEnd("+noOfScrolls+")"));
        }

        // scrollToEnd (moves exactly by one view. 10 scrolls max)
        public void scrollToBeginning_Android(AppiumDriver driver,int noOfScrolls) {
            driver.findElement(AppiumBy
                    .androidUIAutomator("new UiScrollable(new UiSelector().scrollable(true)).scrollToBeginning("+noOfScrolls+")"));
        }*/
}
