package com.itkhanz.tests.chap5.ios;

import com.itkhanz.utils.PropertyUtils;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.options.XCUITestOptions;
import org.openqa.selenium.Alert;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

import static com.itkhanz.constants.Paths.APPS_DIR;

public class AlertsTest {
    private AppiumDriver driver;

    @BeforeTest
    private void setUp() throws MalformedURLException {

        XCUITestOptions options = new XCUITestOptions()
                .setPlatformName("iOS")
                .setAutomationName("XCUITest")
                .setUdid(PropertyUtils.getDeviceProp("IOS_UDID"))
                .setApp(APPS_DIR + "UIKitCatalog-iphonesimulator.zip")
                ;

        driver = new IOSDriver(new URL(PropertyUtils.getDeviceProp("APPIUM_SERVER")), options);
    }

    @Test
    public void alertAccept_test() throws IOException {
        driver.findElement(AppiumBy.accessibilityId("Alert Views")).click();
        driver.findElement(AppiumBy.accessibilityId("Simple")).click();
        driver.switchTo().alert().accept();

    }

    @Test
    public void alertCancel_test() throws IOException {
        driver.findElement(AppiumBy.accessibilityId("Alert Views")).click();
        driver.findElement(AppiumBy.accessibilityId("Okay / Cancel")).click();
        driver.switchTo().alert().dismiss();

    }

    @Test
    public void alertText_test() throws IOException, InterruptedException {
        driver.findElement(AppiumBy.accessibilityId("Alert Views")).click();
        driver.findElement(AppiumBy.accessibilityId("Text Entry")).click();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.alertIsPresent());
        Alert alert = driver.switchTo().alert();
        alert.sendKeys("Hello TAU");
        System.out.println(alert.getText());
        alert.accept();
    }

    @AfterTest
    private void tearDown() {
        if (null != driver) {
            driver.quit();
        }
    }
}
