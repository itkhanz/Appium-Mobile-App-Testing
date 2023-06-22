package com.itkhanz.practice.base;

import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.util.Optional;

public class DriverManager {
    private static ThreadLocal<AppiumDriver> driver = new ThreadLocal<AppiumDriver>();


    protected static AppiumDriver getDriver() {
        return driver.get();
    }

    protected static void setDriver(AppiumDriver dr) { driver.set(dr); }

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
}
