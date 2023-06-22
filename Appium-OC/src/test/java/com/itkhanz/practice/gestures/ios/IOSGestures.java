package com.itkhanz.practice.gestures.ios;

import com.itkhanz.practice.annotations.TestMetaData;
import com.itkhanz.practice.constants.Apps;
import com.itkhanz.practice.constants.Platform;
import com.itkhanz.practice.base.DriverFactory;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebElement;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.ITestNGMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.net.MalformedURLException;
import java.util.*;

/**
 * iOS Gestures documentation
 * https://github.com/appium/appium-xcuitest-driver
 * https://github.com/clarabez/appium-1/blob/master/docs/en/writing-running-appium/ios/ios-xctest-mobile-gestures.md
 * https://appium.github.io/appium-xcuitest-driver/4.30/execute-methods/#mobile-swipe
 * https://developer.apple.com/documentation/xctest/xcuielement
 * https://developer.apple.com/documentation/xctest/xcuicoordinate
 *
 * Note: IOS generates XML for all the elements on the screen even if they are not visible.
 * This is the big advantage of iOS over android which unlike iOS only populates the XML for elements which are currently visible.
 */
public class IOSGestures {

    private static AppiumDriver driver;

    @BeforeTest
    public void setup(ITestContext ctx) throws MalformedURLException {
        //Method method parameter cannot be used for methods annotated with @BeforeTest with method.getName()
        /*Arrays.stream(ctx.getAllTestMethods()).forEach(testMethod -> {

            Method m = testMethod.getConstructorOrMethod().getMethod();
            String className = getClass().getSimpleName();
            String methodName = m.getName();

            String description = m.getAnnotation(Test.class).description();

            String template = "Class '%s'; method; '%s'; description: '%s'";

            System.out.println(String.format(template, className, methodName, description));

        });*/

        //Use @TestMetaData annotation to pass custom platform and app if desired
        //This checks for the presence of @TestMetaData on test methods, and retrieve the platform and app for the test to initialize custom driver session
        Optional<ITestNGMethod> annnotatedTestMethod = Arrays.stream(ctx.getAllTestMethods())
                .filter(testMethod -> testMethod.getConstructorOrMethod().getMethod().getAnnotation(TestMetaData.class) != null)
                .findFirst();
        if (annnotatedTestMethod.isPresent()) {
            TestMetaData testMetaData = annnotatedTestMethod.get().getConstructorOrMethod().getMethod().getAnnotation(TestMetaData.class);
            driver = DriverFactory.initializeDriver(testMetaData.platform(), testMetaData.app());
        } else {
            //default appium session with iOS platform and UIKitCatalog application
            driver = DriverFactory.initializeDriver(Platform.IOS, Apps.UIKITCATALOG);
        }

    }

    @AfterTest
    public void tearDown() {
        //You can use ITestResult result parameter in teardown methods annotated with @After with result.getMethod().getMethodName()
        if (driver!=null) driver.quit();
    }

    /**
     * This gesture performs a simple "swipe" gesture on the particular screen element or on the application element, which is usually the whole screen.
     * https://github.com/clarabez/appium-1/blob/master/docs/en/writing-running-appium/ios/ios-xctest-mobile-gestures.md#mobile-swipe
     */
    @Test
    @TestMetaData(app = Apps.UIKITCATALOG, platform = Platform.IOS)
    public void test_swipe() {
        WebElement element = driver.findElement(AppiumBy.iOSNsPredicateString("type='XCUIElementTypeTable'"));
        Map<String, Object> params = new HashMap<>();
        params.put("direction", "up");
        //params.put("velocity", 2500);
        params.put("elementId", ((RemoteWebElement) element).getId());
        driver.executeScript("mobile: swipe", params);

        WebElement webViewElement = driver.findElement(AppiumBy.iOSClassChain("**/XCUIElementTypeStaticText[`label == \"Web View\"`]"));
        Assert.assertTrue(webViewElement.isDisplayed());
    }

    /**
     * Scrolls the element or the whole screen.
     * Use "mobile: scroll" to emulate precise scrolling in tables or collection views, where it is already known to which element the scrolling should be performed.
     * Arguments define the choosen strategy: either 'name', 'direction', 'predicateString' or 'toVisible' in that order.
     * toVisible: If set to true then asks to scroll to the first visible elementId in the parent container.
     * https://github.com/clarabez/appium-1/blob/master/docs/en/writing-running-appium/ios/ios-xctest-mobile-gestures.md#mobile-scroll
     */
    @Test
    public void test_scroll() {
        //scroll_with_direction();
        //scroll_with_name();
        //scroll_with_predicateString();
        //scroll_with_childElementAccessibilityId();
        //WebElement webViewElement = driver.findElement(AppiumBy.iOSClassChain("**/XCUIElementTypeStaticText[`label == \"Web View\"`]"));
        //Assert.assertTrue(webViewElement.isDisplayed());


        scroll_with_visibility();
        WebElement webViewElement = driver.findElement(AppiumBy.iOSClassChain("**/XCUIElementTypeStaticText[`label == \"Activity Indicators\"`]"));
        Assert.assertTrue(webViewElement.isDisplayed());
    }


    public void scroll_with_direction() {
        //The main difference from swipe call with the same argument is that scroll will try to move the current viewport exactly to the next/previous page
        //(the term "page" means the content, which fits into a single device screen)
        WebElement element = driver.findElement(AppiumBy.iOSNsPredicateString("type='XCUIElementTypeTable'"));
        Map<String, Object> params = new HashMap<>();
        params.put("direction", "down");
        params.put("elementId", ((RemoteWebElement) element).getId());
        driver.executeScript("mobile: scroll", params);
    }

    public void scroll_with_name() {
        //The accessibility id of the child element, to which scrolling is performed. The same result can be achieved by setting predicateString argument to 'name == accessibilityId'.
        // Has no effect if elementId is not a container
        WebElement element = driver.findElement(AppiumBy.iOSNsPredicateString("type='XCUIElementTypeTable'"));
        Map<String, Object> params = new HashMap<>();
        params.put("name", "Web View");
        params.put("elementId", ((RemoteWebElement) element).getId());
        driver.executeScript("mobile: scroll", params);
    }

    public void scroll_with_predicateString() {
        //The NSPredicate locator of the child element, to which the scrolling should be performed. Has no effect if elementId is not a container
        WebElement element = driver.findElement(AppiumBy.iOSNsPredicateString("type='XCUIElementTypeTable'"));
        Map<String, Object> params = new HashMap<>();
        params.put("predicateString", "label == 'Web View'");
        params.put("elementId", ((RemoteWebElement) element).getId());
        driver.executeScript("mobile: scroll", params);
    }

    public void scroll_with_visibility() {
        //toVisible: If set to true then asks to scroll to the first visible elementId in the parent container. Has no effect if elementId is not set
        Map<String, Object> params = new HashMap<>();
        params.put("direction", "down");
        driver.executeScript("mobile: scroll", params);

        //Here we are not specifying the direction of the scroll, XCTest will auto find the direction in which element is visible
        WebElement childElement = driver.findElement(AppiumBy.accessibilityId("Activity Indicators"));
        Map<String, Object> paramz = new HashMap<>();
        paramz.put("elementId", ((RemoteWebElement) childElement).getId());
        paramz.put("toVisible", true);
        driver.executeScript("mobile: scroll", paramz);
    }

    public void scroll_with_childElementAccessibilityId() {
        //If the accessibility of child element is known, then the parent element is not needed. This command will automatically scroll to the child element.
        //WebElement parentElement = driver.findElement(AppiumBy.iOSNsPredicateString("type='XCUIElementTypeTable'"));
        WebElement childElement = driver.findElement(AppiumBy.accessibilityId("Web View"));
        Map<String, Object> params = new HashMap<>();
        params.put("toVisible", true);
        params.put("elementId", ((RemoteWebElement) childElement).getId());
        driver.executeScript("mobile: scroll", params);
    }

    /**
     * Performs pinch gesture on the given element or on the application element.
     * https://github.com/clarabez/appium-1/blob/master/docs/en/writing-running-appium/ios/ios-xctest-mobile-gestures.md#mobile-pinch
     */
    @Test
    @TestMetaData(app = Apps.IOSMAPS, platform = Platform.IOS)
    public void test_pinch() {
        //we will use the iOS Maps (karte) application for this gesture
        //scale: Pinch scale of type float. Use a scale between 0 and 1 to "pinch close" or zoom out and a scale greater than 1 to "pinch open" or zoom in.
        //velocity must be less than zero when scale is less than 1

        //If on opening the app, prompt appears, then just click continue
        //driver.findElement(AppiumBy.iOSClassChain("**/XCUIElementTypeButton[`label == \"Continue\"`]")).click();

        //Zoom In
        Map<String, Object> params1 = new HashMap<>();
        params1.put("scale", 10);
        params1.put("velocity", 2.2);
        driver.executeScript("mobile: pinch", params1);

        //If you don't provide the elementID, then entire screen is used for zooming out/in
        //Zoom Out
        WebElement element = driver.findElement(AppiumBy.iOSClassChain("**/XCUIElementTypeOther[`name == \"OverlayView\"`][1]"));
        Map<String, Object> params2 = new HashMap<>();
        params2.put("scale", 0.5);
        params2.put("velocity", -2.2);
        params2.put("elementId", ((RemoteWebElement) element).getId());
        driver.executeScript("mobile: pinch", params2);
    }

    /**
     * Performs long press gesture on the given element or on the screen.
     * https://github.com/clarabez/appium-1/blob/master/docs/en/writing-running-appium/ios/ios-xctest-mobile-gestures.md#mobile-touchandhold
     */
    @Test
    public void test_longClick() {
        driver.findElement(AppiumBy.accessibilityId("Steppers")).click();

        WebElement elementIncrement = driver.findElement(AppiumBy.iOSClassChain("**/XCUIElementTypeButton[`name == \"Increment\"`][1]"));
        Map<String, Object> params1 = new HashMap<>();
        params1.put("duration", 10);
        params1.put("elementId", ((RemoteWebElement) elementIncrement).getId());
        driver.executeScript("mobile: touchAndHold", params1);

        WebElement elementDecrement = driver.findElement(AppiumBy.iOSClassChain("**/XCUIElementTypeButton[`name == \"Decrement\"`][1]"));
        Map<String, Object> params2 = new HashMap<>();
        params2.put("duration", 10);
        params2.put("elementId", ((RemoteWebElement) elementDecrement).getId());
        driver.executeScript("mobile: touchAndHold", params2);
    }

    /**
     * Performs tap gesture by coordinates on the given element or on the screen.
     * x and y coordinates are offset from the top-left of the reference elementID
     * https://github.com/clarabez/appium-1/blob/master/docs/en/writing-running-appium/ios/ios-xctest-mobile-gestures.md#mobile-tap
     */
    @Test
    public void test_tap() {
        WebElement stepperElement = driver.findElement(AppiumBy.accessibilityId("Steppers"));

        Map<String, Object> params = new HashMap<>();
        params.put("x", 0);
        params.put("y", 0);
        params.put("elementId", ((RemoteWebElement) stepperElement).getId());
        driver.executeScript("mobile: tap", params);
    }

    /**
     * Performs selection of the next or previous picker wheel value. This might be useful if these values are populated dynamically.
     * Note: Appium don't have a special backend command for Android picker wheel
     * https://github.com/clarabez/appium-1/blob/master/docs/en/writing-running-appium/ios/ios-xctest-mobile-gestures.md#mobile-selectpickerwheelvalue
     */
    @Test
    public void test_selectPicker() {
        driver.findElement(AppiumBy.accessibilityId("Picker View")).click();
        boolean flag=false;
        while(!flag) {
            WebElement redPickerWheel = driver.findElement(AppiumBy.iOSNsPredicateString("label = 'Red color component value'"));
            Map<String, Object> params = new HashMap<>();
            params.put("order", "next");
            params.put("offset", 0.15);
            params.put("elementId", ((RemoteWebElement) redPickerWheel).getId());
            driver.executeScript("mobile: selectPickerWheelValue", params);
            if (redPickerWheel.getText().equals("90")) {
                flag=true;
            }
        }
    }

    /**
     *
     */
    @Test
    public void test_slider() {
        driver.findElement(AppiumBy.accessibilityId("Sliders")).click();

        //value of slider should be in range 0 and 1
        WebElement element = driver.findElement(AppiumBy.iOSNsPredicateString("value == \"42 %\""));
        element.sendKeys("0");

        element = driver.findElement(AppiumBy.iOSNsPredicateString("value == \"0 %\""));
        element.sendKeys("1");

    }

}
