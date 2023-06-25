package com.itkhanz.espresso.tests;

import com.google.common.collect.ImmutableMap;
import com.itkhanz.utils.GestureUtils;
import com.itkhanz.utils.PropertyUtils;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Arrays;

/*
Start the server with espresso driver for these tests
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
public class EspressoBackdoorTest extends EspressoBaseTest {
    @Test
    public void testBackdoorRating() throws InterruptedException {
        //open Views
        driver.findElement(AppiumBy.accessibilityId("Views")).click();

        //scroll to rating bar
        //UIScrollable is native to UIAutomator2
        //https://github.com/appium/appium-espresso-driver#mobile-swipe
        WebElement element = driver.findElement(AppiumBy.id("android:id/list"));
        driver.executeScript("mobile: swipe", ImmutableMap.of(
                "element", ((RemoteWebElement)element).getId(),
                "direction", "up"
        ));

        //open Rating Bar
        wait.until(ExpectedConditions.elementToBeClickable(AppiumBy.accessibilityId("Rating Bar"))).click();

        //Rating Bar at the second position
        WebElement ratingBar = wait.until(ExpectedConditions.presenceOfElementLocated(AppiumBy.id("io.appium.android.apis:id/ratingbar2")));

        //Give a rating of 1.5 using backdoor
        //https://developer.android.com/reference/android/widget/RatingBar#setRating(float)
        ImmutableMap<String, Object> scriptArgs = ImmutableMap.of(
                "target", "element",
                "elementId", ((RemoteWebElement)ratingBar).getId(),
                "methods", Arrays.asList(ImmutableMap.of(
                        "name", "setRating",
                        "args", Arrays.asList(ImmutableMap.of(
                                "value", "1.5",
                                "type", "float"
                        ))
                ))
        );
        driver.executeScript("mobile: backdoor", scriptArgs);

        //validate the rating result
        WebElement ratingResult = wait.until(ExpectedConditions.presenceOfElementLocated(AppiumBy.id("io.appium.android.apis:id/rating")));
        Assert.assertEquals(ratingResult.getText(), "Rating: 1.5/5");
    }


    @Test
    public void testScrollToBottom() {
        //open Views
        driver.findElement(AppiumBy.accessibilityId("Views")).click();

        WebElement element = driver.findElement(AppiumBy.id("android:id/list"));
        //Scroll to bottom
        //https://developer.android.com/reference/android/view/View.html#canScrollVertically(int)
        ImmutableMap<String, Object> scriptArgs = ImmutableMap.of(
                "target", "element",
                "elementId", ((RemoteWebElement)element).getId(),
                "methods", Arrays.asList(ImmutableMap.of(
                        "name", "canScrollVertically",
                        "args", Arrays.asList(ImmutableMap.of(
                                "value", "1",
                                "type", "int"
                        ))
                ))
        );

        boolean canScrollMore = (boolean) driver.executeScript("mobile: backdoor", scriptArgs);
        while (canScrollMore) {
            driver.executeScript("mobile: swipe", ImmutableMap.of(
                    "element", ((RemoteWebElement)element).getId(),
                    "direction", "up"
            ));
            canScrollMore = (boolean) driver.executeScript("mobile: backdoor", scriptArgs);
        }

        //validate the rating result
        WebElement webView3 = wait.until(ExpectedConditions.presenceOfElementLocated(AppiumBy.accessibilityId("WebView3")));
        Assert.assertTrue(webView3.isDisplayed());
    }

    @Test
    public void testCountListViewItems() {
        //open Views
        driver.findElement(AppiumBy.accessibilityId("Views")).click();

        WebElement element = driver.findElement(AppiumBy.id("android:id/list"));

        //Count Using Backdoor ViewFunctions
        //ListView.getAdapter().getCount();
        //https://developer.android.com/reference/android/widget/ListView#getAdapter()
        //https://developer.android.com/reference/android/widget/AdapterView#getCount()
        ImmutableMap<String, Object> scriptArgs = ImmutableMap.of(
                "target", "element",
                "elementId", ((RemoteWebElement)element).getId(),
                "methods", Arrays.asList(
                        //Returns the adapter currently in use in this ListView.
                        ImmutableMap.of("name", "getAdapter"),
                        //The number of items owned by the Adapter associated with this AdapterView
                        ImmutableMap.of("name", "getCount")
                )
        );

        long listItemCount = (long) driver.executeScript("mobile: backdoor", scriptArgs);

        Assert.assertEquals(listItemCount, 42);
    }

    //Test is disabled
    //No public method 'raiseToast' is defined on class io.appium.android.apis.ApiDemos or its parents which takes [class java.lang.String] arguments
    @Test (enabled = false)
    public void testBackdoorToast() throws InterruptedException {
        //Display a toast message on screen with given value
        ImmutableMap<String, Object> scriptArgs = ImmutableMap.of(
                "target", "activity",
                "methods", Arrays.asList(
                        ImmutableMap.of(
                                "name", "raiseToast",
                                "args", Arrays.asList(ImmutableMap.of(
                                        "value", "Welcome to Heisenbug \\n This message was sent by automation code to app under test",
                                        "type", "java.lang.String"
                                ))
                        )
                )
        );
        driver.executeScript("mobile: backdoor", scriptArgs);

        Thread.sleep(5000);
    }

    @Test
    public void testUIAutomatorCommands() throws InterruptedException {
        //Allows to execute a limited set of UiAutomator commands to allow out-of-app interactions with accessibility elements.
        //https://github.com/appium/appium-espresso-driver/tree/master#mobile-uiautomator

        //Clicks on element that contains text 'App'
        ImmutableMap<String, Object> scriptArgs = ImmutableMap.of(
                "strategy", "textContains",
                "locator", "App",
                "index", "0",
                "action", "click"
        );
        driver.executeScript("mobile: uiautomator", scriptArgs);

        WebElement actionBar = wait.until(ExpectedConditions.presenceOfElementLocated(AppiumBy.accessibilityId("Action Bar")));
        Assert.assertTrue(actionBar.isDisplayed());
    }

    @Test
    public void testColor() {
        //open Views
        driver.findElement(AppiumBy.accessibilityId("Views")).click();

        wait.until(ExpectedConditions.presenceOfElementLocated(AppiumBy.accessibilityId("Drag and Drop"))).click();

        WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(AppiumBy.xpath("//android.widget.TextView[@resource-id='app:id/drag_explanation']")));

        //Get text color Using Backdoor TextView
        //https://developer.android.com/reference/android/widget/TextView#getCurrentTextColor()
        //getCurrentTextColor() returns the difference between current color and white color value (both in decimal) minus 1
        ImmutableMap<String, Object> scriptArgs = ImmutableMap.of(
                "target", "element",
                "elementId", ((RemoteWebElement)element).getId(),
                "methods", Arrays.asList(
                        //Return the current color selected for normal text.
                        ImmutableMap.of("name", "getCurrentTextColor")
                )
        );

        long textColor = (long) driver.executeScript("mobile: backdoor", scriptArgs);

        //https://stackoverflow.com/a/17487979/7673215
        //https://stackoverflow.com/a/6539929/7673215
        System.out.println("HEX Color: #" + Integer.toHexString((int) textColor));
    }

    @Test
    public void testFlashElement() throws InterruptedException {
        //Highlights the given element in the UI by adding flashing to it
        //https://github.com/appium/appium-espresso-driver/tree/master#mobile-flashelement

        WebElement element = driver.findElement(AppiumBy.accessibilityId("Views"));

        //Flashes the Views element
        ImmutableMap<String, Object> scriptArgs = ImmutableMap.of(
                "element", ((RemoteWebElement)element).getId(),
                "durationMillis", "200",
                "repeatCount", "10"
        );
        driver.executeScript("mobile: flashElement", scriptArgs);
    }

    @Test
    public void testProgressBar() throws InterruptedException {
        //open Views
        driver.findElement(AppiumBy.accessibilityId("Views")).click();


        //scroll to progress bar using W3C Actions
        int scrollCount = 0;
        final int MAX_SCROLL_COUNT = 10;
        while (scrollCount < MAX_SCROLL_COUNT) {
            try {
                wait.until(ExpectedConditions.visibilityOfElementLocated(AppiumBy.accessibilityId("Progress Bar")));
                break;
            } catch (Exception e) {
                GestureUtils.scroll(driver, "DOWN");
            }
            scrollCount++;
        }

        driver.findElement(AppiumBy.accessibilityId("Progress Bar")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(AppiumBy.accessibilityId("1. Incremental"))).click();

        WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(AppiumBy.xpath("//android.widget.ProgressBar[@resource-id='app:id/progress_horizontal']")));

        //Get the progress bar value
        //https://developer.android.com/reference/android/widget/ProgressBar#getProgress()
        ImmutableMap<String, Object> scriptArgs = ImmutableMap.of(
                "target", "element",
                "elementId", ((RemoteWebElement)element).getId(),
                "methods", Arrays.asList(
                        //Get the progress bar's current level of progress. Return 0 when the progress bar is in indeterminate mode.
                        ImmutableMap.of("name", "getProgress")
                )
        );

        long progress = (long) driver.executeScript("mobile: backdoor", scriptArgs);
        Assert.assertEquals(progress, 50);
    }



    /*
    Clicks on the link (415)555-1212 in sub string which has no visible locator
    Clicking on this phone number opens the dialer app with this number pasted on the screen
    Important: Clicking on the link takes us to outside of the App which Espresso does not support, therefore initialize the server and driver with espresso and uiautomator2 both
     */
    @Test
    public void testTouchSubString() throws InterruptedException {

        //Sub string of the link (415)555-1212
        final String SUB_TEXT = "415";
        //open Views
        driver.findElement(AppiumBy.accessibilityId("Text")).click();
        //Open linkify
        driver.findElement(AppiumBy.accessibilityId("Linkify")).click();

        WebElement text1El = driver.findElement(AppiumBy.xpath("//android.widget.TextView[@resource-id='app:id/text1']"));
        String fulltext = text1El.getText();    //Full text inside th element i.e. text1
        int link_index = fulltext.indexOf(SUB_TEXT) + SUB_TEXT.length() / 2;    //Gets the middle index of SUB_TEXT link
        int top_x = text1El.getLocation().x;    //x-coordinate of the text1 (full text)
        int top_y = text1El.getLocation().y;    //y-coordinate of the text1 (full text)

        long left_padding = (long) driver.executeScript("mobile: backdoor", ImmutableMap.of(
                "target", "element",
                "elementId", ((RemoteWebElement)text1El).getId(),
                "methods", Arrays.asList(
                        //Returns the left padding of the view, plus space for the left Drawable if any.
                        ImmutableMap.of("name", "getCompoundPaddingLeft")
        )));

        //x coordinate of word (link)
        long hor = (long) driver.executeScript("mobile: backdoor", ImmutableMap.of(
                        "target", "element",
                        "elementId", ((RemoteWebElement)text1El).getId(),
                        "methods", Arrays.asList(
                                ImmutableMap.of("name", "getLayout"),
                                ImmutableMap.of(
                                        //Get the primary horizontal position for the specified text offset
                                        "name", "getPrimaryHorizontal",
                                        "args", Arrays.asList(
                                                ImmutableMap.of("type", "int","value", link_index)
                                        )
                                    )
                                )
                            )
                        );

        //line number at which word(link) exists
        long line = (long) driver.executeScript("mobile: backdoor", ImmutableMap.of(
                        "target", "element",
                        "elementId", ((RemoteWebElement)text1El).getId(),
                        "methods", Arrays.asList(
                                ImmutableMap.of("name", "getLayout"),
                                ImmutableMap.of(
                                        //Get the line number on which the specified text offset appears
                                        "name", "getLineForOffset",
                                        "args", Arrays.asList(
                                                ImmutableMap.of("type", "int","value", link_index)
                                        )
                                )
                        )
                )
        );

        //y coordinate of word (link)
        long ver = (long) driver.executeScript("mobile: backdoor", ImmutableMap.of(
                        "target", "element",
                        "elementId", ((RemoteWebElement)text1El).getId(),
                        "methods", Arrays.asList(
                                ImmutableMap.of("name", "getLayout"),
                                ImmutableMap.of(
                                        //Return the vertical position of the baseline of the specified line.
                                        "name", "getLineBaseline",
                                        "args", Arrays.asList(
                                                ImmutableMap.of("type", "int","value", line)
                                        )
                                )
                        )
                )
        );

        //Calculate the x and y coordinate of point to tap by adding link coordinate to text coordinate
        int x = (int) (hor + top_x + left_padding);
        int y = (int) (ver + top_y);
        Point tappingPoint = new Point(x, y);

        try {
            GestureUtils.tap(driver, tappingPoint);
        } catch (WebDriverException e) {
            //Espresso is limited to the application sandbox. It is not possible to access any apps except of the one, which is under test.
            //Following error is thrown
            //androidx.test.espresso.NoActivityResumedException: No activities in stage RESUMED. Did you forget to launch the activity. (test.getActivity() or similar)?
            System.out.println(e.getClass().getSimpleName());;
            System.out.println(e.getMessage());;
        }
        Assert.assertEquals(((AndroidDriver)driver).currentActivity(), "com.android.dialer.main.impl.MainActivity");

        //https://discuss.appium.io/t/espresso-driver-noactivityresumedexception/30012
        //((AndroidDriver) driver).activateApp("com.google.android.dialer");
        //WebElement dialer = driver.findElement(AppiumBy.xpath("//android.widget.EditText[@resource-id='com.google.android.dialer:id/digits']"));
        //System.out.println(dialer.getText());
        //((AndroidDriver) driver).activateApp("android_apiDemos_appPackage");
    }




}
