package com.itkhanz.linkedin.advancedappium.tests;

import com.google.common.collect.ImmutableMap;
import com.itkhanz.base.DriverManager;
import com.itkhanz.enums.Platform;
import com.itkhanz.utils.PropertyUtils;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.Map;

import static com.itkhanz.constants.Paths.APPS_DIR;

public class AppStateTest extends BaseTest {
    private final String USERNAME = "alice";
    private final String PASSWORD = "mypassword";
    protected AppiumDriver driver;
    protected WebDriverWait wait;

    //This BeforeMethod is placed in child class because not all test classes have to start the driver session with android TheApp
    //Otherwise if all the test classes have to be initialized for same app then it may be moved along with driver and wait to parent BaseTest class
    @BeforeMethod
    public void setup() {
        Map<String, String> appProps = ImmutableMap.of(
                "appUrl", APPS_DIR + PropertyUtils.getAppProp("android_theApp_name")
        ) ;

        driver = DriverManager.initializeDriver(Platform.ANDROID, appProps);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @Test
    public void testLoginNormally() {
        Instant start = Instant.now();
        WebElement screen = wait.until(ExpectedConditions.presenceOfElementLocated(AppiumBy.accessibilityId("Login Screen")));
        screen.click();

        WebElement username = wait.until(ExpectedConditions.presenceOfElementLocated(AppiumBy.accessibilityId("username")));
        username.sendKeys(USERNAME);

        WebElement password = wait.until(ExpectedConditions.presenceOfElementLocated(AppiumBy.accessibilityId("password")));
        password.sendKeys(PASSWORD);

        WebElement login = wait.until(ExpectedConditions.presenceOfElementLocated(AppiumBy.accessibilityId("loginBtn")));
        login.click();

        WebElement loginText = wait.until(ExpectedConditions.presenceOfElementLocated(AppiumBy.xpath("//android.widget.TextView[contains(@text, 'You are logged in')]")));

        Assert.assertTrue(loginText.getText().contains("alice"));
        Instant end = Instant.now();
        System.out.println(Thread.currentThread().getStackTrace()[1].getMethodName() +  " script took: " + Duration.between(start, end).toMillis() + " milliseconds to execute");
    }


    /*
    Deep Links
        Custom URL scheme registered with mobile OS
        These url schemes can be registered for both iOS and android as part of app configuration or manifest file
        Mobile OS opens your app, passing link content
        Link can be parsed using arbitrary format
        Action taken based on link parsing
    Helpful
        In establishing the application state quickly
    Cons
        Devs need to register the scheme and need to build API to trigger actions associated with deeplink
    Examnple
        theapp://login<username>/<password>
        theapp is the Custom scheme
        login<username>/<password> is the Path
     */
    @Test
    public void testLoginWithDeepLink() {
        Instant start = Instant.now();
        //Bypass the login process of UI with deeplink
        driver.get("theapp://login/alice/mypassword");

        WebElement loginText = wait.until(ExpectedConditions.presenceOfElementLocated(AppiumBy.xpath("//android.widget.TextView[contains(@text, 'You are logged in')]")));

        Assert.assertTrue(loginText.getText().contains("alice"));
        Instant end = Instant.now();
        System.out.println(Thread.currentThread().getStackTrace()[1].getMethodName() +  " script took: " + Duration.between(start, end).toMillis() + " milliseconds to execute");
    }



}
