package com.itkhanz.tests.chap5.android;

import com.itkhanz.utils.PropertyUtils;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

import static com.itkhanz.constants.Paths.TEST_DATA_DIR;

public class SendingPhotosTest {
    private AppiumDriver driver;

    @BeforeTest
    private void setUp() throws MalformedURLException {

        UiAutomator2Options options = new UiAutomator2Options()
                .setPlatformName("Android")
                .setAutomationName("UiAutomator2")
                .setUdid(PropertyUtils.getDeviceProp("ANDROID_UDID"))
                .setAppPackage("com.google.android.apps.photos")
                .setAppActivity(".home.HomeActivity")
                .setAutoGrantPermissions(true)
                ;

        driver = new AndroidDriver(new URL(PropertyUtils.getDeviceProp("APPIUM_SERVER")), options);
    }

    @Test
    public void send_photo_test() throws IOException {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));

        //legacy locators and flow for older android versions
        //By backupBtn = AppiumBy.id("auto_backup_switch");
        //By touchOutsideBtn = AppiumBy.id("touch_outside");
        //By keepOffBtn = AppiumBy.xpath("//*[@text='KEEP OFF']");
        //wait.until(ExpectedConditions.presenceOfElementLocated(backupBtn)).click();
        //wait.until(ExpectedConditions.presenceOfElementLocated(touchOutsideBtn)).click();
        //wait.until(ExpectedConditions.presenceOfElementLocated(keepOffBtn)).click();

        //not needed with autoGrantPermissions capability set to true
        //By allowNotificationsBtn = AppiumBy.id("permission_allow_button");
        //wait.until(ExpectedConditions.presenceOfElementLocated(allowNotificationsBtn)).click();

        By doNotBackupBtn = AppiumBy.id("onboarding_do_not_backup_button");
        wait.until(ExpectedConditions.presenceOfElementLocated(doNotBackupBtn)).click();

        File image = new File(TEST_DATA_DIR + "appium-logo.png").getAbsoluteFile();
        String Android_Photo_Path = "/sdcard/Pictures";
        ((AndroidDriver)driver).pushFile(Android_Photo_Path + "/" + image.getName(), image);

        By libraryTab = AppiumBy.id("tab_library");
        wait.until(ExpectedConditions.presenceOfElementLocated(libraryTab)).click();

        By picturesAlbum = AppiumBy.id("album_cover_view");
        wait.until(ExpectedConditions.presenceOfElementLocated(picturesAlbum)).click();

        By photo = AppiumBy.xpath("//android.view.ViewGroup[contains(@content-desc,'Photo taken')]");
        wait.until(ExpectedConditions.numberOfElementsToBe(photo, 1));
    }

    @AfterTest
    private void tearDown() {
        if (null != driver) {
            driver.quit();
        }
    }
}
