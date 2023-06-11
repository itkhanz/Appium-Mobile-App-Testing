package com.itkhanz.tests;

import com.itkhanz.base.AppiumServer;
import com.itkhanz.base.DriverManager;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;

import static com.itkhanz.base.DriverManager.getDriver;

public class BaseTest {
    @BeforeTest
    public void startup() {
        AppiumServer.start();
    }

    @AfterTest(alwaysRun = true)
    public void teardown() {
        if (null != getDriver()) {
            getDriver().quit();
            DriverManager.removeDriverThreadValue();
        }
        AppiumServer.stop();
    }
}
