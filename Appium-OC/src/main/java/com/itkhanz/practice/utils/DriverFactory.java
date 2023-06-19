package com.itkhanz.practice.utils;

import com.itkhanz.practice.constants.Apps;
import com.itkhanz.practice.constants.Platform;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.options.XCUITestOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.Map;
import java.util.Optional;

public class DriverFactory {

    private static ThreadLocal<AppiumDriver> driver = new ThreadLocal<AppiumDriver>();

    public static final String APPS_DIR = System.getProperty("user.dir")
            + File.separator + "src"
            + File.separator + "main"
            + File.separator + "resources"
            + File.separator + "apps"
            + File.separator
            ;

    private static AppiumDriver getDriver() {
        return driver.get();
    }

    private static void setDriver(AppiumDriver dr) {
        driver.set(dr);
    }

    public static AppiumDriver initializeDriver(Platform platform, Apps appName){
        if(DriverFactory.getDriver()==null) {
            try {
                new DriverFactory(platform, appName);
            } catch (Exception e) {
                System.out.println(e.getClass().getSimpleName());
                System.out.println(e.getMessage());
                e.printStackTrace();
                throw new RuntimeException("Failed to initialize the DriverFactory");
            }
        }
        System.out.println("[ThreadID] " + Thread.currentThread().getId() + " [SessionID] " + ((RemoteWebDriver)getDriver()).getSessionId());
        return DriverFactory.getDriver();
    }

    public static void shutdownDriver(){
        if(getDriver() != null) {
            try {
                getDriver().quit();
                driver.remove();
            } catch (Exception e) {
                throw new RuntimeException("[ThreadID] " + Thread.currentThread().getId() + "Failed to quit the Driver");
            }
        }
        Optional<RemoteWebDriver> remoteDriver = Optional.ofNullable(((RemoteWebDriver)getDriver()));
        if (remoteDriver.isEmpty()) System.out.println("[ThreadID] " + Thread.currentThread().getId() + " Driver session terminated successfully");
    }

    private DriverFactory(Platform platform, Apps appName) throws MalformedURLException {
        AppiumDriver driver;
        URL url = new URL("http://0.0.0.0:4723");   //Appium Server URL and port
        Map<String, String> appCapabilities = getAppCapabilities(appName);

        switch(platform) {
            case ANDROID:
                driver = new AndroidDriver(url, getUiAutomator2Options(appCapabilities));
                break;
            case IOS:
                driver = new IOSDriver(url, getUiAutomator2Options(appCapabilities));
                break;
            default:
                throw new RuntimeException("Unable to create session with platform: " + platform.platformName);
        }

        System.out.println("[Driver Capabilities] " + driver.getCapabilities().getCapability("appium:desired"));
        //driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10)); //use Explicit Waits or appium wait plugin
        DriverFactory.setDriver(driver);
    }


    /**
     * This method generates the app specific capabilities for the appium session
     * @param appName Constant App identifier
     * @return Key,value pair of the app specific capabilities
     */
    private static Map<String, String> getAppCapabilities(Apps appName) {
        switch (appName) {
            case APIDEMOS:
                return Map.of("appPackage","io.appium.android.apis",
                        "appActivity", "io.appium.android.apis.ApiDemos",
                        "appUrl", APPS_DIR + "ApiDemos-debug.apk");
            case UIKITCATALOG:
                return Map.of("bundleId","com.example.apple-samplecode.UICatalog",
                        "appUrl", APPS_DIR + "UIKitCatalog-iphonesimulator.app");
            case MAPS:
                return Map.of("appPackage","com.google.android.apps.maps",
                        "appActivity", "com.google.android.maps.MapsActivity");
            case IOSMAPS:
                return Map.of("bundleId","com.apple.Maps");
            default:
                throw new RuntimeException("Invalid app: " + appName);
        }
    }


    private static UiAutomator2Options getUiAutomator2Options(Map<String, String> appCapabilities) {
        return new UiAutomator2Options()
                .setPlatformName("android") //optional
                .setAutomationName("UiAutomator2")  //optional
                .setDeviceName("pixel_5")     //not mandatory with udid
                .setUdid("emulator-5554")
                .setApp(appCapabilities.get("appUrl"))   //Install the app if not pre-installed, not needed with AppPackage and AppActivity
                .setAppPackage(appCapabilities.get("appPackage"))
                .setAppActivity(appCapabilities.get("appActivity"))
                .setAutoGrantPermissions(true)
                .setNewCommandTimeout(Duration.ofSeconds(180))
                //.setAvd("Pixel_5")  //automatically launches the android emulator with given avdID
                //.setAvdLaunchTimeout(Duration.ofSeconds(180))
                .setUnlockType("fingerprint")   //['pin', 'password', 'pattern', 'fingerprint', 'pinWithKeyEvent'] Set one of the possible types of Android lock screens to unlock.
                .setUnlockKey("1")  //Allows to set an unlock key. e.g. 1235789 for Z pattern or if pin then e.g. 1234
                ;
    }

    private static XCUITestOptions getXCUITestOptions(Map<String, String> appCapabilities) {
        return new XCUITestOptions()
                .setAutomationName("XCUITest")            //optional
                .setPlatformName("iOS") //optional
                .setDeviceName("iPhone 14")               //not mandatory when udid is provided
                .setUdid("6B4B083D-5F01-4B6D-88D1-175A4AFA3C4F")
                .setApp(appCapabilities.get("appUrl"))      //Install the app if not pre-installed
                .setBundleId(appCapabilities.get("bundleId"))
                //.setSimulatorStartupTimeout(Duration.ofSeconds(180))  //waits for the simulator to launch
                .setAutoAcceptAlerts(true)
                .setNewCommandTimeout(Duration.ofSeconds(180))
                ;
    }
}
