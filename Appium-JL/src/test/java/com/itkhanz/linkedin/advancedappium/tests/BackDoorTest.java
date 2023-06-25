package com.itkhanz.linkedin.advancedappium.tests;

import com.google.common.collect.ImmutableMap;
import com.itkhanz.base.DriverManager;
import com.itkhanz.enums.Platform;
import com.itkhanz.utils.PropertyUtils;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.Arrays;
import java.util.Map;

import static com.itkhanz.constants.Paths.APPS_DIR;
/*
    Appium helps to directly call the methods in application source code
    Since this strategy is not available to the users so we call it backdoor into application
    Such methods can help set up the app state
    The Appium Espresso driver can trigger these methods
    You need to know all the info about the internal method e.g. name, where it is find, what kinds of argument it takes
    Pros
        No extra dev work
        Total app access
        instant
    Cons
        Android only
        Espresso only
    Important!
        Server must be started with the Espresso driver
        Initialize the driver with automationName capability set to Espresso or use EspressoOptions
        If installed the app previously with UIAutomator2, then uninstall the app first then run the server with espresso and install the app again
        adb uninstall io.appium.uiautomator2.server
        adb uninstall io.appium.uiautomator2.server.test
        adb uninstall io.appium.settings
    Documentation:
        https://appium.readthedocs.io/en/latest/en/commands/mobile-command/
        https://www.headspin.io/blog/calling-methods-inside-your-app-from-appium
        https://medium.com/bumble-tech/white-box-testing-with-appium-espresso-driver-f51cda81c1bb
        https://www.youtube.com/watch?v=ap4Zp6X-sWc
        https://github.com/rajdeepv/appium_backdoors/tree/master
     */
public class BackDoorTest extends BaseTest {

    //This methods is disabled as the raiseToast method does not exist any more in the appplication class of TheApp, in its MainApplication.java
    //https://github.com/appium-pro/TheApp/blob/main/android/app/src/main/java/com/appiumpro/the_app/MainApplication.java
    @Test(enabled = false)
    public void testBackdoorToast() throws InterruptedException {
        Map<String, String> appProps = ImmutableMap.of(
                "appUrl", APPS_DIR + PropertyUtils.getAppProp("android_theApp_name")
        ) ;

        AppiumDriver driver = DriverManager.initializeDriver(Platform.ANDROIDESPRESSO, appProps);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        /*//Internal Application method that exists inside main application class
        //It creates a simple temporary notice that shows up on screen
        //This code can be read from the application source code
        public void raiseToast(String message) {
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        }*/

        //a Map defining a complex object specifying the name and details of the in-app method to call
        //This Map is turned into a JSON object and interpreted by the Appium server.
        ImmutableMap<String, Object> scriptArgs = ImmutableMap.of(
                "target", "application",
                "methods", Arrays.asList(ImmutableMap.of(
                        "name", "raiseToast",
                        "args", Arrays.asList(ImmutableMap.of(
                                "value", "Hello from the test script!",
                                "type", "String"
                        ))
                ))
        );

        //Invoke arbitrary methods defined in Android app.
        // The methods must be public. target is activity, application or element.
        // methods are methods you would like to invoke.
        // elementId is mandatory if target is element
        driver.executeScript("mobile: backdoor", scriptArgs);
        Thread.sleep(3000);
    }

    @Test
    public void testRating() throws InterruptedException {
        Map<String, String> appProps = ImmutableMap.of(
                "appUrl", APPS_DIR + PropertyUtils.getAppProp("android_apiDemos_name")
        ) ;

        AppiumDriver driver = DriverManager.initializeDriver(Platform.ANDROID, appProps);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        driver.findElement(AppiumBy.accessibilityId("Views")).click();

        WebElement ratingBarView =  driver.findElement(AppiumBy.androidUIAutomator(
                "new UiScrollable(scrollable(true).instance(0))" +
                        ".scrollIntoView(textMatches(\"Rating Bar\").instance(0))")
        );
        ratingBarView.click();


        WebElement ratingBar = driver.findElement(AppiumBy.id("io.appium.android.apis:id/ratingbar2"));

        //click to give rating of 4.5
        //Since there is no unique locator for the stars in UI therefore we only can automate this with coordinates
        //This will be problematic in case of orientation or screen size change
        driver.executeScript("mobile: clickGesture", ImmutableMap.of(
                "x", 590,
                "y", 514,
                "duration", 200
        ));

        WebElement ratingResult = wait.until(ExpectedConditions.presenceOfElementLocated(AppiumBy.id("io.appium.android.apis:id/rating")));
        Assert.assertEquals(ratingResult.getText(), "Rating: 4.5/5");
        Thread.sleep(3000);
    }



}
