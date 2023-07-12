package com.itkhanz.tests.chap5.android;

import com.itkhanz.utils.PropertyUtils;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.net.MalformedURLException;
import java.net.URL;

public class SendingSMSTest {
    private AppiumDriver driver;

    @BeforeTest
    private void setUp() throws MalformedURLException {

        UiAutomator2Options options = new UiAutomator2Options()
                .setPlatformName("Android")
                .setAutomationName("UiAutomator2")
                .setUdid(PropertyUtils.getDeviceProp("ANDROID_UDID"))
                .setAppPackage("com.google.android.apps.messaging")
                .setAppActivity(".ui.ConversationListActivity")
                ;

        driver = new AndroidDriver(new URL(PropertyUtils.getDeviceProp("APPIUM_SERVER")), options);
    }

    @Test
    public void send_SMS_test() {
        //Documentation
        //https://appium.readthedocs.io/en/latest/en/commands/device/network/send-sms/
        ((AndroidDriver)driver).sendSMS("555-123-4567","Hello from TAU");
    }

    @AfterTest
    private void tearDown() {
        if (null != driver) {
            driver.quit();
        }
    }
}

