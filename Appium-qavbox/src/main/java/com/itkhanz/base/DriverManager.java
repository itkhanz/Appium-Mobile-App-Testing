package com.itkhanz.base;

import io.appium.java_client.AppiumDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

public class DriverManager {
    private static final ThreadLocal<AppiumDriver> driver = new ThreadLocal<>();
    private static final Logger LOG = LoggerFactory.getLogger("DriverManager.class");
    //Logger logger = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger("com.itkhanz");   //can also use custom log appender with different logging level

    public static AppiumDriver getDriver(){
        return driver.get();
    }

    public static void setDriver(AppiumDriver Driver){
        driver.set(Driver);
        LOG.info("Driver is initialized");
        //logger.info("This is logged from logger");
        //System.out.println("Driver is set");
    }

    public static void removeDriver() {
        if (getDriver() != null) {
            getDriver().quit(); //closing the driver session
            LOG.info("Driver Session is closed");
            driver.remove();    //removing driver thread value
            LOG.info("Driver thread value is removed");
            //System.out.println("Driver is removed");
        }
    }

    public static void setupDriverTimeouts() {
        getDriver().manage()
                .timeouts()
                .implicitlyWait(Duration.ofSeconds(5));
    }
}
