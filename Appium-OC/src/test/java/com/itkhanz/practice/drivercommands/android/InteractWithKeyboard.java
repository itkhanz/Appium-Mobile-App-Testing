package com.itkhanz.practice.drivercommands.android;

import com.itkhanz.practice.constants.Apps;
import com.itkhanz.practice.constants.Platform;
import com.itkhanz.practice.base.DriverFactory;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.net.MalformedURLException;
import java.time.Duration;

/*
Documentation
https://www.javadoc.io/doc/io.appium/java-client/6.0.0/io/appium/java_client/android/nativekey/PressesKey.html
https://developer.android.com/reference/android/view/KeyEvent
 */
public class InteractWithKeyboard {
    private static AppiumDriver driver;

    @BeforeTest
    public void setup() throws MalformedURLException {
        driver = DriverFactory.initializeDriver(Platform.ANDROID, Apps.APIDEMOS);
    }

    @AfterTest(alwaysRun = true)
    public void tearDown() {
        DriverFactory.shutdownDriver();
    }

    /*
    We can press the keys on android keyboard and also check if the keyboard is displayed or not
     */
    @Test
    public void test_keyboard() throws InterruptedException {
        driver.findElement(AppiumBy.accessibilityId("Views")).click();

        //Scroll to element using UiScrollable
        driver.findElement(AppiumBy.androidUIAutomator(
                        "new UiScrollable(scrollable(true).instance(0))" +
                                ".scrollIntoView(textContains(\"TextFields\").instance(0))")
                )
                .click();

        By editText = AppiumBy.id("io.appium.android.apis:id/edit");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement textField = wait.until(ExpectedConditions.visibilityOfElementLocated(editText));

        textField.click();
        Assert.assertTrue(((AndroidDriver) driver).isKeyboardShown());

        ((AndroidDriver) driver).pressKey(new KeyEvent().withKey(AndroidKey.A));
        ((AndroidDriver) driver).pressKey(new KeyEvent().withKey(AndroidKey.B));
        ((AndroidDriver) driver).pressKey(new KeyEvent().withKey(AndroidKey.C));

        Assert.assertEquals(textField.getText(), "abc");

        ((AndroidDriver) driver).hideKeyboard();
        Assert.assertFalse(((AndroidDriver) driver).isKeyboardShown());

    }

    /*
    KeyEvent is not only specific to the keyboard, it is for the device so we can enter the keys such as HOME, BACK
     */
    @Test
    public void test_deviceKeys() throws InterruptedException {
        By views = AppiumBy.accessibilityId("Views");
        driver.findElement(views).click();

        By gallery = AppiumBy.accessibilityId("Gallery");
        WebElement galleryElment = driver.findElement(gallery);
        galleryElment.click();

        Thread.sleep(2000);
        ((AndroidDriver) driver).pressKey(new KeyEvent().withKey(AndroidKey.BACK));
        Thread.sleep(2000);

        Assert.assertTrue(galleryElment.isDisplayed());

        ((AndroidDriver) driver).pressKey(new KeyEvent().withKey(AndroidKey.HOME));
        Thread.sleep(2000);
        var appState = ((AndroidDriver) driver).queryAppState("io.appium.android.apis"); //takes appPackage as parameter
        System.out.println(appState.toString());
        Assert.assertEquals(appState.toString(), "RUNNING_IN_BACKGROUND");
    }

    /*
    we can also open other apps with keyEvent such as calendar, calculator etc.
     */
    @Test
    public void test_openAppsWithKeyEvent() throws InterruptedException {
        ((AndroidDriver) driver).pressKey(new KeyEvent().withKey(AndroidKey.CALENDAR));
        Thread.sleep(2000);

        System.out.println(((AndroidDriver) driver).currentActivity());
        System.out.println(((AndroidDriver) driver).getCurrentPackage());

        Assert.assertEquals(((AndroidDriver) driver).getCurrentPackage(), "com.google.android.calendar");

    }
}
