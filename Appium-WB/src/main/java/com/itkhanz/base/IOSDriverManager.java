package com.itkhanz.base;

import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.options.XCUITestOptions;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import lombok.Builder;
import org.openqa.selenium.Capabilities;

import java.nio.file.Path;
import java.text.MessageFormat;
import java.time.Duration;

/*
Implements the IDriverManager<? extends AppiumDriver> with override getDriver()
Creates an instance of the Driver with IOSDriver and capabilities for iOS app
 */
@Builder (builderMethodName = "createDriver", buildMethodName = "create")
public class IOSDriverManager implements IDriverManager<IOSDriver> {
    private static final String USER_DIR = System.getProperty ("user.dir");

    private       String                   appName;
    @Builder.Default
    private       String                   deviceName      = "iPhone 14";
    private       boolean                  isHeadless;
    @Builder.Default
    private final String                   platformVersion = "16.4";
    @Builder.Default
    private final int                   maxTypingFrequency = 60;

    private AppiumDriverLocalService service;

    @Override
    public IOSDriver getDriver () {
        return new IOSDriver (this.service.getUrl (), buildCapabilities ());
    }

    private Capabilities buildCapabilities () {
        final var options = new XCUITestOptions();
        options.setPlatformName ("iOS")
                .setPlatformVersion (this.platformVersion)
                .setDeviceName (this.deviceName)
                .setApp (Path.of (USER_DIR, MessageFormat.format ("src/test/resources/apps/{0}.app.zip", this.appName))
                        .toString ())
                .setAutoAcceptAlerts (true)
                .setFullReset (false)
                .setIsHeadless (this.isHeadless)
                .setMaxTypingFrequency(this.maxTypingFrequency)
        ;
        return options;
    }
}
