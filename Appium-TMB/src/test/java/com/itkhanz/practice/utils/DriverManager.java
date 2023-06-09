package com.itkhanz.practice.utils;

import com.itkhanz.practice.constants.App;
import com.itkhanz.practice.constants.PlatformOS;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.options.XCUITestOptions;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.Map;
import java.util.Properties;

public class DriverManager {
    private static ThreadLocal<AppiumDriver> driver = new ThreadLocal<AppiumDriver>();

    public static AppiumDriver getDriver() {
        return driver.get();
    }

    public static void setDriver(AppiumDriver dr) {
        driver.set(dr);
    }

    /**
     * This method can be used in test cases to get the Appium driver.
     * It checks if the driver for current thread is already created, it supplies the driver else it initializes new driver.
     * @param platformOS can be ANDROID or IOS
     * @param appName can be APIDEMOS, UIKITCATALOG, MAPS, IOSMAPS, MYDEMOAPP-ANDROID
     * @return AppiumDriver
     */
    public static AppiumDriver initializeDriver(PlatformOS platformOS, App appName, String udid, String port){
        if(DriverManager.getDriver()==null) {
            try {
                new DriverManager(platformOS, appName, udid, port);
            } catch (Exception e) {
                throw new RuntimeException("Failed to initialize the DriverManager");
            }
        }
        return DriverManager.getDriver();
    }

    /**
     * Closes the driver session
     */
    public static void shutdownDriver(){
        if(getDriver() != null) {
            try {
                getDriver().quit();
            } catch (Exception e) {
                throw new RuntimeException("Failed to quit the Driver from thread: " + Thread.currentThread().getId());
            }
        }
    }

    /**
     * Creates new Appium Driver with desired capabilities and set the created driver to Driver Factory
     * @param platformOS can be ANDROID or IOS
     * @param appName can be APIDEMOS, UIKITCATALOG, MAPS, IOSMAPS, MYDEMOAPP-ANDROID
     * @throws MalformedURLException
     */
    private DriverManager(PlatformOS platformOS, App appName, String udid, String port) throws MalformedURLException {
        AppiumDriver driver;

        URL url = new URL(getFormattedUrl(port));   //Appium Server URL and port

        Map<String, String> appCapabilities = getAppCapabilities(appName);  //app specific capabilities
        Properties deviceProperties = getDeviceProperties(platformOS);      //device specific capabilities
        String udidDevice = getDeviceUdid(udid,deviceProperties);           //UDID of the device to be initialzed

        switch(platformOS) {
            case ANDROID:
                driver = new AndroidDriver(url, getUiAutomator2Options(udidDevice, appCapabilities));
                break;
            case IOS:
                driver = new IOSDriver(url, getXCUITestOptions(udidDevice, appCapabilities));
                break;
            default:
                throw new RuntimeException("Unable to create session with platform: " + platformOS.platformName);
        }
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        DriverManager.setDriver(driver);
    }

    /**
     * Creates UIAutomator2Options for the Appium Driver Session
     * @param udid UDID of the android device emulator
     * @param appCapabilities List of app specific properties such as Url, activity and package
     * @return new instance of UIAutomator2Options for initializing appium driver
     */
    private static UiAutomator2Options getUiAutomator2Options(String udid, Map<String, String> appCapabilities) {
        return new UiAutomator2Options()
                //.setPlatformName(platformOS.platformName) //optional
                //.setAutomationName("UiAutomator2")  //optional
                //.setDeviceName("pixel_5")     //not mandatory with udid
                .setUdid(udid)
                //.setApp(appCapabilities.get("app"))   //Install the app if not pre-installed, not needed with AppPackage and AppActivity
                .setAppPackage(appCapabilities.get("appPackage"))
                .setAppActivity(appCapabilities.get("appActivity"))
                .setAutoGrantPermissions(true)
                .setNewCommandTimeout(Duration.ofSeconds(180))
                //.setAvd("Pixel_5")  //automatically launches the android emulator with given avdID
                //.setAvdLaunchTimeout(Duration.ofSeconds(180))
                ;
    }

    /**
     * Creates XCUITestOptions for the Appium Driver Session
     * @param udid UDID of the iOS device simulator
     * @param appCapabilities List of app specific properties such as Url, bundleID
     * @return new instance of XCUITestOptions for initializing appium driver
     */
    private static XCUITestOptions getXCUITestOptions(String udid, Map<String, String> appCapabilities) {
        return new XCUITestOptions()
                //.setAutomationName("XCUITest")            //optional
                //.setPlatformName(platformOS.platformName) //optional
                //.setDeviceName("iPhone 14")               //not mandatory when udid is provided
                .setUdid(udid)
                //.setApp(appCapabilities.get("app"))      //Install the app if not pre-installed
                .setBundleId(appCapabilities.get("bundleId"))
                //.setSimulatorStartupTimeout(Duration.ofSeconds(180))  //waits for the simulator to launch
                .setAutoAcceptAlerts(true)
                .setNewCommandTimeout(Duration.ofSeconds(180))
                ;
    }

    /**
     * Returns the Udid of the device to be initialized for appium session
     * @param udid string udid from the test case
     * @param deviceProperties contains the device specific info and default udid if the udid from test case is null
     * @return string udid of the device to be initialzed
     */
    private static String getDeviceUdid(String udid, Properties deviceProperties) {
        if (udid != null && !udid.isBlank()) {
            return udid;
        }

        //If no udid is provided then the default device from properties file are setup for emulator/simulator
        return deviceProperties.getProperty("UDID");
    }


    /**
     * This method generates the app specific capabilities for the appium session
     * @param appName Constant App identifier
     * @return Key,value pair of the app specific capabilities
     */
    private static Map<String, String> getAppCapabilities(App appName) {
        switch (appName) {
            case APIDEMOS:
                return Map.of("appPackage","io.appium.android.apis",
                        "appActivity", "io.appium.android.apis.ApiDemos",
                        "appUrl", System.getProperty("user.dir") + "/src/main/resources/apps/ApiDemos-debug.apk");
            case UIKITCATALOG:
                return Map.of("bundleId","com.example.apple-samplecode.UICatalog",
                        "appUrl", System.getProperty("user.dir") + "/src/main/resources/apps/UIKitCatalog-iphonesimulator.app");
            case MAPS:
                return Map.of("appPackage","com.google.android.apps.maps",
                        "appActivity", "com.google.android.maps.MapsActivity");
            case IOSMAPS:
                return Map.of("bundleId","com.apple.Maps");
            case MYDEMOAPPANDROID:
                return Map.of("appPackage","com.saucelabs.mydemoapp.rn",
                        "appActivity", "com.saucelabs.mydemoapp.rn.MainActivity",
                            "appUrl", System.getProperty("user.dir") + "/src/main/resources/apps/Android-MyDemoAppRN.1.3.0.build-244.apk");
            case MYDEMOAPPIOS:
                return Map.of("bundleId","com.saucelabs.mydemoapp.rn",
                        "appUrl", System.getProperty("user.dir") + "/Users/ibkh/dev/demo-apps/iOS-Simulator-MyRNDemoApp.1.3.0-162.zip");
            default:
                throw new RuntimeException("Invalid app: " + appName);
        }
    }

    /**
     * Concatenate the IP with port. If port is not specified, then default port is selected from properties file
     * @param port appium server port
     * @return formatted string for Appium server URL e.g. http://localhost:4723
     */
    private static String getFormattedUrl(String port) {
        Properties serverProperties = PropertyUtils.propertyLoader("src/test/resources/server.properties");
        if (port != null && !port.isBlank()) {
            return serverProperties.getProperty("ip") + ":" + port;
        }
        return serverProperties.getProperty("ip") + ":" + serverProperties.getProperty("port");
    }

    /**
     * Loads the platform specific properties for device such as udid, device name
     * @param platformOS
     * @return device properties
     */
    private static Properties getDeviceProperties(PlatformOS platformOS) {
        if (platformOS.platformName.equalsIgnoreCase("ANDROID")) {
            return PropertyUtils.propertyLoader("src/test/resources/android.properties");
        }
        return PropertyUtils.propertyLoader("src/test/resources/ios.properties");
    }
}
