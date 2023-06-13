package com.itkhanz.tests;

import com.itkhanz.base.AppiumServer;
import com.itkhanz.base.DriverManager;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;

public class BaseTest {
    @BeforeTest
    public void startup() {
        AppiumServer.start();
    }

    @AfterTest(alwaysRun = true)
    public void teardown() {
        DriverManager.removeDriver();
        AppiumServer.stop();
    }
}
