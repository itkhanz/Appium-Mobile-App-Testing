package com.itkhanz.tests.chap5.ios;

import com.itkhanz.utils.PropertyUtils;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.options.XCUITestOptions;
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

        XCUITestOptions options = new XCUITestOptions()
                .setPlatformName("iOS")
                .setAutomationName("XCUITest")
                .setUdid(PropertyUtils.getDeviceProp("IOS_UDID"))
                .setBundleId("com.apple.mobileslideshow")
                .setAutoAcceptAlerts(true)
                ;

        driver = new IOSDriver(new URL(PropertyUtils.getDeviceProp("APPIUM_SERVER")), options);
    }

    @Test
    public void send_photo_test() throws IOException {
        String imageName = "appium-logo.png";
        File image = new File(TEST_DATA_DIR + imageName).getAbsoluteFile();
        ((IOSDriver)driver).pushFile(imageName, image);

        //TODO you can further write code to perform assertions for successfully transfer of photo to device
    }

    @AfterTest
    private void tearDown() {
        if (null != driver) {
            driver.quit();
        }
    }
}
