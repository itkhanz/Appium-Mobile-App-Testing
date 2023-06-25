package com.itkhanz.utils;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;

public class ContextUtils {

    //TODO implement generic bounded parameters to remove duplicate methods for android and ios

    public static void switchToNativeContext(AndroidDriver driver) {
        driver.context("NATIVE_APP");
    }

    public static void switchToNativeContext(IOSDriver driver) {
        driver.context("NATIVE_APP");
    }

    public static void switchToWebContext(AndroidDriver driver) {
        String context = driver.getContextHandles()
                .stream()
                .filter(handle -> !handle.equals("NATIVE_APP"))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("web context not fund"));
        driver.context(context);
    }

    public static void switchToWebContext(IOSDriver driver) {
        String context = driver.getContextHandles()
                .stream()
                .filter(handle -> !handle.equals("NATIVE_APP"))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("web context not fund"));
        driver.context(context);
    }

    public static void switchToWebContext(AndroidDriver driver, String contextName) {
        String context = driver.getContextHandles()
                .stream()
                .filter(handle -> handle.equals(contextName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(contextName + " context not fund"));
        driver.context(context);
    }

    public static void switchToWebContext(IOSDriver driver, String contextName) {
        String context = driver.getContextHandles()
                .stream()
                .filter(handle -> handle.equals(contextName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(contextName + " context not fund"));
        driver.context(context);
    }
}
