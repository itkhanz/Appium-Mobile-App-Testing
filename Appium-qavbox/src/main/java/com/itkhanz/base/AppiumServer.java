package com.itkhanz.base;

import com.itkhanz.utils.PropertyUtils;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.time.Duration;
import java.util.Properties;

import static io.appium.java_client.service.local.flags.GeneralServerFlag.*;

public class AppiumServer {
    static AppiumDriverLocalService server;
    private static final Logger LOG = LoggerFactory.getLogger("AppiumServer.class");

    public static void start(){
        getInstance().start();
        System.out.println(server.getUrl());
        System.out.println(server.isRunning());
        System.out.println("Appium server started");
        LOG.info("Appium server started: " + server.getUrl());
    }

    public static void stop(){
        if(server != null){
            getInstance().stop();
            System.out.println("Appium server stopped");
            LOG.info("Appium server stopped");
        }
    }

    static AppiumDriverLocalService getInstance(){
        if(server == null){
            setInstance();
        }
        return server;
    }

    static void setInstance(){
        Properties serverProperties = PropertyUtils.propertyLoader("src/test/resources/server.properties");
        String ipAddress = serverProperties.getProperty("ip").split("http://")[1];
        int port = Integer.parseInt(serverProperties.getProperty("port"));

        AppiumServiceBuilder builder = new AppiumServiceBuilder();
        builder
                .withAppiumJS(new File(serverProperties.getProperty("appium_path")))
                .usingDriverExecutable(new File(serverProperties.getProperty("node_path")))
                .withIPAddress(ipAddress)
                .usingPort(port)
                .withArgument(LOCAL_TIMEZONE)
                .withArgument(USE_DRIVERS, serverProperties.getProperty("use_drivers"))
                .withArgument(USE_PLUGINS, serverProperties.getProperty("use_plugins"))
                .withLogFile(new File(serverProperties.getProperty("logfile_path")))
                .withTimeout(Duration.ofSeconds(Long.parseLong(serverProperties.getProperty("server_timeout"))))
                //.withArgument(LOG_LEVEL, "info")
                //.withArgument(SESSION_OVERRIDE)
                //.withArgument(ALLOW_INSECURE, "chromedriver_autodownload")
                ;
        server = AppiumDriverLocalService.buildService(builder);
    }

}
