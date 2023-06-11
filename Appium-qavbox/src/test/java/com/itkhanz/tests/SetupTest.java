package com.itkhanz.tests;

import com.itkhanz.base.AppFactory;
import com.itkhanz.constants.App;
import com.itkhanz.constants.PlatformOS;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

import static com.itkhanz.base.DriverManager.getDriver;

public class SetupTest {

    //TODO start appium server with plugins programmatically
    /*@BeforeTest
    public void startup() {
        AppiumServer.start();
    }

    @AfterTest
    public void teardown() {
        AppiumServer.stop();
    }*/

    @Test
    public void test_androidLaunch() {
        AppFactory.launchApp(PlatformOS.ANDROID, App.SAUCELABSANDROID, null, null);
        WebElement element = getDriver().findElement(AppiumBy.xpath("//android.view.ViewGroup[@content-desc=\"container header\"]/android.widget.TextView"));
        Assert.assertEquals(element.getText(), "Products");
        getDriver().quit();
    }

    @Test
    public void test_iOSLaunch() {
        AppFactory.launchApp(PlatformOS.IOS, App.SAUCELABSIOS, null, null);
        WebElement element = getDriver().findElement(AppiumBy.iOSClassChain("**/XCUIElementTypeStaticText[`label == \"Products\"`]"));
        Assert.assertEquals(element.getText(), "Products");
        getDriver().quit();
    }

}
