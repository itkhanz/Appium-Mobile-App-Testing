package com.itkhanz.base;

import org.openqa.selenium.WebDriver;

public class DriverManager {
    private static final ThreadLocal<WebDriver> driver = new ThreadLocal<>();

    public static WebDriver getDriver(){
        return driver.get();
    }

    public static void setDriver(WebDriver Driver){
        driver.set(Driver);
        System.out.println("Driver is set");
    }

    public static void removeDriverThreadValue() {
        driver.remove();
        System.out.println("Driver is removed");
    }
}
