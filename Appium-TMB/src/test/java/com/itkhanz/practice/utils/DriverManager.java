package com.itkhanz.practice.utils;

import com.itkhanz.practice.constants.App;
import com.itkhanz.practice.constants.PlatformOS;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.remote.MobileCapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

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
     * Creates new Appium Driver with desired capabilities and set the created driver to Driver Factory
     * @param platformOS can be ANDROID or IOS
     * @param appName can be APIDEMOS, UIKITCATALOG, MAPS, IOSMAPS, MYDEMOAPP-ANDROID
     * @throws MalformedURLException
     */
    private DriverManager(PlatformOS platformOS, App appName, String udid, String port) throws MalformedURLException {
        AppiumDriver driver;

        URL url = new URL(getFormattedUrl(port));   //Appium Server URL and port

        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability("newCommandTimeout", 300);

        Map<String, String> appCapabilities = getAppCapabilities(appName);

        Properties deviceProperties = getDeviceProperties(platformOS);
        String udidDevice;
        if (udid != null && !udid.isBlank()) {
            udidDevice = udid;
        } else  {
            //If no udid is provided then the default device from properties file are setup for emulator/simulator
            udidDevice = deviceProperties.getProperty("UDID");
        }

        switch(platformOS) {
            case ANDROID:
                caps.setCapability(MobileCapabilityType.PLATFORM_NAME , platformOS.platformName);
                //caps.setCapability(MobileCapabilityType.DEVICE_NAME ,"pixel_5");
                caps.setCapability(MobileCapabilityType.AUTOMATION_NAME ,"UiAutomator2");
                caps.setCapability(MobileCapabilityType.UDID ,udidDevice);
                caps.setCapability("appPackage", appCapabilities.get("appPackage"));
                caps.setCapability("appActivity", appCapabilities.get("appActivity"));
                caps.setCapability("autoGrantPermissions", "true");
                //caps.setCapability("avd", "Pixel_5"); //automatically launches the android emulator with given avdID
                //caps.setCapability("avdLaunchTimeout", 180000);
                //caps.setCapability("app", appCapabilities.get("app"));    //Install the app if not pre-installed
                driver = new AndroidDriver(url, caps);
                break;
            case IOS:
                caps.setCapability(MobileCapabilityType.PLATFORM_NAME , platformOS.platformName);
                //caps.setCapability(MobileCapabilityType.DEVICE_NAME ,"iPhone 14");
                caps.setCapability(MobileCapabilityType.AUTOMATION_NAME ,"XCUITest");
                caps.setCapability(MobileCapabilityType.UDID ,udidDevice);
                caps.setCapability("bundleId", appCapabilities.get("bundleId"));
                caps.setCapability("autoAcceptAlerts", "true");
                //caps.setCapability("simulatorStartupTimeout", 180000);
                //caps.setCapability("app", appCapabilities.get("app"));    //Install the app if not pre-installed
                driver = new IOSDriver(url, caps);
                break;
            default:
                throw new RuntimeException("Unable to create session with platform: " + platformOS.platformName);
        }
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        DriverManager.setDriver(driver);
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
                        "appUrl", System.getProperty("user.dir") + "/src/main/resources/apps/UIKitCatalog-iphonesimulator.app");
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
