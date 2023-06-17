package com.itkhanz.sauce.tests;

import com.itkhanz.base.AndroidDriverManager;
import com.itkhanz.base.AppiumServiceManager;
import com.itkhanz.sauce.pages.CartCheckoutPage;
import com.itkhanz.sauce.pages.HomePage;
import com.itkhanz.sauce.pages.LoginPage;
import com.itkhanz.sauce.pages.ProductDetailPage;
import com.itkhanz.utils.FingerGestureUtils;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static java.time.Duration.ofSeconds;
import static org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable;
import static org.testng.Assert.assertEquals;

public class AndroidTest {
    private CartCheckoutPage<AndroidDriver>   cartPage;
    private AndroidDriver                     driver;
    private FingerGestureUtils<AndroidDriver> fingerGesture;
    private HomePage<AndroidDriver>           homePage;
    private ProductDetailPage<AndroidDriver>  productPage;
    private AppiumDriverLocalService service;

    @BeforeClass
    public void setupClass () {
        this.service = AppiumServiceManager.composeService ()
                .composed ()
                .buildService ();
        this.service.start ();
        this.driver = AndroidDriverManager.createDriver ()
                .waitActivity ("com.swaglabsmobileapp.MainActivity")
                .appName ("sauce-demo")
                .service (this.service)
                .create ()
                .getDriver ();
        this.driver.manage ()
                .timeouts ()
                .implicitlyWait (ofSeconds (5));

        this.fingerGesture = new FingerGestureUtils<>(this.driver);

        this.homePage = new HomePage<>(this.driver);
        this.productPage = new ProductDetailPage<>(this.driver);
        this.cartPage = new CartCheckoutPage<>(this.driver);

        final var loginPage = new LoginPage<>(this.driver);
        loginPage.login ("standard_user", "secret_sauce");
    }

    @AfterClass(alwaysRun = true)
    public void tearDownClass () {
        this.driver.quit ();
        this.service.stop ();
    }

    @Test
    public void testAddToCart () {
        final var productItem = this.homePage.productItem ("Sauce Labs Backpack");
        this.homePage.getWait ()
                .until (elementToBeClickable (productItem.dragHandle ()));
        this.fingerGesture.dragTo (productItem.dragHandle (), this.homePage.cartDropZone ());

        assertEquals (this.homePage.cartCount (), 1);
    }
}
