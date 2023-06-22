package com.itkhanz.practice.drivercommands.android;

import com.itkhanz.practice.constants.Apps;
import com.itkhanz.practice.constants.Platform;
import com.itkhanz.practice.base.DriverFactory;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.net.MalformedURLException;
import java.time.Duration;

public class AppLockUnlock {
    private static AppiumDriver driver;

    @BeforeTest
    public void setup() throws MalformedURLException {
        driver = DriverFactory.initializeDriver(Platform.ANDROID, Apps.APIDEMOS);
    }

    @AfterTest(alwaysRun = true)
    public void tearDown() {
        DriverFactory.shutdownDriver();
    }

    /*
    lockDevice(Duration duration) locks the dveice for duration and then unlocks it
     */
    @Test
    public void test_deviceLockForDuration() throws InterruptedException {
        /*DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability(MobileCapabilityType.PLATFORM_NAME, "Android");
        caps.setCapability(MobileCapabilityType.AUTOMATION_NAME, "UiAutomator2");
        caps.setCapability(MobileCapabilityType.UDID, "emulator-5554");

        caps.setCapability(MobileCapabilityType.UDID, "emulator-5554");
        String appUrl = System.getProperty("user.dir") + File.separator + "src" + File.separator + "main"
                + File.separator + "resources" + File.separator + "apps" + File.separator + "ApiDemos-debug.apk";
        caps.setCapability(MobileCapabilityType.APP, appUrl);

        URL url = new URL("http://0.0.0.0:4723/");

        driver = new AndroidDriver(url, caps);*/

       ((AndroidDriver) driver).lockDevice(Duration.ofMillis(5000));
        Thread.sleep(10000);

        //Not working for me: Appium fails to unlock the device after set duration automatically
        //works after reinstalling the uiautomator2 driver
        Assert.assertFalse(((AndroidDriver) driver).isDeviceLocked());
    }

    /*
    lockDevice() locks the device permanently until you call unlockDevice()
     */
    @Test
    public void test_deviceLockAndUnlock() throws InterruptedException {
        ((AndroidDriver) driver).lockDevice();
        Assert.assertTrue(((AndroidDriver) driver).isDeviceLocked());
        ((AndroidDriver) driver).unlockDevice();
    }

    /*
    If the app is locked with a protection, then we need to provide 2 additional options to the driver during initialization
    appium:unlockType Set one of the possible types of Android lock screens to unlock.
    appium:unlockKey  Allows to set an unlock key. (We treat the pattern pins as the numbers of a phone dial. So in case of Z pattern, the unlockKey would be "1235789")
    Pattern unlocking in Appium is that each dot corresponds to a specific number starting from 1.
    Top row: the three dots will be 1 2 3
    Middle row: the three dots will be 4 5 6
    Last row: the three dots will be 7 8 9
    For setting fingerprint, navigate to the Settings->ecurity -> Pixel Imprint, Click I agree and Next
    Enter the adb -e emu finger touch <finger_id> from command line multiple times till the finge print has been setup successfully in emulator
    Make sure to enable the corresponding unlock security settings in android emulator before running the test
    https://github.com/appium/appium-uiautomator2-driver#device-locking
    https://medium.com/@thaveeshadarshana/biometric-scenario-automation-using-adb-commands-11e453986c8e
    https://qavalidation.com/2018/09/how-to-enable-use-fingerprint-in-android-simulators.html/
    https://stackoverflow.com/questions/35335892/android-m-fingerprint-scanner-on-android-emulator
    https://github.com/appium/appium-android-driver/blob/master/docs/UNLOCK.md
    https://medium.com/codex/appium-how-to-handle-device-lock-and-unlock-actions-for-android-emulators-44b962698e89
     */
    @Test
    public void test_deviceUnlockSecured() throws InterruptedException {
        ((AndroidDriver) driver).lockDevice();
        Assert.assertTrue(((AndroidDriver) driver).isDeviceLocked());
        ((AndroidDriver) driver).unlockDevice();

       // ((AndroidDriver) driver).fingerPrint(1);

        Thread.sleep(5000);
        Assert.assertFalse(((AndroidDriver) driver).isDeviceLocked());

    }
}
