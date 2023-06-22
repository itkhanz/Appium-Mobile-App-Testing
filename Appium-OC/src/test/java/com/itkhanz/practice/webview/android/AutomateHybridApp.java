package com.itkhanz.practice.webview.android;

import com.google.common.collect.ImmutableMap;
import com.itkhanz.practice.constants.Apps;
import com.itkhanz.practice.constants.Platform;
import com.itkhanz.practice.base.DriverFactory;
import com.itkhanz.practice.base.DriverManager;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebElement;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.net.MalformedURLException;
import java.util.Set;

/*
Start the appium server with uiautomator2 driver (and better also with wait plugin)
appium --use-drivers=uiautomator2 --use-plugins=element-wait
Launch Hybrid app and Launch Chrome browser and navigate to the webpage  chrome://inspect#devices
ChromeDriver needs to be installed (a default version comes with Appium) and configured for automating the specific version of Chrome available on the device.
Use chromedriverExecutable capability to provide path to the ChromeDriver version.
Alternatively there is no need to manage ChromeDriver versions!
Appium can automatically download the compatible ChromeDriver and use it for automating the browser.
appium --allow-insecure chromedriver_autodownload
use chromedriverExecutableDir capability to provide path for appium to download and copy the browser executables

Documentation:-
https://github.com/appium/appium/tree/1.x/docs/en/writing-running-appium/web
https://appium.readthedocs.io/en/latest/en/writing-running-appium/web/hybrid/
 */
public class AutomateHybridApp {
    private static AppiumDriver driver;

    @BeforeTest
    public void setup() throws MalformedURLException {
        driver = DriverFactory.initializeDriver(Platform.ANDROID, Apps.APIDEMOS);
    }

    @AfterTest(alwaysRun = true)
    public void tearDown() {
        DriverManager.shutdownDriver();
    }

    /*
    Get all available contexts
    Switches to Webview context and locate the elements in webview and perform actions
    switch back again to the native view
     */
    @Test
    public void test_webview() throws InterruptedException {
        driver.findElement(AppiumBy.accessibilityId("Views")).click();

        WebElement element = driver.findElement(AppiumBy.id("android:id/list"));

        //scroll to WebView element at the bottom and click which opens up the webview
        driver.executeScript("mobile: swipeGesture", ImmutableMap.of(
                "elementId", ((RemoteWebElement)element).getId(),
                "direction", "up",
                "percent", "0.75"
        ));

        driver.findElement(AppiumBy.accessibilityId("WebView")).click();

        Thread.sleep(5000); //wait for webview to be loaded
        //Get Set of all available contexts
        Set<String> contextHandles = ((AndroidDriver)driver).getContextHandles();
        contextHandles.forEach(System.out::println);

        //switch to webview context i.e. at the first index in array
        ((AndroidDriver) driver).context(contextHandles.toArray()[1].toString());
        Assert.assertEquals(((AndroidDriver) driver).getContext(), "WEBVIEW_io.appium.android.apis");

        String pageHeading = driver.findElement(By.cssSelector("body > h1")).getText();
        Assert.assertEquals(pageHeading, "This page is a Selenium sandbox");

        String inputFieldName = driver.findElement(By.xpath("//*[@id=\"i_am_a_textbox\"]")).getAttribute("name");
        Assert.assertEquals(inputFieldName, "i_am_a_textbox");

        //switch back to native view
        ((AndroidDriver) driver).context("NATIVE_APP");
        Assert.assertEquals(((AndroidDriver) driver).getContext(), "NATIVE_APP");

    }
}
