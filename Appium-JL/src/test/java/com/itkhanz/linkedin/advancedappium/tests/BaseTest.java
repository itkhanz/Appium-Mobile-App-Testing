package com.itkhanz.linkedin.advancedappium.tests;

import com.itkhanz.base.DriverManager;
import org.testng.annotations.AfterMethod;

public class BaseTest {

    @AfterMethod(alwaysRun = true)
    protected void tearDown() {
        DriverManager.shutdownDriver();
    }

}
