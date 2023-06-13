package com.itkhanz.base;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itkhanz.constants.App;
import com.itkhanz.constants.PlatformOS;
import com.itkhanz.pojo.applications.ApplicationsRoot;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.options.XCUITestOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.Duration;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class AppFactory {
    static AppiumDriver driver;
    private static final Logger LOG = LoggerFactory.getLogger("AppFactory.class");

    /**
     * Creates new Appium Driver with specified platform and app and set the created driver to Driver Factory
     * @param platformOS can be any of the constants specified in PlatformOS Enum
     * @param app can be any of the constants specified in App Enum
     * @param udid udid of the emulator or simulator or real-device, if null then default device for OS is setup
     * @param port port of the appium server, if null then default port is used
     */
    public static void launchApp(PlatformOS platformOS, App app, String udid, String port) {
        String appID = app.getId();
        try {
            URL url = new URL(PropertiesManager.getFormattedUrl(port));   //Appium Server URL and port
            LOG.info("Appium Server url: " + url.toString());

            Map<String, String> appCapabilities = getAppCapabilities(appID, platformOS);  //app specific capabilities
            String udidDevice = PropertiesManager.getDeviceUdid(udid,platformOS);           //UDID of the device to be initialzed
            LOG.info("Device UDID: " + udidDevice);

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
            LOG.error("Could not launch app: " + appID + " on Server: " + PropertiesManager.getFormattedUrl(port));
            System.out.println("Could not launch app: " + appID + " on Server: " + PropertiesManager.getFormattedUrl(port));
            System.out.println(e.getClass().getSimpleName());
            System.out.println(e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to launch app and start session");
        }
        LOG.info("Application launched: " + appID);
        //DriverManager.setupDriverTimeouts();
    }

    public static void launchAndroidApp(URL url, UiAutomator2Options options) {
        driver = new AndroidDriver(url, options);
        DriverManager.setDriver(driver);
        //System.out.println("AndroidDriver is set");
        LOG.info("AndroidDriver is set");
    }

    public static void launchiOSApp(URL url, XCUITestOptions options) {
        driver = new IOSDriver(url, options);
        DriverManager.setDriver(driver);
        //System.out.println("IOSDriver is set");
        LOG.info("IOSDriver is set");
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
     * This method generates the app specific capabilities for the appium session.
     * It looks for the application specific properties in applications.json file based on the input parameter appID
     *
     * @param appID Constant App identifier
     * @param platformOS target platform
     * @return Key,value pair of the app specific capabilities
     */
    private static Map<String, String> getAppCapabilities(String appID, PlatformOS platformOS) {
        Map<String, String> applicationCapabilities = new HashMap<String, String>();
        try {
            //Deserializing JSON to POJO
            ObjectMapper mapper = new ObjectMapper();
            ApplicationsRoot applicationsRoot = mapper.readValue(
                    new File(System.getProperty("user.dir") + "/src/test/resources/applications.json"),
                    ApplicationsRoot.class);

            //Converting POJO to Map (Filter the application properties based on appID)
            applicationCapabilities = applicationsRoot.getApplications()
                    .stream()
                    .filter(app -> app.getId().equalsIgnoreCase(appID))
                    .map(app -> mapper.convertValue(app.getProperties(), new TypeReference<Map<String, String>>() {}))
                    .findFirst().orElseThrow(() -> new RuntimeException("applications.json does not have app: " + appID));

            //Concatenating directory path to appUrl from applications.json
            if (applicationCapabilities.containsKey("appUrl")) {
                applicationCapabilities.put("appUrl", System.getProperty("user.dir") + applicationsRoot.getAppsDirectory() + applicationCapabilities.get("appUrl"));
            }
        } catch (IOException e) {
            LOG.error("Invalid app: " + appID);
            System.out.println(e.getClass().getSimpleName());
            System.out.println(e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Invalid app: " + appID);
        }

        LOG.info("App will be loaded with following options: " + Collections.singletonList(applicationCapabilities));
        //System.out.println(Collections.singletonList(applicationCapabilities));
        return applicationCapabilities;
    }


}
