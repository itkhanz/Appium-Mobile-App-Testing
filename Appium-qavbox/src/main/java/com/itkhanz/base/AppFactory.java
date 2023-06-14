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
import io.appium.java_client.remote.MobileCapabilityType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class AppFactory {
    static AppiumDriver driver;
    private static final Logger LOG = LoggerFactory.getLogger("AppFactory.class");

    /**
     * Formats the URL from appium server ip and port, and pass it to child method to create platform based driver
     * @param platformOS can be any of the constants specified in PlatformOS Enum
     * @param appCapabilities application options based on platform and app loaded from test case and application.properties
     */
    public static void launchApp(PlatformOS platformOS, Map<String, Object> appCapabilities) {

        try {
            URL url = new URL(PropertiesManager.getFormattedUrl());   //Appium Server URL and port
            LOG.info("Appium Server url: " + url.toString());

            createDriver(platformOS, url, appCapabilities);
        } catch (Exception e) {
            LOG.error("Could not launch app on Server: " + PropertiesManager.getFormattedUrl());
            System.out.println("Could not launch app on Server: " + PropertiesManager.getFormattedUrl());
            System.out.println(e.getClass().getSimpleName());
            System.out.println(e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to launch app and start session");
        }
        LOG.info("Application launched successfully");
        //DriverManager.setupDriverTimeouts();
    }

    /**
     * Gets the platform specific options for Platform and pass forwards them for setting driver
     * @param platformOS can be any of the constants specified in PlatformOS Enum
     * @param url ip address and port of the appium server as URL
     * @param appCapabilities application options based on platform and app loaded from test case and application.properties
     */
    private static void createDriver(PlatformOS platformOS, URL url, Map<String, Object> appCapabilities) {
        switch(platformOS) {
            case ANDROID:
                UiAutomator2Options androidOptions = getUiAutomator2Options(appCapabilities);
                setAndroidDriver(url, androidOptions);
                break;
            case IOS:
                XCUITestOptions iOSOptions = getXCUITestOptions(appCapabilities);
                setIOSDriver(url, iOSOptions);
                break;
            default:
                throw new RuntimeException("Unable to create session with platform: " + platformOS.platformName);
        }
    }

    /**
     * creates new android driver from URL and capabilities and sets the created driver to DriverManager's Appium driver
     * @param url url ip address and port of the appium server as URL
     * @param options android uiautomator2options (capabilities)
     */
    private static void setAndroidDriver(URL url, UiAutomator2Options options) {
        driver = new AndroidDriver(url, options);
        DriverManager.setDriver(driver);
        //System.out.println("AndroidDriver is set");
        LOG.info("AndroidDriver is set");
    }

    /**
     * creates new IOS driver from URL and capabilities and sets the created driver to DriverManager's Appium driver
     * @param url url ip address and port of the appium server as URL
     * @param options ios XCUITestOptions (capabilities)
     */
    private static void setIOSDriver(URL url, XCUITestOptions options) {
        driver = new IOSDriver(url, options);
        DriverManager.setDriver(driver);
        //System.out.println("IOSDriver is set");
        LOG.info("IOSDriver is set");
    }

    /**
     * Creates UIAutomator2Options for the Appium Driver Session
     * @param appCapabilities List of app specific properties such as Url, activity and package
     * @return new instance of UIAutomator2Options for initializing appium driver
     */
    private static UiAutomator2Options getUiAutomator2Options(Map<String, Object> appCapabilities) {
        appCapabilities.putIfAbsent(MobileCapabilityType.PLATFORM_NAME, "android"); //optional
        appCapabilities.putIfAbsent(MobileCapabilityType.AUTOMATION_NAME, "uiautomator2"); //optional
        appCapabilities.putIfAbsent("autoGrantPermissions", true);
        appCapabilities.putIfAbsent("newCommandTimeout", 180);     //in seconds
        //appCapabilities.putIfAbsent("app", appCapabilities.get("appUrl"));     //Full path to the application to be tested (Install the app if not pre-installed, not needed with AppPackage and AppActivity)
        //appCapabilities.putIfAbsent("avd", "Pixel_5");   //The name of Android emulator to run the test on (automatically launches the android emulator with given avdID)
        //appCapabilities.putIfAbsent("avdLaunchTimeout", 180000);   //TMaximum number of milliseconds to wait until Android Emulator is started. 60000 ms by default

        UiAutomator2Options options = new UiAutomator2Options(appCapabilities);
        LOG.info("UIAutomator2 options set as: " + Arrays.toString(appCapabilities.entrySet().toArray()));
        //System.out.println("UIAutomator2 options set as: " + Arrays.toString(appCapabilities.entrySet().toArray()));

        return options;
    }

    /**
     * Creates XCUITestOptions for the Appium Driver Session
     * @param appCapabilities List of app specific properties such as Url, bundleID
     * @return new instance of XCUITestOptions for initializing appium driver
     */
    private static XCUITestOptions getXCUITestOptions(Map<String, Object> appCapabilities) {
        appCapabilities.putIfAbsent(MobileCapabilityType.PLATFORM_NAME, "iOS"); //optional
        appCapabilities.putIfAbsent(MobileCapabilityType.AUTOMATION_NAME, "XCUITest"); //optional
        appCapabilities.putIfAbsent("autoAcceptAlerts", true);     //Accept all iOS alerts automatically if they pop up.
        appCapabilities.putIfAbsent("newCommandTimeout", 180);     //How long (in seconds) the driver should wait for a new command from the client before assuming the client has stopped sending requests.
        appCapabilities.putIfAbsent("noReset", true);              //Prevents the device to be reset before the session startup if set to true.
        //appCapabilities.putIfAbsent("app", appCapabilities.get("appUrl"));     //Full path to the application to be tested (Install the app if not pre-installed, not needed with AppPackage and AppActivity)
        //appCapabilities.putIfAbsent("simulatorStartupTimeout", 1800000);    //Allows to change the default timeout for Simulator startup. By default this value is set to 120000ms (2 minutes)

        XCUITestOptions options = new XCUITestOptions(appCapabilities);
        LOG.info("XCUITestOptions options set as: " + Arrays.toString(appCapabilities.entrySet().toArray()));
        //System.out.println("XCUITestOptions options set as: " + Arrays.toString(appCapabilities.entrySet().toArray()));

        return options;
    }

    /**
     * This method creates a Map of app capabilities loaded from application.properties and also adds the test specific properties.
     * This map is then passed in next steps to launch the app with specified capabilities
     * @param platformOS platform constant i.e. Android or IOS
     * @param app App constant
     * @param udid Test Specific Device UDID. If left as null, default udid from properties file will be loaded
     * @return Map of app capabilities
     */
    public static Map<String, Object> getAppCapabilities(PlatformOS platformOS, App app, String udid) {
        String appID = app.getId();
        Map<String, Object> appCapabilities = getAppPropertiesAsMap(appID, platformOS);  //app specific capabilities from applications.json

        String udidDevice = PropertiesManager.getDeviceUdid(udid,platformOS);           //UDID of the device to be initialzed
        LOG.info("Device UDID: " + udidDevice);
        appCapabilities.put("udid", udidDevice);

        return appCapabilities;
    }

    /**
     * This method generates the app specific capabilities for the appium session.
     * It looks for the application specific properties in applications.json file based on the input parameter appID
     *
     * @param appID Constant App identifier
     * @param platformOS target platform
     * @return Key,value pair of the app specific capabilities
     */
    private static Map<String, Object> getAppPropertiesAsMap(String appID, PlatformOS platformOS) {
        Map<String, Object> applicationCapabilities = new HashMap<String, Object>();
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
                    .map(app -> mapper.convertValue(app.getProperties(), new TypeReference<Map<String, Object>>() {}))
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

        LOG.info("Parsed options for the app from applications.json: " + Collections.singletonList(applicationCapabilities));
        //System.out.println(Collections.singletonList(applicationCapabilities));
        return applicationCapabilities;
    }


}
