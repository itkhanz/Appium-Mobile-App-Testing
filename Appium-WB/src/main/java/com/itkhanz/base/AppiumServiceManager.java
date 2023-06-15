package com.itkhanz.base;

import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.GeneralServerFlag;
import lombok.Builder;

import java.nio.file.Path;
import java.text.MessageFormat;

import static io.appium.java_client.service.local.flags.GeneralServerFlag.*;

/*
This class is responsible for creating the serb
 */
@Builder(builderMethodName = "composeService", buildMethodName = "composed")
public class AppiumServiceManager {
    private static final String USER_DIR = System.getProperty("user.dir");

    @Builder.Default
    private String driverName = "uiautomator2";
    @Builder.Default
    private String host = "127.0.0.1";
    @Builder.Default
    private int port = 4723;

    public AppiumDriverLocalService buildService() {
        final var logFile = Path.of(USER_DIR, "logs", MessageFormat.format("appium-{0}.log", this.driverName)).toFile();

        final var builder = new AppiumServiceBuilder();

        return builder
                .withIPAddress(this.host)
                .usingPort(this.port)
                .withLogFile(logFile)
                .withArgument(BASEPATH, "/wd/hub")
                .withArgument(LOG_LEVEL, "info")
                .withArgument(USE_DRIVERS, this.driverName)
                .withArgument(SESSION_OVERRIDE)
                .withArgument(LOCAL_TIMEZONE)
                .build()
                ;
    }
}
