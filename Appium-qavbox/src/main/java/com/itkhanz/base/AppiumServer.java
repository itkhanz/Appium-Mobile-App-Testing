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
        LOG.info("***********************************************************************");
        LOG.info("******************** STARTING APPIUM SERVER ***************************");
        LOG.info("***********************************************************************");
        getInstance().start();
        //System.out.println("Appium server started");
        LOG.info("Appium server started: " + server.getUrl());
    }

    public static void stop(){
        if(server != null){
            getInstance().stop();
            //System.out.println("Appium server stopped");
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
                .withAppiumJS(new File(serverProperties.getProperty("appium_path")))    //Tell serviceBuilder where Appium is installed. Or set this path in an environment variable named APPIUM_PATH
                .usingDriverExecutable(new File(serverProperties.getProperty("node_path"))) // Tell serviceBuilder where node is installed. Or set this path in an environment variable named NODE_PATH
                .withIPAddress(ipAddress)
                .usingPort(port)
                //.usingAnyFreePort() //Use any port, in case the default 4723 is already taken (maybe by another Appium server). Use either usingPort(port) or this.
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
