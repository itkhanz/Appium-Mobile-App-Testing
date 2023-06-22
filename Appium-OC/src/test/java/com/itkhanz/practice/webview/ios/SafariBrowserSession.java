package com.itkhanz.practice.webview.ios;

import com.itkhanz.practice.base.BrowserFactory;
import com.itkhanz.practice.base.DriverManager;
import com.itkhanz.practice.constants.Browsers;
import com.itkhanz.practice.utils.Gestures;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.net.MalformedURLException;
import java.time.Duration;

import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated;

/*
1. Launch Hybrid app and navigate to the WebView page OR Launch Safari browser and navigate to the webpage
2. Open desktop Safari
3. Enable “Develop” menu option if not already enabled. Got to Safari -> Preferences… -> Advanced menu and enable “Show Develop menu in menu bar” option
4. Go to Develop Menu -> Select Simulator and then the WebView page/Browser page -> It opens up the Web Inspector
5. Copy the CSS or XPath or any other locator as you would do in Selenium
 */
public class SafariBrowserSession {
    private AppiumDriver driver;
    private WebDriverWait wait;

    @BeforeTest
    public void setup() throws MalformedURLException {
        //Use browserName capability with value Chrome or Chromium
        driver = BrowserFactory.initializeDriver(Browsers.SAFARI);
        //driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10)); //use Explicit Waits or appium wait plugin
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @AfterTest(alwaysRun = true)
    public void tearDown() {
        DriverManager.shutdownDriver();
    }

    /*
    Basic test to validate if the browser session is created
    Use proper waiting strategy or wait-plugin because website loads too slow on emulator
    */
    @Test
    public void test_browserSession() throws InterruptedException {
        driver.get("https://tesla.com");
        Thread.sleep(10000);
        Assert.assertEquals(driver.getTitle().trim(), "Electric Cars, Solar & Clean Energy | Tesla");
    }

    /*
     This is just a demo for automating the mobile browser safari with appium
     */
    @Test
    public void test_saucedemoE2E() {
        driver.get("https://www.saucedemo.com/");

        //Login
        wait.until(visibilityOfElementLocated(By.xpath("//input[@id='user-name']"))).sendKeys("standard_user");
        wait.until(visibilityOfElementLocated(By.xpath("//input[@id='password']"))).sendKeys("secret_sauce");
        wait.until(visibilityOfElementLocated(By.xpath("//input[@id='login-button']"))).click();

        //Reset app state
        wait.until(visibilityOfElementLocated(By.xpath("//button[@id='react-burger-menu-btn']"))).click();
        wait.until(visibilityOfElementLocated(By.xpath("//a[@id='reset_sidebar_link']"))).click();

        //Close the sidebar
        wait.until(visibilityOfElementLocated(By.xpath("//button[@id='react-burger-cross-btn']"))).click();

        //Scroll down to add to cart button
        Gestures.scroll(driver, Gestures.ScrollDirection.DOWN, 0.1);

        //add to cart
        wait.until(visibilityOfElementLocated(By.xpath("//button[@id='add-to-cart-sauce-labs-backpack']"))).click();

        //scroll to the top to add to cart button
        Gestures.scroll(driver, Gestures.ScrollDirection.UP, 0.1);

        //Open Cart
        wait.until(visibilityOfElementLocated(By.xpath("//a[@class='shopping_cart_link']"))).click();

        //Proceed for checkout
        wait.until(visibilityOfElementLocated(By.xpath("//button[@id='checkout']"))).click();

        //Fill personal details and continue
        wait.until(visibilityOfElementLocated(By.xpath("//input[@id='first-name']"))).sendKeys("test");
        wait.until(visibilityOfElementLocated(By.xpath("//input[@id='last-name']"))).sendKeys("user");
        wait.until(visibilityOfElementLocated(By.xpath("//input[@id='postal-code']"))).sendKeys("12345");
        wait.until(visibilityOfElementLocated(By.xpath("//input[@id='continue']"))).click();

        //Validate order summary
        WebElement total = wait.until(visibilityOfElementLocated(By.xpath("//div[contains(@class, 'summary_total_label')]")));
        float totalBill = Float.parseFloat(total.getText().split("Total: \\$")[1]);
        Assert.assertEquals(totalBill, totalBill);

        //Finish order
        wait.until(visibilityOfElementLocated(By.xpath("//button[@id='finish']"))).click();

        //Validate order status
        WebElement orderStatus =  wait.until(visibilityOfElementLocated(By.xpath("//div[@id='header_container']//span[@class='title']")));
        Assert.assertEquals(orderStatus.getText(), "Checkout: Complete!");

        //Logout
        wait.until(visibilityOfElementLocated(By.xpath("//button[@id='react-burger-menu-btn']"))).click();
        wait.until(visibilityOfElementLocated(By.xpath("//a[@id='logout_sidebar_link']"))).click();

        //Validation for successful logout
        WebElement loginLogo =  wait.until(visibilityOfElementLocated(By.xpath("//div[@class='login_logo']")));
        Assert.assertTrue(loginLogo.isEnabled());
    }
}
