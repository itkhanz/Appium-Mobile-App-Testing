package com.itkhanz.tests.webnative;

import com.itkhanz.base.AppFactory;
import com.itkhanz.base.DriverManager;
import com.itkhanz.constants.App;
import com.itkhanz.constants.PlatformOS;
import com.itkhanz.tests.BaseTest;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.ios.IOSDriver;
import org.openqa.selenium.By;

import org.testng.annotations.Test;

import java.util.Map;
import java.util.Set;

/*
Elements inside a webview cannot be captured by Appium locator in usual way
To enable it, switch from Native App mode to Web/Hybrid App Mode by clicking on the globe icon on top of Appium inspector
Switch to Current Webview context from the dropdown on the right-side
Appium Inspector is not always reliable for the element locators in webview, using browser is recommended
Webview context switch is currently only working till iOS 16.2
https://github.com/appium/appium/tree/1.x/docs/en/writing-running-appium/web
https://github.com/appium/appium/blob/1.x/docs/en/writing-running-appium/web/hybrid.md
https://github.com/appium/appium/blob/1.x/docs/en/writing-running-appium/web/mobile-web.md
 */
public class IOSHybridTest extends BaseTest {
    @Test
    public void test_iOSWebView() throws InterruptedException {
        //Load the capabilities of application based on platform and App constants
        Map<String, Object> appCapabilities = AppFactory.getAppCapabilities(PlatformOS.IOS, App.WDIOIOS, null);

        //Launch the application with specified capabilities by setting platform specific driver
        AppFactory.launchApp(PlatformOS.IOS, appCapabilities);

        IOSDriver driver = (IOSDriver)DriverManager.getDriver();

        System.out.println("Current context " + driver.getContext());
        System.out.println("Current available contexts " + driver.getContextHandles());

        //Clicking on WebView tab opens up the website of webdriverIO
        driver.findElement(AppiumBy.accessibilityId("Webview")).click();
        Thread.sleep(10000);    //waiting because sometimes app takes longer to load webview

        Set<String> handles = driver.getContextHandles();
        System.out.println("Printing the available contexts");
        for (String contextName : handles) {
            System.out.println(contextName); //prints out something like NATIVE_APP \n WEBVIEW_1
        }

        //switching to webview context
        driver.context((String) handles.toArray()[1]); // we are on webview
        Thread.sleep(1000);
        System.out.println("after switch webview, Current context " + driver.getContext());

        driver.findElement(By.cssSelector("[class='buttons_pzbO'] [href='/docs/gettingstarted']")).click();
        Thread.sleep(2000);

        driver.context("NATIVE_APP");
        driver.findElement(AppiumBy.accessibilityId("Login")).click();
        driver.findElement(AppiumBy.accessibilityId("input-email")).sendKeys("qavbox");
        Thread.sleep(2000);

    }
}
