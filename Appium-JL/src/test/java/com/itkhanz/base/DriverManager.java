package com.itkhanz.base;

import com.itkhanz.enums.Platform;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.EspressoOptions;
import io.appium.java_client.android.options.UiAutomator2Options;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.options.XCUITestOptions;
import org.openqa.selenium.remote.SessionId;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.Map;

import static com.itkhanz.constants.Paths.BROWSER_DIR;
import static com.itkhanz.utils.PropertyUtils.getDeviceProp;

public class DriverManager {
    private static final ThreadLocal<AppiumDriver> driver = new ThreadLocal<AppiumDriver>();
    private static final ThreadLocal<SessionId> sessionId = new ThreadLocal<SessionId>();

    private static AppiumDriver getDriver() {
        return driver.get();
    }

    private static void setDriver(AppiumDriver dr) {
        driver.set(dr);
    }

    public static SessionId getThreadSessionID() {
        return sessionId.get();
    }

    private static void setThreadSessionID(SessionId sID) {
        sessionId.set(sID);
    }


    public static void shutdownDriver(){
        if(getDriver() != null) {
            try {
                getDriver().quit();
                driver.remove();
            } catch (Exception e) {
                throw new RuntimeException("Failed to quit the Driver from thread: " + Thread.currentThread().getId());
            }
        }
        System.out.println("Terminated Driver Session with sessionID: " + DriverManager.getThreadSessionID());
        sessionId.remove();
    }

    public static AppiumDriver initializeDriver(Platform platform, Map<String, String> appProps){
        if(DriverManager.getDriver()==null) {
           new DriverManager(platform, appProps);
        }

        return DriverManager.getDriver();
    }

    DriverManager(Platform platform, Map<String, String> appProps) {
        try {
            AppiumDriver driver;
            switch(platform) {
                case ANDROID:
                    driver = setUpAndroidUIAutomator2(appProps);
                    break;
                case ANDROIDESPRESSO:
                    driver = setUpAndroidEspresso(appProps);
                    break;
                case IOS:
                    driver = setUpIOS(appProps);
                    break;
                default:
                    throw new IllegalArgumentException("Unable to create session with platform: " + platform);
            }
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
            DriverManager.setDriver(driver);
            DriverManager.setThreadSessionID(DriverManager.getDriver().getSessionId());
            System.out.println("Initialized Driver Session with sessionID: " + DriverManager.getThreadSessionID());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println(e.getClass().getSimpleName());
            e.printStackTrace();
            throw new RuntimeException("Failed to initialize the DriverManager");
        }
    }

    private static AppiumDriver setUpAndroidUIAutomator2(Map<String, String> appProps) throws MalformedURLException {
        UiAutomator2Options options = new UiAutomator2Options()
                .setPlatformName("Android")
                .setAutomationName("UiAutomator2")
                .setUdid(getDeviceProp("ANDROID_UDID"))
                .autoGrantPermissions()
                .setChromedriverExecutableDir(BROWSER_DIR)  //Download the chromeDriver compatible with the chrome version on your device/emulator from https://chromedriver.chromium.org/downloads
                .setAutoWebviewTimeout(Duration.ofSeconds(10))
                .setAdbExecTimeout(Duration.ofSeconds(30))
                ;
        if (appProps.containsKey("appUrl")) options = options.setApp(appProps.get("appUrl"));
        if (appProps.containsKey("appPackage")) options = options.setAppPackage(appProps.get("appPackage"));
        if (appProps.containsKey("appActivity")) options = options.setAppActivity(appProps.get("appActivity"));
        return new AndroidDriver(new URL(getDeviceProp("APPIUM_SERVER")), options);
    }

    private static AppiumDriver setUpAndroidEspresso(Map<String, String> appProps) throws MalformedURLException {
        EspressoOptions options = new EspressoOptions()
                .setPlatformName("Android")
                .setAutomationName("Espresso")
                .setUdid(getDeviceProp("ANDROID_UDID"))
                .autoGrantPermissions()
                .setChromedriverExecutableDir(BROWSER_DIR)  //Download the chromeDriver compatible with the chrome version on your device/emulator from https://chromedriver.chromium.org/downloads
                .setAutoWebviewTimeout(Duration.ofSeconds(10))
                .setAdbExecTimeout(Duration.ofSeconds(30))
                ;
        if (appProps.containsKey("appUrl")) options = options.setApp(appProps.get("appUrl"));
        if (appProps.containsKey("appPackage")) options = options.setAppPackage(appProps.get("appPackage"));
        if (appProps.containsKey("appActivity")) options = options.setAppActivity(appProps.get("appActivity"));
        return new AndroidDriver(new URL(getDeviceProp("APPIUM_SERVER")), options);
    }

    private static AppiumDriver setUpIOS(Map<String, String> appProps) throws MalformedURLException {
        XCUITestOptions options = new XCUITestOptions()
                .setPlatformName("iOS")
                .setAutomationName("XCUITest")
                .setUdid(getDeviceProp("IOS_UDID"))
                .includeSafariInWebviews()
                .setWebviewConnectTimeout(Duration.ofSeconds(10))
                ;
        if (appProps.containsKey("appUrl")) options = options.setApp(appProps.get("appUrl"));
        if (appProps.containsKey("bundleId")) options = options.setBundleId(appProps.get("bundleId"));
        return new IOSDriver(new URL(getDeviceProp("APPIUM_SERVER")), options);
    }
}
