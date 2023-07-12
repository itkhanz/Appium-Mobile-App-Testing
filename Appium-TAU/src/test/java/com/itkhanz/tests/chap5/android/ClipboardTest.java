package com.itkhanz.tests.chap5.android;

import com.itkhanz.utils.PropertyUtils;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

import static com.itkhanz.constants.Paths.APPS_DIR;

public class ClipboardTest {
    private AppiumDriver driver;

    @BeforeTest
    private void setUp() throws MalformedURLException {

        UiAutomator2Options options = new UiAutomator2Options()
                .setPlatformName("Android")
                .setAutomationName("UiAutomator2")
                .setUdid(PropertyUtils.getDeviceProp("ANDROID_UDID"))
                .setApp(APPS_DIR + PropertyUtils.getAppProp("android_generalStore_name"))
                .setAutoGrantPermissions(true)
                ;

        driver = new AndroidDriver(new URL(PropertyUtils.getDeviceProp("APPIUM_SERVER")), options);
    }

    @Test
    public void clipboard_test() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        String textToCopy = "Hello TAU";
        ((AndroidDriver)driver).setClipboardText(textToCopy);

        WebElement nameText = wait.until(ExpectedConditions.presenceOfElementLocated(AppiumBy.id("nameField")));
        nameText.clear();

        String clipboardText = ((AndroidDriver)driver).getClipboardText();
        nameText.sendKeys(clipboardText);

        Assert.assertEquals(textToCopy, nameText.getText());
    }

    @AfterTest
    private void tearDown() {
        if (null != driver) {
            driver.quit();
        }
    }
}
