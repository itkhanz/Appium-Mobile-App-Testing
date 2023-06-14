package com.itkhanz.tests;

import com.itkhanz.base.AppFactory;
import com.itkhanz.base.DriverManager;
import com.itkhanz.constants.App;
import com.itkhanz.constants.PlatformOS;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import static com.itkhanz.base.DriverManager.getDriver;
import static com.itkhanz.utils.GestureUtils.*;
import static com.itkhanz.utils.GestureUtils.ScrollDirection.*;
import static com.itkhanz.utils.WaitUtils.waitForElementToBeVisible;


public class GesturesTest{

    //performs long press on the element which opens up new dialog box
    @Test
    public void test_longPress() {
        AppFactory.launchApp(PlatformOS.ANDROID, App.APIDEMOS, null, null);

        getDriver().findElement(AppiumBy.xpath(".//*[@text='Views']")).click();
        getDriver().findElement(AppiumBy.xpath(".//*[@text='Expandable Lists']")).click();
        getDriver().findElement(AppiumBy.xpath(".//*[@text='1. Custom Adapter']")).click();
        WebElement element = getDriver().findElement(AppiumBy.xpath(".//*[@text='People Names']"));
        longPress(element, 3);

        //Alternatively you can also perform longPress using Actions class instead of sequence class
        //new Actions(driver).clickAndHold(element).perform();

        WebElement sampleMenu = getDriver().findElement(AppiumBy.xpath(".//android.widget.TextView[@text='Sample menu']"));
        Assert.assertTrue(sampleMenu.isDisplayed());

        DriverManager.removeDriver(); //not needed when class extends from BaseTest which closes the driver in after hook
    }

    @Test
    public void test_scrollTillElement() throws InterruptedException {
        AppFactory.launchApp(PlatformOS.IOS, App.IOSCONTACTS, null, null);

        //For this test, manually add some contacts just once before running the test for first time so the list becomes longer and scrollable
        By contactLocator = AppiumBy.iOSClassChain("**/XCUIElementTypeCell[`label == \"Olivia\"`]");
        long timeout = 30;
        if (scrollTillElement(contactLocator, DOWN, 0.3, 500, timeout)) {
            WebElement contact = getDriver().findElement(contactLocator);
            Assert.assertTrue(contact.isDisplayed());
        } else {
            Assert.fail("Failed to find the element after scrolling for " + timeout + " seconds");
        }
        DriverManager.removeDriver(); //not needed when class extends from BaseTest which closes the driver in after hook
    }

    @Test
    public void test_swipe_ios() {

        AppFactory.launchApp(PlatformOS.IOS, App.WDIOIOS, null, null);  //Launch wdio iOS app

        getDriver().findElement(AppiumBy.accessibilityId("Swipe")).click();

        scroll(DOWN, 0.2, 600);    // scroll down (swipe up)

        scroll(RIGHT, 0.5, 600); //scroll in the horizontal direction to right
        scroll(RIGHT, 0.5, 600); //scroll again in the horizontal direction to right
        WebElement jsFoundation = getDriver().findElement(AppiumBy.accessibilityId("JS.FOUNDATION"));
        Assert.assertTrue(jsFoundation.isDisplayed());

        scroll(LEFT, 0.5, 600); //scroll in the horizontal direction to right
        scroll(LEFT, 0.5, 600); //scroll again in the horizontal direction to right
        WebElement fullyOpenSource = getDriver().findElement(AppiumBy.accessibilityId("FULLY OPEN SOURCE"));
        Assert.assertTrue(fullyOpenSource.isDisplayed());

        scroll(UP, 0.2, 600);    // scroll up (swipe down)

        DriverManager.removeDriver(); //not needed when class extends from BaseTest which closes the driver in after hook
    }

    @Test
    public void test_swipe_android() {

        AppFactory.launchApp(PlatformOS.ANDROID, App.WDIOANDROID, null, null);  //Launch wdio android app

        WebElement swipeMenuBtn = waitForElementToBeVisible(getDriver(), AppiumBy.accessibilityId("Swipe"), 5000);
        swipeMenuBtn.click();

        scroll(DOWN, 0.2, 600);    // scroll down (swipe up)

        scroll(RIGHT, 0.5, 600); //scroll in the horizontal direction to right
        scroll(RIGHT, 0.5, 600); //scroll again in the horizontal direction to right

        scroll(LEFT, 0.5, 600); //scroll in the horizontal direction to right
        scroll(LEFT, 0.5, 600); //scroll again in the horizontal direction to right

        scroll(UP, 0.2, 600);    // scroll up (swipe down)

        DriverManager.removeDriver(); //not needed when class extends from BaseTest which closes the driver in after hook
    }
}
