package com.itkhanz.tests.todoapp;

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

public class BaseTest {
    protected static AppiumDriver driver;

    protected static void android_Setup() {

        UiAutomator2Options options = new UiAutomator2Options()
                .setPlatformName("Android")
                .setAutomationName("UiAutomator2")
                .setUdid(PropertyUtils.getDeviceProp("ANDROID_UDID"))
                .setApp(APPS_DIR + PropertyUtils.getAppProp("android_todo_name"))
                .setAutoGrantPermissions(true)
                ;
        try {
            driver = new AndroidDriver(new URL(PropertyUtils.getDeviceProp("APPIUM_SERVER")), options);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to create web driver session");
        }
    }

    protected static void iOS_Setup() {
        XCUITestOptions options = new XCUITestOptions()
                .setPlatformName("iOS")
                .setAutomationName("XCUITest")
                .setUdid(PropertyUtils.getDeviceProp("IOS_UDID"))
                .setApp(APPS_DIR + PropertyUtils.getAppProp("ios_dailycheck_name"))
                .setAutoAcceptAlerts(true)
                ;
        try {
            driver = new IOSDriver(new URL(PropertyUtils.getDeviceProp("APPIUM_SERVER")), options);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to create web driver session");
        }
    }


    protected static void tearDown() {
        if (null != driver) {
            driver.quit();
        }
    }

    @DataProvider(name = "tasks data")
    protected Object[][] getTasksData() {
        return JsonUtils.getJsonData(TEST_DATA_DIR + "TasksData.json", "Tasks Data", 2);
    }
}
