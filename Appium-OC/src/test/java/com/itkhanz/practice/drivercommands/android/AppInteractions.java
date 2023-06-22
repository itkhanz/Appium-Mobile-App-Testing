package com.itkhanz.practice.drivercommands.android;

import com.itkhanz.practice.constants.Apps;
import com.itkhanz.practice.constants.Platform;
import com.itkhanz.practice.base.DriverFactory;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.appmanagement.AndroidInstallApplicationOptions;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.net.MalformedURLException;
import java.time.Duration;

import static com.itkhanz.practice.constants.Paths.APPS_DIR;

/*
AppiumDriver methods like resetApp, closeApp and launchApp have been deprecated
as they are going to be removed from the future Appium versions.
Alternatively, the suggested approach is to use removeApp, installApp, and activateApp
 */

public class AppInteractions {
    private static AppiumDriver driver;

    @BeforeTest
    public void setup() throws MalformedURLException {
        driver = DriverFactory.initializeDriver(Platform.ANDROID, Apps.APIDEMOS);
    }

    @AfterTest (alwaysRun = true)
    public void tearDown() {
        DriverFactory.shutdownDriver();
    }

    /*
    Puts the application to background
    Terminates an existing app
     */
    @Test
    public void test_appTermination() throws InterruptedException {
        By views = AppiumBy.accessibilityId("Views");
        driver.findElement(views).click();

        Thread.sleep(5000);

        boolean isTerminated = ((AndroidDriver) driver).terminateApp("io.appium.android.apis");    //appPackage
        Assert.assertTrue(isTerminated);
    }

    /*
    Test app upgrades (Installs the app if not already installed, or else update the app)
    Note: use terminate before installing
     */
    @Test
    public void test_appUpdate() throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        //installApp() takes the app capability as first argument
        String appUrl = APPS_DIR + "ApiDemos-debug.apk";
        ((AndroidDriver) driver).installApp(appUrl, new AndroidInstallApplicationOptions().withReplaceEnabled());


        //A toast message should appear "ApiDemos has been updated"
        try {
            //Multiple locator strategies for capturing toast element
            By toastView = AppiumBy.xpath("//android.widget.Toast[1]");
            //By toastView = AppiumBy.xpath("//android.widget.Toast[@text='ApiDemos has been updated!']");
            //By toastView = AppiumBy.className("android.widget.Toast");
            //By toastView = AppiumBy.androidUIAutomator("text(\"ApiDemos has been updated!\")");
            //By toastView = AppiumBy.androidUIAutomator("new UiSelector().text(\"ApiDemos has been updated!\")");
            //By toastView = AppiumBy.androidUIAutomator("new UiSelector().className(\"android.widget.Toast\")");
            WebElement toastMessage = wait.until(ExpectedConditions.presenceOfElementLocated(toastView));
            Assert.assertEquals(toastMessage.getText(), "ApiDemos has been updated!");
        } catch (TimeoutException e) {
            Assert.fail("Failed to capture the toast message within timeout");
        }

    }

    /*
    Checks if app is already installed
     */
    @Test
    public void test_appInstallation() {
        //isAppInstalled() takes the appPackage as argument
        boolean isInstalled = ((AndroidDriver) driver).isAppInstalled("io.appium.android.apis"); //takes appPackage as parameter
        Assert.assertTrue(isInstalled);
    }

    /*
    Run app in background
    sends app to background for specified time and then brings back to foreground
     */
    @Test
    public void test_runAppInBackground() throws InterruptedException {
        By views = AppiumBy.accessibilityId("Views");
        WebElement viewsTab = driver.findElement(views);
        viewsTab.click();

        ((AndroidDriver) driver).runAppInBackground(Duration.ofSeconds(5));

        Thread.sleep(2500);

        WebElement animation = driver.findElement(AppiumBy.accessibilityId("Animation"));
        Assert.assertTrue(animation.isDisplayed());
    }

    /*
    Activates an app and move it to foreground (the app should already be running)
    Switch between applications
     */
    @Test
    public void test_appActivation() throws InterruptedException {
        By views = AppiumBy.accessibilityId("Views");
        WebElement viewsTab = driver.findElement(views);
        viewsTab.click();

        //send the APIDemos app to background
        ((AndroidDriver) driver).terminateApp("io.appium.android.apis"); //takes appPackage as parameter
        Thread.sleep(2500);

        //Bring the settings app to foreground
        ((AndroidDriver) driver).activateApp("com.android.settings"); //takes appPackage as parameter
        Thread.sleep(2500);

        //Bring the APIDemos app to foreground
        ((AndroidDriver) driver).activateApp("io.appium.android.apis"); //takes appPackage as parameter
        Thread.sleep(2500);
    }

    /*
    Query application state
    Returns current app state e.g. RUNNING_IN_FOREGROUND
     */
    @Test
    public void test_appQueryState() throws InterruptedException {
        By views = AppiumBy.accessibilityId("Views");
        WebElement viewsTab = driver.findElement(views);
        viewsTab.click();

        //query the app state when it is running
        var appState1 = ((AndroidDriver) driver).queryAppState("io.appium.android.apis"); //takes appPackage as parameter
        System.out.println(appState1.toString());
        Assert.assertEquals(appState1.toString(), "RUNNING_IN_FOREGROUND");

        //query the app state when it is in background
        ((AndroidDriver) driver).activateApp("com.android.settings"); //takes appPackage as parameter
        Thread.sleep(2500);
        var appState2 = ((AndroidDriver) driver).queryAppState("io.appium.android.apis"); //takes appPackage as parameter
        System.out.println(appState2.toString());
        Assert.assertEquals(appState2.toString(), "RUNNING_IN_BACKGROUND");

        //query the app state after termination
        ((AndroidDriver) driver).terminateApp("io.appium.android.apis"); //takes appPackage as parameter
        Thread.sleep(2500);
        var appState3 = ((AndroidDriver) driver).queryAppState("io.appium.android.apis"); //takes appPackage as parameter
        System.out.println(appState3.toString());
        Assert.assertEquals(appState3.toString(), "NOT_RUNNING");
    }


    /*
    Install and uninstall the app
     */
    @Test
    public void test_appRemove() throws InterruptedException {
        boolean isInstalled = ((AndroidDriver) driver).isAppInstalled("io.appium.android.apis"); //takes appPackage as parameter
        Assert.assertTrue(isInstalled);

        if (isInstalled) {
            //Uninstall the application
            ((AndroidDriver) driver).removeApp("io.appium.android.apis");
            Thread.sleep(2500);

            //check if app is uninstalled
            boolean isInstalled2 = ((AndroidDriver) driver).isAppInstalled("io.appium.android.apis"); //takes appPackage as parameter
            Assert.assertFalse(isInstalled2);

            //re-install the app and check if it is installed
            String appUrl = APPS_DIR + "ApiDemos-debug.apk";
            ((AndroidDriver) driver).installApp(appUrl, new AndroidInstallApplicationOptions().withReplaceEnabled());
            Thread.sleep(5000);

            //check if app is installed
            boolean isInstalled3 = ((AndroidDriver) driver).isAppInstalled("io.appium.android.apis"); //takes appPackage as parameter
            Assert.assertTrue(isInstalled3);
        }
        else {
            Assert.fail("Could not test the uninstall because app is not installed, please install the app first!");
        }
    }

}
