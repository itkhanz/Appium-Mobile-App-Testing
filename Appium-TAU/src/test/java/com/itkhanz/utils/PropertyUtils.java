package com.itkhanz.utils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class PropertyUtils {
    private static Properties deviceProps;
    private static Properties appProps;
    /**
     * This method loads the properties file and handles different exceptions
     * @param filePath file path where properties are defined
     * @return loaded Properties object
     */
    private static Properties propertyLoader(String filePath) {
        Properties properties = new Properties();
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(filePath));  //Reading properties file
            try {
                properties.load(reader);    //Reads property list as key-value pair
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException("failed to load properties file "+ filePath);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("properties file not found at " + filePath);
        }
        return properties;
    }

    public static String getDeviceProp(String property) {
        if (deviceProps == null) {
            deviceProps = propertyLoader("src/test/resources/config.properties");
        }
        //deviceProps.forEach((k, v) -> System.out.println("Key : " + k + ", Value : " + v));
        if (deviceProps.containsKey(property)) {
            return deviceProps.getProperty(property);
        }
        throw new IllegalArgumentException(property + " does not exist in config.properties");
    }

    public static String getAppProp(String property) {
        if (appProps == null) {
            appProps = propertyLoader("src/test/resources/app.properties");
        }
        if (appProps.containsKey(property)) {
            return appProps.getProperty(property);
        }
        throw new IllegalArgumentException(property + " does not exist in app.properties");
    }
}
