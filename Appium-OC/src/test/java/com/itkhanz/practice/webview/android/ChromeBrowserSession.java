package com.itkhanz.practice.webview.android;

import com.itkhanz.practice.constants.Browsers;
import com.itkhanz.practice.base.BrowserFactory;
import com.itkhanz.practice.base.DriverManager;
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

import static org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable;

/*
Start the appium server with uiautomator2 driver (and better also with wait plugin)
appium --use-drivers=uiautomator2 --use-plugins=element-wait
Launch Hybrid app and Launch Chrome browser and navigate to the webpage  chrome://inspect#devices
ChromeDriver needs to be installed (a default version comes with Appium) and configured for automating the specific version of Chrome available on the device.
Use chromedriverExecutable capability to provide path to the ChromeDriver version.
Alternatively there is no need to manage ChromeDriver versions!
Appium can automatically download the compatible ChromeDriver and use it for automating the browser.
appium --allow-insecure chromedriver_autodownload
use chromedriverExecutableDir capability to provide path for appium to download and copy the browser executables

Documentation:-
https://github.com/appium/appium/tree/1.x/docs/en/writing-running-appium/web
https://appium.readthedocs.io/en/latest/en/writing-running-appium/web/hybrid/
*/

public class ChromeBrowserSession {
    private AppiumDriver driver;
    private WebDriverWait wait;

    @BeforeTest
    public void setup() throws MalformedURLException {
        //Use browserName capability with value Chrome or Chromium
        driver = BrowserFactory.initializeDriver(Browsers.CHROME);
        //driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10)); //use Explicit Waits or appium wait plugin
        wait = new WebDriverWait(driver, Duration.ofSeconds(30));
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
        Assert.assertEquals(driver.getTitle().trim(), "Electric Cars, Solar & Clean Energy | Tesla");
    }

    /*
     This is just a demo for automating the mobile browser with appium
     Highly recommended to execute the browser automation on real device as emulator is relly slow
     */
    @Test
    public void test_saucedemoE2E() throws InterruptedException {
        driver.get("https://www.saucedemo.com/");

        //Login
        driver.findElement(By.xpath("//input[@id='user-name']")).sendKeys("standard_user");
        driver.findElement(By.xpath("//input[@id='password']")).sendKeys("secret_sauce");
        driver.findElement(By.xpath("//input[@id='login-button']")).click();

        //Add Product to cart
        WebElement onesieAddtoCartBtn = driver.findElement(By.xpath("//button[@id='add-to-cart-sauce-labs-onesie']"));
        driver.executeScript("arguments[0].scrollIntoView(true);", onesieAddtoCartBtn);
        onesieAddtoCartBtn.click();

        //Open Cart
        WebElement cart = driver.findElement(By.xpath("//div[@id='shopping_cart_container']"));
        driver.executeScript("arguments[0].scrollIntoView(true);", cart);
        cart.click();
        //Proceed for checkout
        driver.findElement(By.xpath("//button[@id='checkout']")).click();

        //Fill personal details and continue
        driver.findElement(By.xpath("//input[@id='first-name']")).sendKeys("test");
        driver.findElement(By.xpath("//input[@id='last-name']")).sendKeys("user");
        driver.findElement(By.xpath("//input[@id='postal-code']")).sendKeys("12345");
        driver.findElement(By.xpath("//input[@id='continue']")).click();

        //Validate order summary
        WebElement total = driver.findElement(By.xpath("//div[contains(@class, 'summary_total_label')]"));
        float totalBill = Float.parseFloat(total.getText().split("Total: \\$")[1]);
        Assert.assertEquals(totalBill, totalBill);

        //Finish order
        driver.findElement(By.xpath("//button[@id='finish']")).click();

        //Validate order status
        WebElement orderStatus = driver.findElement(By.xpath("//div[@id='header_container']//span[@class='title']"));
        Assert.assertEquals(orderStatus.getText(), "Checkout: Complete!");

        //Logout
        driver.findElement(By.xpath("//button[@id='react-burger-menu-btn']")).click();
        driver.findElement(By.xpath("//a[@id='logout_sidebar_link']")).click();

        //Validation for successful logout
        WebElement loginLogo = driver.findElement(By.xpath("//div[@class='login_logo']"));
        Assert.assertTrue(loginLogo.isEnabled());
    }
}
