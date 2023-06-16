package com.itkhanz.wdio.tests;

import com.itkhanz.base.AppiumServiceManager;
import com.itkhanz.base.IOSDriverManager;
import com.itkhanz.utils.FingerGestureUtils;
import com.itkhanz.wdio.pages.HomePage;
import com.itkhanz.wdio.pages.SwipePage;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.HashMap;

import static com.google.common.truth.Truth.assertThat;
import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated;

public class IOSSwipeTest {
    private IOSDriver driver;
    private FingerGestureUtils<IOSDriver> fingerGesture;
    private AppiumDriverLocalService service;
    private WebDriverWait wait;

    @BeforeTest(alwaysRun = true)
    public void setupTest () {
        this.service = AppiumServiceManager.composeService ()
                .port (4724)
                .driverName ("xcuitest")
                .composed ()
                .buildService ();
        this.service.start ();
        this.driver = IOSDriverManager.createDriver ()
                .service (this.service)
                .appName ("wdio-demo")
                .create ()
                .getDriver ();
        this.wait = new WebDriverWait (this.driver, Duration.ofSeconds (10));
        this.fingerGesture = new FingerGestureUtils<> (this.driver);
    }

    @AfterTest(alwaysRun = true)
    public void tearDownTest () {
        this.driver.quit ();
        this.service.stop ();
    }

    //performs the swipe till element using executeScript ("mobile: scrollToElement", args);
    @Test
    public void testSwipeTillElementUsingScript () {
        final var homePage = new HomePage();
        final var swipePage = new SwipePage();

        this.fingerGesture.tap (this.wait.until (visibilityOfElementLocated (homePage.getSwipeTab ())));

        final var logo = this.driver.findElement (swipePage.getPlainLogo ());

        final var args = new HashMap<String, Object>();
        args.put ("elementId", ((RemoteWebElement) logo).getId ());
        this.driver.executeScript ("mobile: scrollToElement", args);

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
