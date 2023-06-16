package com.itkhanz.base;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import lombok.Builder;
import org.openqa.selenium.Capabilities;

import java.nio.file.Path;
import java.text.MessageFormat;
import java.time.Duration;

/*
Implements the IDriverManager<? extends AppiumDriver> with override getDriver()
Creates an instance of the Driver with AndroidDriver and capabilities for Android app
 */
@Builder(builderMethodName = "createDriver", buildMethodName = "create")
public class AndroidDriverManager implements IDriverManager<AndroidDriver> {
    private static final String USER_DIR = System.getProperty ("user.dir");

    private String                   appName;
    @Builder.Default
    private String                   deviceName      = "Pixel_5";
    private boolean                  isHeadless;
    @Builder.Default
    private String                   platformVersion = "13";
    private AppiumDriverLocalService service;
    private String                   waitActivity;

    @Override
    public AndroidDriver getDriver () {
        return new AndroidDriver (this.service.getUrl (), buildCapabilities (this.appName));
    }

    private Capabilities buildCapabilities (final String appName) {
        final var options = new UiAutomator2Options();
        options.setPlatformName ("Android")
                .setPlatformVersion (this.platformVersion)
                .setDeviceName (this.deviceName)
                .setAvd (this.deviceName)
                .setApp (Path.of (USER_DIR, MessageFormat.format ("src/test/resources/apps/{0}.apk", appName))
                        .toString ())
                .setAppWaitActivity (this.waitActivity)
                .setAutoGrantPermissions (true)
                .setFullReset (false)
                .setIsHeadless (this.isHeadless)
                .setAdbExecTimeout(Duration.ofMillis(60000))    //Timeout in milliseconds used to wait for adb command execution. Defaults to 20000
                .setCapability ("appium:settings[ignoreUnimportantViews]", true);
        return options;
    }
}
