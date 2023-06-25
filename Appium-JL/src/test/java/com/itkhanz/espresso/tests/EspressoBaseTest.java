package com.itkhanz.espresso.tests;

import com.google.common.collect.ImmutableMap;
import com.itkhanz.base.DriverManager;
import com.itkhanz.enums.Platform;
import com.itkhanz.utils.PropertyUtils;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.time.Duration;
import java.util.Map;

import static com.itkhanz.constants.Paths.APPS_DIR;

public class EspressoBaseTest {
    protected AppiumDriver driver;
    protected WebDriverWait wait;

    @BeforeMethod
    protected void setup() {
        Map<String, String> appProps = ImmutableMap.of(
                "appUrl", APPS_DIR + PropertyUtils.getAppProp("android_apiDemos_name")
        ) ;

        driver = DriverManager.initializeDriver(Platform.ANDROIDESPRESSO, appProps);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @AfterMethod(alwaysRun = true)
    protected void tearDown() {
        DriverManager.shutdownDriver();
    }
}
