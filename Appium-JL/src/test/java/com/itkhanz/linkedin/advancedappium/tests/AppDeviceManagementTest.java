package com.itkhanz.linkedin.advancedappium.tests;

import com.google.common.collect.ImmutableMap;
import com.itkhanz.base.DriverManager;
import com.itkhanz.enums.Platform;
import com.itkhanz.utils.ContextUtils;
import com.itkhanz.utils.PropertyUtils;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.GsmCallActions;
import io.appium.java_client.ios.IOSDriver;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.ScreenOrientation;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.Map;

import static com.itkhanz.constants.Paths.APPS_DIR;

public class AppDeviceManagementTest extends BaseTest {

    @Test
    public void testHybridApp() throws InterruptedException {
        Map<String, String> appProps = ImmutableMap.of(
                "appUrl", APPS_DIR + PropertyUtils.getAppProp("android_theApp_name")
        ) ;

        AppiumDriver driver = DriverManager.initializeDriver(Platform.ANDROID, appProps);
        driver.findElement(AppiumBy.accessibilityId("Webview Demo")).click();

        //enter the url in field and press Go button
        driver.findElement(AppiumBy.accessibilityId("urlInput")).sendKeys("https://appiumpro.com");
        driver.findElement(AppiumBy.accessibilityId("navigateBtn")).click();

        //switch to web context
        ContextUtils.switchToWebContext((AndroidDriver) driver);

        Assert.assertEquals(driver.getTitle(), "Appium Pro");

        //switch back again to native context
        ContextUtils.switchToNativeContext((AndroidDriver) driver);

        Assert.assertEquals(((AndroidDriver) driver).getContext(), "NATIVE_APP");
    }

    /*
    Appium Files API:
        Seed your app with the user data
        Verify internally saved app data
        Get photos/media onto the device quickly and reliably
    Limitations:
        On iOS, limited to working with the application container
        On Android, root access only in emulators or rooted device
    Documentation:
    https://appium.readthedocs.io/en/latest/en/commands/device/files/push-file/
    https://appium.readthedocs.io/en/latest/en/commands/device/files/pull-file/
    https://www.seleniumeasy.com/appium-tutorials/upload-file-using-appium-on-device
     */
    @Test
    public void testFilesTransfer() throws IOException {
        Map<String, String> appProps = ImmutableMap.of(
                "appPackage", PropertyUtils.getAppProp("android_photos_appPackage"),
                "appActivity", PropertyUtils.getAppProp("android_photos_appActivity")
        ) ;

        AppiumDriver driver = DriverManager.initializeDriver(Platform.ANDROID, appProps);

        //Run the adb command to find your sdcard directory on android
        //adb shell echo \$EXTERNAL_STORAGE
        String ANDROID_PHOTO_PATH = "/sdcard/Pictures";
        File image = new File("src/test/resources/image.jpeg").getAbsoluteFile();
        String REMOTE_PATH = ANDROID_PHOTO_PATH + "/" + image.getName();

        //put a file on  the device from your computer
        ((AndroidDriver)driver).pushFile(REMOTE_PATH, image);

        //download a file from the device to your test script
        byte[] fileBase64 = ((AndroidDriver) driver).pullFile(REMOTE_PATH); //get an array of bytes representing the downloaded data.

        //write file to the system
        Path localFilePath = Files.write(Paths.get(System.getProperty("user.dir") + File.separator + "image.jpeg"), fileBase64);

        Assert.assertTrue(Files.exists(localFilePath));

    }

    /*
    Rotates the device to landscape and portrait mode and capture screenshot and save to local
    Helpful:
        Orientation-specific functionality
        Window size-dependent element visibility
        Differences in layout or DPI causing issues for visual testing
    Important:
        Screenshot pixel != logical pixels
        Screenshots will consist of 1 pixel for each physical pixel,
        whereas the device screen size is measured in logical pixels, which may be a fraction of the physical pixels. Retina-based iOS devices,
        for example, deliver screenshots that are 3x the dimensions of the logical screen size.
        for iOS, the dimensions of actual screenshot could be 3x the dimensions on console because the simulator is simulating a retina device
    Documentation:
        https://appium.readthedocs.io/en/stable/en/commands/session/orientation/set-orientation/
        https://javadoc.io/doc/io.appium/java-client/latest/index.html
     */
    @Test
    public void testScreenMethods() throws IOException {
        Map<String, String> appProps = ImmutableMap.of(
                "appUrl", APPS_DIR + PropertyUtils.getAppProp("ios_theApp_name")
        ) ;

        AppiumDriver appiumDriver = DriverManager.initializeDriver(Platform.IOS, appProps);
        IOSDriver driver  = (IOSDriver)appiumDriver;

        //get the current orientation
        ScreenOrientation cuOrientation = driver.getOrientation();
        System.out.println(cuOrientation);

        //Rotate to landscape mode if scree is in portrait mode
        if (cuOrientation != ScreenOrientation.LANDSCAPE) {
            driver.rotate(ScreenOrientation.LANDSCAPE);
            Assert.assertEquals(driver.getOrientation().toString(), "LANDSCAPE");

            //dimensions of screen in landscape mode
            Dimension size = driver.manage().window().getSize();
            System.out.println(size);

            //Take the screenshot in landscape mode and save to local
            File screenshot = driver.getScreenshotAs(OutputType.FILE);
            File saveFile = new File(System.getProperty("user.dir") + File.separator + "screen.jpeg");
            FileUtils.copyFile(screenshot, saveFile);

        }

        //Rotate back to portrait mode if screen is in landscape mode
        driver.rotate(ScreenOrientation.PORTRAIT);
        Assert.assertEquals(driver.getOrientation().toString(), "PORTRAIT");
    }


    /*
    Accept and end the incoming phone call to make sure that app does not crash during usage
    Simulating phone calls and sms
     Limited to Android only, and works only on emulators
    Phone Call API:
        Trigger incoming call from certain number
        Allows to accept call, cancel call, put call on hold
        To trigger the phone call on real device, you need to have sim inserted in it and use telephony service
    SMS API:
        Android apps can read SMS messages to use for app functionality, so we can simulate SMS messages
        e.g. some apps may send a security code in SMS as a part of 2-FA.
        Apps can then read incoming message, verify the code, and log the user-in all without requiring the user to manually enter anything
        We cannot read SMS text using Appium. If we need to read the content of message, we have to navigate to the messages app and then read it from  there.
    Make sure to add the autoGrantPermissions capability to the driver before app is launched so the permission popups do not effect tests
     */
    @Test
    public void testPhoneAndSMS() throws InterruptedException {
        Map<String, String> appProps = ImmutableMap.of(
                "appUrl", APPS_DIR + PropertyUtils.getAppProp("android_theApp_name")
        ) ;

        AppiumDriver driver = DriverManager.initializeDriver(Platform.ANDROID, appProps);
        WebDriverWait wait  = new WebDriverWait(driver, Duration.ofSeconds(10));
        final String PHONE_NUMBER = "555123458";    //any dummy/fake phone number string without dashes

        driver.findElement(AppiumBy.accessibilityId("Login Screen")).click();

        //Trigger an incoming call from dummy phone number
        ((AndroidDriver)driver).makeGsmCall(PHONE_NUMBER, GsmCallActions.CALL);
        Thread.sleep(2000); //pause just for effect

        //accept a call
        ((AndroidDriver)driver).makeGsmCall(PHONE_NUMBER, GsmCallActions.ACCEPT);
        WebElement usernameField = driver.findElement(AppiumBy.accessibilityId("username"));
        usernameField.sendKeys("hi");
        Thread.sleep(2000); //pause just for effect

        //cancel or end a call
        ((AndroidDriver)driver).makeGsmCall(PHONE_NUMBER, GsmCallActions.CANCEL);
        Thread.sleep(2000); //pause just for effect

        Assert.assertTrue(usernameField.isDisplayed());

        //navigate back to the home screen of app
        driver.navigate().back();

        //Click on the view to reach the verification screen
        wait.until(ExpectedConditions.presenceOfElementLocated(AppiumBy.accessibilityId("Verify Phone Number"))).click();
        //text label to define that we are waiting for the incoming sms
        wait.until(ExpectedConditions.presenceOfElementLocated(AppiumBy.xpath("//android.widget.TextView[contains(@text, 'Waiting to receive')]")));

        //send the correct code and assert that the verification message is present
        ((AndroidDriver) driver).sendSMS(PHONE_NUMBER, "Your code is 123456");

        //text label contain the string verified that only shows up after the message with correct code is received
        WebElement smsVerified = wait.until(ExpectedConditions.presenceOfElementLocated(AppiumBy.xpath("//android.widget.TextView[contains(@text, 'verified')]")));

        Assert.assertTrue(smsVerified.isDisplayed());
    }
}
