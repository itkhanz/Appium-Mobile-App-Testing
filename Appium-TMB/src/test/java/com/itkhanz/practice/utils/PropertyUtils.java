package com.itkhanz.practice.utils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class PropertyUtils {

    /**
     * This method loads the properties file and handles different exceptions
     * @param filePath file path where properties are defined
     * @return loaded Properties object
     */
    public static Properties propertyLoader(String filePath) {
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
}
