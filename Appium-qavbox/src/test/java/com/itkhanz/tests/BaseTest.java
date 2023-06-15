package com.itkhanz.tests;

import com.itkhanz.base.AppiumServer;
import com.itkhanz.base.DriverManager;
import org.testng.annotations.*;

public class BaseTest {

    //Comment out the BeforeSuite and AfterSuite methods if launching the server manually from CMD
    //Uncomment if you want to launch the appium server programmatically
    /*@BeforeClass
    public void startServer() {
        AppiumServer.start();
    }

    @AfterClass(alwaysRun = true)
    public void stopServer() {
        AppiumServer.stop();
    }*/

    @AfterTest(alwaysRun = true)
    public void teardown() {
        DriverManager.removeDriver();
    }
}
