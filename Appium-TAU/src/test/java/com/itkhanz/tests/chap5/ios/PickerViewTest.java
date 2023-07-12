package com.itkhanz.tests.chap5.ios;

import com.itkhanz.utils.PropertyUtils;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.options.XCUITestOptions;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import static com.itkhanz.constants.Paths.APPS_DIR;
import static com.itkhanz.constants.Paths.TEST_DATA_DIR;

public class PickerViewTest {
    private AppiumDriver driver;

    @BeforeTest
    private void setUp() throws MalformedURLException {

        XCUITestOptions options = new XCUITestOptions()
                .setPlatformName("iOS")
                .setAutomationName("XCUITest")
                .setUdid(PropertyUtils.getDeviceProp("IOS_UDID"))
                .setApp(APPS_DIR + "UIKitCatalog-iphonesimulator.zip")
                .setAutoAcceptAlerts(true)
                ;

        driver = new IOSDriver(new URL(PropertyUtils.getDeviceProp("APPIUM_SERVER")), options);
    }

    @Test
    public void pickerView_test() throws IOException {
        driver.findElement(AppiumBy.accessibilityId("Picker View")).click();
        driver.findElement(AppiumBy.accessibilityId("Red color component value")).sendKeys("30");
        driver.findElement(AppiumBy.accessibilityId("Green color component value")).sendKeys("200");

        WebElement blue = driver.findElement(AppiumBy.accessibilityId("Blue color component value"));
        blue.sendKeys("100");

        Assert.assertEquals(blue.getText(), "100");
    }

    @AfterTest
    private void tearDown() {
        if (null != driver) {
            driver.quit();
        }
    }
}
