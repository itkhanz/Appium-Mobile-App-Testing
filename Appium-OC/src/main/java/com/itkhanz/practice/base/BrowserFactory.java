package com.itkhanz.practice.base;

import com.itkhanz.practice.constants.Browsers;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.options.XCUITestOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

import static com.itkhanz.practice.constants.Paths.BROWSER_DIR;
import static com.itkhanz.practice.constants.Paths.CHROME_EXEC_DIR;

public class BrowserFactory extends DriverManager {

    public static AppiumDriver initializeDriver(Browsers browser){
        if(DriverManager.getDriver()==null) {
            try {
                new BrowserFactory(browser);
            } catch (Exception e) {
                System.out.println(e.getClass().getSimpleName());
                System.out.println(e.getMessage());
                e.printStackTrace();
                throw new RuntimeException("Failed to initialize the BrowserFactory");
            }
        }
        System.out.println("[ThreadID] " + Thread.currentThread().getId() + " [SessionID] " + ((RemoteWebDriver)getDriver()).getSessionId());
        return DriverManager.getDriver();
    }

    private BrowserFactory(Browsers browser) throws MalformedURLException {
        AppiumDriver driver;
        URL url = new URL("http://127.0.0.1:4723");   //Appium Server URL and port

        switch(browser) {
            case CHROME:
                driver = new AndroidDriver(url, getUiAutomator2Options(browser.browserName));
                break;
            case SAFARI:
                driver = new IOSDriver(url, getXCUITestOptions(browser.browserName));
                break;
            default:
                throw new RuntimeException("Unable to create session with browser: " + browser.browserName);
        }

        System.out.println("[Driver Capabilities] " + driver.getCapabilities().getCapability("appium:desired"));
        DriverManager.setDriver(driver);
    }


    private static UiAutomator2Options getUiAutomator2Options(String browser) {
        return new UiAutomator2Options()
                .setPlatformName("android")
                .setAutomationName("UiAutomator2")
                //.setDeviceName("pixel_5")
                .setUdid("emulator-5554")
                .setAutoGrantPermissions(true)  //Have Appium automatically determine which permissions your app requires and grant them to the app on install.
                .setNewCommandTimeout(Duration.ofSeconds(180))  //How long (in seconds) Appium will wait for a new command from the client before assuming the client quit and ending the session. Setting to 0 will disable the new command timeout. Default 60s
                .setUiautomator2ServerLaunchTimeout(Duration.ofSeconds(60)) //Sometimes it takes too much time to launch uiAutomator2. Defaults to 20s
                .withBrowserName(browser)   //The name of the browser to run the test on. If this capability is provided then the driver will try to start the test in Web context mode.
                //.setChromedriverExecutable(CHROME_EXEC_DIR) //Full path to the chromedriver executable on the server file system. Not recommended if automating multiple devices with different browser versions
                .setChromedriverExecutableDir(BROWSER_DIR)  //Path to chrome driver executables, appium will download and copy the drivers to this directory so the drivers wont get deleted on appium update or reinstall
                ;
    }

    private static XCUITestOptions getXCUITestOptions(String browser) {
        return new XCUITestOptions()
                .setAutomationName("XCuiTest")
                .setPlatformName("iOS")
                .setDeviceName("iPhone 14")
                .setUdid("1E8FE39B-1118-4117-B02B-66A390AECB3F")
                .setAutoAcceptAlerts(true)
                .setNewCommandTimeout(Duration.ZERO)
                .withBrowserName(browser)
                ;
    }
}
