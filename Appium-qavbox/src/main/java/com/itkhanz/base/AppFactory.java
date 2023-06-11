package com.itkhanz.base;

import com.itkhanz.constants.App;
import com.itkhanz.constants.PlatformOS;
import com.itkhanz.utils.PropertyUtils;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.options.XCUITestOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.Map;
import java.util.Properties;

import static com.itkhanz.base.DriverManager.getDriver;

public class AppFactory {
    static AppiumDriver driver;

    /**
     * Creates new Appium Driver with specified platform and app and set the created driver to Driver Factory
     * @param platformOS can be any of the constants specified in PlatformOS Enum
     * @param appName can be any of the constants specified in App Enum
     * @param udid udid of the emulator or simulator or real-device, if null then default device for OS is setup
     * @param port port of the appium server, if null then default port is used
     * @throws MalformedURLException
     */
    public static void launchApp(PlatformOS platformOS, App appName, String udid, String port) {
        try {
            URL url = new URL(PropertiesManager.getFormattedUrl(port));   //Appium Server URL and port

            Map<String, String> appCapabilities = getAppCapabilities(appName);  //app specific capabilities
            String udidDevice = PropertiesManager.getDeviceUdid(udid,platformOS);           //UDID of the device to be initialzed

            switch(platformOS) {
                case ANDROID:
                    UiAutomator2Options androidOptions = getUiAutomator2Options(udidDevice, appCapabilities);
                    launchAndroidApp(url, androidOptions);
                    break;
                case IOS:
                    XCUITestOptions iOSOptions = getXCUITestOptions(udidDevice, appCapabilities);
                    launchiOSApp(url, iOSOptions);
                    break;
                default:
                    throw new RuntimeException("Unable to create session with platform: " + platformOS.platformName);
            }
        } catch (Exception e) {
            System.out.println("Could not launch app: " + appName.appName + " on Server: " + PropertiesManager.getFormattedUrl(port));
            System.out.println(e.getClass().getSimpleName());
            System.out.println(e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to launch app and start session");
        }
    }

    public static void launchAndroidApp(URL url, UiAutomator2Options options) {
        driver = new AndroidDriver(url, options);
        DriverManager.setDriver(driver);
        System.out.println("AndroidDriver is set");
    }

    public static void launchiOSApp(URL url, XCUITestOptions options) {
        driver = new IOSDriver(url, options);
        DriverManager.setDriver(driver);
        System.out.println("IOSDriver is set");
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
     * This method generates the app specific capabilities for the appium session
     * //TODO Move these capabilities to JSON or properties file (or yaml file and read from there)
     * @param appName Constant App identifier
     * @return Key,value pair of the app specific capabilities
     */
    private static Map<String, String> getAppCapabilities(App appName) {
        String APPS_DIRECTORY = System.getProperty("user.dir") + "/src/main/resources/apps/";

        switch (appName) {
            case APIDEMOS:
                return Map.of("appPackage","io.appium.android.apis",
                        "appActivity", "io.appium.android.apis.ApiDemos",
                        "appUrl", APPS_DIRECTORY + "ApiDemos-debug.apk");
            case UIKITCATALOG:
                return Map.of("bundleId","com.example.apple-samplecode.UICatalog",
                        "appUrl", APPS_DIRECTORY + "UIKitCatalog-iphonesimulator.app");
            case MAPS:
                return Map.of("appPackage","com.google.android.apps.maps",
                        "appActivity", "com.google.android.maps.MapsActivity");
            case IOSMAPS:
                return Map.of("bundleId","com.apple.Maps");
            case SAUCELABSANDROID:
                return Map.of("appPackage","com.saucelabs.mydemoapp.rn",
                        "appActivity", "com.saucelabs.mydemoapp.rn.MainActivity",
                        "appUrl", APPS_DIRECTORY + "Android-MyDemoAppRN.1.3.0.build-244.apk");
            case SAUCELABSIOS:
                return Map.of("bundleId","com.saucelabs.mydemoapp.rn",
                        "appUrl", APPS_DIRECTORY + "iOS-Simulator-MyRNDemoApp.1.3.0-162.zip");
            case WDIOANDROID:
                return Map.of("appPackage","com.wdiodemoapp",
                        "appActivity", "com.wdiodemoapp.MainActivity",
                        "appUrl", APPS_DIRECTORY + "Android-NativeDemoApp-0.4.0.apk",
                        "appWaitActivity", "*");
            case WDIOIOS:
                return Map.of("bundleId","org.wdioNativeDemoApp",
                        "appUrl", APPS_DIRECTORY + "iOS-Simulator-NativeDemoApp-0.4.0.app.zip");
            default:
                throw new RuntimeException("Invalid app: " + appName);
        }
    }


}
