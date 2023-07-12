package com.itkhanz.tests.todoapp.chap10;

import com.itkhanz.utils.JsonUtils;
import com.itkhanz.utils.PropertyUtils;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.options.XCUITestOptions;
import org.testng.annotations.DataProvider;

import java.net.MalformedURLException;
import java.net.URL;

import static com.itkhanz.constants.Paths.APPS_DIR;
import static com.itkhanz.constants.Paths.TEST_DATA_DIR;

public class ParallelBaseTest {
    private static ThreadLocal<AppiumDriver> driver = new ThreadLocal<AppiumDriver>();

    private static void setDriver(AppiumDriver dr) { driver.set(dr); }

    protected AppiumDriver getDriver() {
        return driver.get();
    }


    /**
     * Use this method to initialize appium driver session for parallel android  tests
     * provide custom device udid, system port for uiautomator2 driver, and different appium server port
     * @param port appium server port
     * @param udid the device id
     * @param systemPort If you are using appium-uiautomator2-driver, set a unique system port for each parallel session.
     * Docs: https://github.com/appium/appium/blob/1.x/docs/en/advanced-concepts/parallel-tests.md#parallel-android-tests
     */
    protected static void android_Setup(String port, String udid, int systemPort) {
        UiAutomator2Options options = new UiAutomator2Options()
                .setPlatformName("Android")
                .setAutomationName("UiAutomator2")
                .setUdid(udid)
                .setApp(APPS_DIR + PropertyUtils.getAppProp("android_todo_name"))
                .setAutoGrantPermissions(true)
                .setSystemPort(systemPort)  //The number of the port on the host machine used for the UiAutomator2 server.
                ;
        try {
            AppiumDriver dr;
            dr = new AndroidDriver(new URL(PropertyUtils.getDeviceProp("APPIUM_SERVER_URL") + ":" + port), options);
            setDriver(dr);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to create web driver session");
        }
    }

    /**
     * Use this method to setup ios device with custom port and device UDID.
     * Used to execute tests in parallel on multiple devices.
     * @param port appium server port, default 4723
     * @param udid udid of the device, can be found from xcrun simctl list
     * @param wdaLocalPort port of the WebDriverAgent must be unique for each parallel session, default 8100
     */
    protected static void iOS_Setup(String port, String udid, int wdaLocalPort) {
        XCUITestOptions options = new XCUITestOptions()
                .setPlatformName("iOS")
                .setAutomationName("XCUITest")
                .setUdid(udid)
                .setApp(APPS_DIR + PropertyUtils.getAppProp("ios_dailycheck_name"))
                .setAutoAcceptAlerts(true)
                .setWdaLocalPort(wdaLocalPort)  //default port is 8100
                //.usePrebuiltWda()
                //.setDerivedDataPath("/Users/ibkh/Library/Developer/Xcode/DerivedData/WebDriverAgent-cmloeqhuqbaydofbwmkqjkvbyjnx")
                ;
        try {
            AppiumDriver dr;
            dr = new IOSDriver(new URL(PropertyUtils.getDeviceProp("APPIUM_SERVER_URL") + ":" + port), options);
            setDriver(dr);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to create web driver session");
        }
    }

    protected void tearDown() {
        if(getDriver() != null) {
            try {
                getDriver().quit();
                driver.remove();
            } catch (Exception e) {
                throw new RuntimeException("Failed to quit the Driver");
            }
        }
    }
}
