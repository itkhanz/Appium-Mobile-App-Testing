package com.itkhanz.base;

import io.appium.java_client.AppiumDriver;

/*
AndroidDriverManager and IOSDriverManager implements this interface with AndroidDriver and IOSDriver respectively with platform specific capabilities
 */
public interface IDriverManager <D extends AppiumDriver> {
    D getDriver();
}
