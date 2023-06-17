package com.itkhanz.tests;

import com.itkhanz.base.AppFactory;
import com.itkhanz.constants.App;
import com.itkhanz.constants.PlatformOS;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Map;

import static com.itkhanz.base.DriverManager.getDriver;

public class SetupTest extends BaseTest{

    @Test
    public void test_androidLaunch() {
        //Load the capabilities of application based on platform and App constants
        Map<String, Object> appCapabilities = AppFactory.getAppCapabilities(PlatformOS.ANDROID, App.SAUCELABSANDROID, null);

        //Launch the application with specified capabilities by setting platform specific driver
        AppFactory.launchApp(PlatformOS.ANDROID, appCapabilities);

        WebElement element = getDriver().findElement(AppiumBy.xpath("//android.view.ViewGroup[@content-desc=\"container header\"]/android.widget.TextView"));
        Assert.assertEquals(element.getText(), "Products");
    }

    @Test
    public void test_iOSLaunch() {
        //Load the capabilities of application based on platform and App constants
        Map<String, Object> appCapabilities = AppFactory.getAppCapabilities(PlatformOS.IOS, App.SAUCELABSIOS, null);

        //Launch the application with specified capabilities by setting platform specific driver
        AppFactory.launchApp(PlatformOS.IOS, appCapabilities);

        WebElement element = getDriver().findElement(AppiumBy.iOSClassChain("**/XCUIElementTypeStaticText[`label == \"Products\"`]"));
        Assert.assertEquals(element.getText(), "Products");

    }

}
