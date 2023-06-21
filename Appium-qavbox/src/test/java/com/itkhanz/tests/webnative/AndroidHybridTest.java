package com.itkhanz.tests.webnative;

import com.itkhanz.base.AppFactory;
import com.itkhanz.base.DriverManager;
import com.itkhanz.constants.App;
import com.itkhanz.constants.PlatformOS;
import com.itkhanz.tests.BaseTest;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.testng.annotations.Test;

import java.util.Map;
import java.util.Set;

/*
Elements inside a webview cannot be captured by Appium locator in usual way
To enable it, switch from Native App mode to Web/Hybrid App Mode by clicking on the globe icon on top of Appium inspector
Switch to Current Webview context from the dropdown on the right-side
Appium Inspector is not always reliable for the element locators in webview, using browser devtools is recommended
Android webview uses the chromedriver to update the webview
Run appium server with : appium --allow-insecure chromedriver_autodownload
This will auto download the browser binaries required to run the appium in webview
https://github.com/appium/appium/tree/1.x/docs/en/writing-running-appium/web
https://github.com/appium/appium/blob/1.x/docs/en/writing-running-appium/web/hybrid.md
https://github.com/appium/appium/blob/1.x/docs/en/writing-running-appium/web/mobile-web.md
https://github.com/appium/appium/blob/1.x/docs/en/writing-running-appium/web/chromedriver.md
 */
public class AndroidHybridTest extends BaseTest {

    @Test
    public void test_androidWebView() throws InterruptedException {
        //Load the capabilities of application based on platform and App constants
        Map<String, Object> appCapabilities = AppFactory.getAppCapabilities(PlatformOS.ANDROID, App.WDIOANDROID, null);

        //Launch the application with specified capabilities by setting platform specific driver
        AppFactory.launchApp(PlatformOS.ANDROID, appCapabilities);

        AndroidDriver driver = (AndroidDriver) DriverManager.getDriver();

        System.out.println("Current context " + driver.getContext());
        System.out.println("Current available contexts " + driver.getContextHandles());

        //Clicking on WebView tab opens up the website of webdriverIO
        driver.findElement(AppiumBy.accessibilityId("Webview")).click();
        Thread.sleep(10000);    //waiting because sometimes app takes longer to load webview

        Set<String> handles = driver.getContextHandles();
        System.out.println("Printing the available contexts");  // "NATIVE_APP", "WEBVIEW_com.wdiodemoapp"
        for (String contextName : handles) {
            System.out.println(contextName);
        }

        //switching to webview context to automate thw webview
        driver.context((String) handles.toArray()[1]); // we are on webview
        Thread.sleep(1000);
        System.out.println("after switch webview, Current context " + driver.getContext());

        //click on hamburger navigation icon
        driver.findElement(By.cssSelector("[aria-label='Toggle navigation bar']")).click();
        Thread.sleep(2000);

        //click to toggle theme
        driver.findElement(By.cssSelector("[class='toggle_vylO margin-right--md']")).click();
        Thread.sleep(2000);

        //click to close the sidebar navigation
        driver.findElement(By.cssSelector("[aria-label='Close navigation bar']")).click();

        //click on Getting started
        driver.findElement(By.cssSelector("[class='buttons_pzbO'] [href='/docs/gettingstarted']")).click();
        Thread.sleep(2000);

        //switch context back to native view to continue automating the native app
        driver.context("NATIVE_APP");
        Thread.sleep(5000);
        System.out.println("Current context " + driver.getContext());

        //click on the login tab and enter username
        driver.findElement(AppiumBy.accessibilityId("Login")).click();
        Thread.sleep(1000);
        driver.findElement(AppiumBy.accessibilityId("input-email")).sendKeys("qavbox");
        Thread.sleep(2000);

    }
    }
