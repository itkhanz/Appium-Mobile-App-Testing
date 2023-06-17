package com.itkhanz.wdio.tests;

import com.itkhanz.base.AndroidDriverManager;
import com.itkhanz.base.AppiumServiceManager;
import com.itkhanz.utils.FingerGestureUtils;
import com.itkhanz.wdio.pages.HomePage;
import com.itkhanz.wdio.pages.SwipePage;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.HashMap;

import static com.google.common.truth.Truth.assertThat;
import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated;

public class AndroidSwipeTest {
    private AndroidDriver driver;
    private FingerGestureUtils<AndroidDriver> fingerGesture;
    private AppiumDriverLocalService service;
    private WebDriverWait wait;

    @BeforeTest(alwaysRun = true)
    public void setupTest() {
        //Builds the AppiumDriverLocalService starts the appium server
        this.service = AppiumServiceManager.composeService()
                .composed()
                .buildService();
        this.service.start();

        //creates an instance of AndroidDriverManager and launches the app
        this.driver = AndroidDriverManager.createDriver()
                .waitActivity("com.wdiodemoapp.MainActivity")
                .appName("wdio-demo")
                .service(this.service)
                .create().getDriver();

        //Instantiates the WebDriverWait
        this.wait = new WebDriverWait(this.driver, Duration.ofSeconds(10));

        //Instantiates the FingerGestures utility class
        this.fingerGesture = new FingerGestureUtils<>(this.driver);
    }

    @AfterTest(alwaysRun = true)
    public void tearDownTest() {
        this.driver.quit();
        this.service.stop();
    }

    //Performs swipe till element gesture using executeScript("mobile: scroll",args);
    @Test
    public void testSwipeTillElementUsingScript(){
        final var homePage = new HomePage();
        final var swipePage = new SwipePage();

        this.fingerGesture.tap(this.wait.until(visibilityOfElementLocated(homePage.getSwipeTab())));

        final var scrollView = this.wait.until(visibilityOfElementLocated(swipePage.getScrollView()));

        //We pass the id of the scroll element/screen and the target element strategy and selector to executeScript()
        final var args = new HashMap<String, Object>();
        args.put("elementId", ((RemoteWebElement) scrollView).getId());
        args.put("strategy", "accessibility id");
        args.put("selector", "WebdriverIO logo");
        this.driver.executeScript("mobile: scroll",args);

        final var logo = this.wait.until(visibilityOfElementLocated(swipePage.getPlainLogo()));
        assertThat(logo.isDisplayed()).isTrue();
    }

    //Performs swipe till element gesture using UiAutomator UiScrollable Class (only for Android)
    @Test
    public void testSwipeTillElementUsingSelector () {
        final var homePage = new HomePage ();
        final var swipePage = new SwipePage ();

        this.fingerGesture.tap (this.wait.until (visibilityOfElementLocated (homePage.getSwipeTab ())));

        //automatically scrolls to the element because the locator uses UiScrollable scrollIntoView method
        final var logo = this.wait.until (visibilityOfElementLocated (swipePage.getScrolledLogo ()));
        assertThat (logo.isDisplayed ()).isTrue ();
    }

    //performs the swipe till element using W3C Actions with sequence class (applicable to both iOS and Android)
    //see the implementation in FingerGestureUtils
    //Much slower than compared to other two methods because it waits for 10 seconds to perform swipe again
    @Test
    public void testSwipeTillElementUsingSwipe () {
        final var homePage = new HomePage ();
        final var swipePage = new SwipePage ();

        this.fingerGesture.tap (this.wait.until (visibilityOfElementLocated (homePage.getSwipeTab ())));

        final var maxSwipe = 5;
        var swipeCount = 0;
        while (SwipePage.isNotDisplayed (swipePage.getPlainLogo (), this.wait) && swipeCount++ < maxSwipe) {
            this.fingerGesture.swipe (FingerGestureUtils.Direction.UP, 70);
        }
        final var logo = this.wait.until (visibilityOfElementLocated (swipePage.getPlainLogo ()));
        assertThat (logo.isDisplayed ()).isTrue ();
    }
}
