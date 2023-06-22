package com.itkhanz.practice.webview.ios;

import com.itkhanz.practice.base.DriverFactory;
import com.itkhanz.practice.base.DriverManager;
import com.itkhanz.practice.constants.Apps;
import com.itkhanz.practice.constants.Platform;
import com.itkhanz.practice.utils.Gestures;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.ios.IOSDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.net.MalformedURLException;
import java.util.Set;

/*
Inspecting Hybrid App/Mobile Browser elements:
- Appium inspector may be able to inspect the WebView elements, but natively. In that case, need to define different selectors for Android and iOS
- WebView elements can be inspected using Chrome remote debugger or Safari Web Inspector
- WebView elements are common for both Android and iOS
- For Android, in order to inspect WebView elements, developer need to set setWebContentsDebuggingEnabled to true within the app
- For Android, the default ChromeDriver version in Appium should be compatible with the Chrome browser version on the device
- For iOS, Appium can automate WkWebView and UIWebView elements, but not SafariViewController elements.

iOS: Hybrid app and Safari browser: Inspecting WebView elements using the Web Inspector (Simulator)
——————————————————————————————————————————————
1. Launch Hybrid app and navigate to the WebView page OR Launch Safari browser and navigate to the webpage
2. Open desktop Safari
3. Enable “Develop” menu option if not already enabled. Got to Safari -> Preferences… -> Advanced menu and enable “Show Develop menu in menu bar” option
4. Go to Develop Menu -> Select Simulator and then the WebView page/Browser page -> It opens up the Web Inspector
5. Copy the CSS or XPath or any other locator as you would do in Selenium
 */
public class HybridAppAutomation {
    private static AppiumDriver driver;

    @BeforeTest
    public void setup() throws MalformedURLException {
        driver = DriverFactory.initializeDriver(Platform.IOS, Apps.UIKITCATALOG);
    }

    @AfterTest(alwaysRun = true)
    public void tearDown() {
        DriverManager.shutdownDriver();
    }

    @Test
    public void test_webviewIOS() throws InterruptedException {

        //scroll down to the Web View and click
        Gestures.scroll(driver, Gestures.ScrollDirection.DOWN, 0.75);
        WebElement webView = driver.findElement(AppiumBy.accessibilityId("Web View"));
        webView.click();

        //Thread.sleep(2500);

        //Get Set of all available contexts
        Set<String> contextHandles = ((IOSDriver)driver).getContextHandles();
        contextHandles.forEach(System.out::println);

        //switch to webview context i.e. at the first index in array
        ((IOSDriver) driver).context(contextHandles.toArray()[1].toString());

        WebElement heading = driver.findElement(By.xpath("//body//h1"));
        Assert.assertEquals(heading.getText(), "This is HTML content inside a WKWebView.");

        //switch back to the native view
        ((IOSDriver) driver).context("NATIVE_APP");

        driver.findElement(AppiumBy.iOSClassChain("**/XCUIElementTypeButton[`label == \"UIKitCatalog\"`]")).click();

        Assert.assertTrue(webView.isDisplayed());
    }
}
