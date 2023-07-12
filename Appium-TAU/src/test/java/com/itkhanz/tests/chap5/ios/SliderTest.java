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

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import static com.itkhanz.constants.Paths.APPS_DIR;

public class SliderTest {
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
    public void slider_test() throws IOException {
        driver.findElement(AppiumBy.accessibilityId("Sliders")).click();

        //value of slider should be in range 0 and 1
        WebElement slider = driver.findElement(AppiumBy.xpath("//XCUIElementTypeSlider"));
        slider.sendKeys("0");   //0%
        slider.sendKeys("1");   //100%
        slider.sendKeys("0.5");   //50%

        //the attribute value is 48 % so we need to escape this character
        Assert.assertEquals(slider.getAttribute("value").replace("\u00a0",""),"48%");
    }

    @AfterTest
    private void tearDown() {
        if (null != driver) {
            driver.quit();
        }
    }
}
