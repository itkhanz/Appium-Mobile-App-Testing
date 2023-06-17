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

import static com.google.common.truth.Truth.assertThat;
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

    /*
    Perfoms tap, drag and drop gestures
     */
    @Test
    public void testAddToCart () {
        final var productItem = this.homePage.productItem ("Sauce Labs Backpack");
        this.homePage.getWait ()
                .until (elementToBeClickable (productItem.dragHandle ()));
        this.fingerGesture.dragTo (productItem.dragHandle (), this.homePage.cartDropZone ());

        assertThat(this.homePage.cartCount()).isEqualTo(1);
        //assertEquals (this.homePage.cartCount (), 1);
    }

    @Test (dependsOnMethods = "testAddToCart")
    public void testProductDetailsPage () {
        this.fingerGesture.tap (this.homePage.productItem ("Sauce Labs Backpack")
                .getTitle ());

        this.fingerGesture.zoomIn (this.productPage.productImage (), 75);
        this.fingerGesture.zoomOut (this.productPage.productImage (), 75);

        assertThat(this.productPage.productName()).isEqualTo("Sauce Labs Backpack");
    }

    /*
    performs vertical swipe in both directions, left swipe to delete cart item
     */
    @Test (dependsOnMethods = "testProductDetailsPage")
    public void testCartDeleteOption () {
        this.fingerGesture.swipe (FingerGestureUtils.Direction.UP, 75);
        this.fingerGesture.swipe (FingerGestureUtils.Direction.DOWN, 75);
        this.fingerGesture.tap (this.homePage.cart ());

        final var cartItem = this.cartPage.cartItem (0);
        this.fingerGesture.swipe (FingerGestureUtils.Direction.LEFT, cartItem.description (), 75);

        this.fingerGesture.tap (this.cartPage.delete ());

        this.fingerGesture.tap (this.cartPage.continueShopping ());

        assertThat(this.homePage.cartCount()).isEqualTo(0);
    }


}
