package com.itkhanz.base;

import com.itkhanz.constants.PlatformOS;
import com.itkhanz.utils.PropertyUtils;

import java.util.Properties;

public class PropertiesManager {
    /**
     * Returns the Udid of the device to be initialized for appium session
     * @param udid string udid from the test case
     * @param platformOS Platform Android or iOS
     * @return string udid of the device to be initialzed
     */
    public static String getDeviceUdid(String udid, PlatformOS platformOS) {
        if (udid != null && !udid.isBlank()) {
            return udid;
        }

        //If no udid is provided then the default device from properties file are setup for emulator/simulator
        return getDeviceProperties(platformOS).getProperty("UDID"); //device specific capabilities
    }

    /**
     * Concatenate the IP with port. If port is not specified, then default port is selected from properties file
     * @return formatted string for Appium server URL e.g. http://localhost:4723
     */
    public static String getFormattedUrl() {
        Properties serverProperties = PropertyUtils.propertyLoader("src/test/resources/server.properties");
        return serverProperties.getProperty("ip") + ":" + serverProperties.getProperty("port");
    }

    /**
     * Loads the platform specific properties for device such as udid, device name
     * @param platformOS
     * @return device properties
     */
    public static Properties getDeviceProperties(PlatformOS platformOS) {
        if (platformOS.platformName.equalsIgnoreCase("ANDROID")) {
            return PropertyUtils.propertyLoader("src/test/resources/android.properties");
        }
        return PropertyUtils.propertyLoader("src/test/resources/ios.properties");
    }
}
