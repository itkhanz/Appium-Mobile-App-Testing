package com.itkhanz.base;

import com.itkhanz.utils.PropertyUtils;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.GeneralServerFlag;

import java.io.File;
import java.util.Properties;

public class AppiumServer {
    static AppiumDriverLocalService server;

    public static void main(String []args) throws InterruptedException {
        AppiumServer.start();
        Thread.sleep(2000);
        AppiumServer.stop();
    }

    public static void start(){
        getInstance().start();
        System.out.println(server.getUrl());
        System.out.println(server.isRunning());
    }

    public static void stop(){
        if(server != null){
            getInstance().stop();
            System.out.println("Appium server stopped");
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
                .withAppiumJS(new File("/usr/local/lib/node_modules/appium/build/lib/main.js"))
                .usingDriverExecutable(new File("/usr/local/bin/node"))
                .usingPort(port)
                .withArgument(GeneralServerFlag.LOCAL_TIMEZONE)
                .withLogFile(new File("Appiumlog.txt"))
                .withIPAddress(ipAddress);
        //.withArgument(GeneralServerFlag.BASEPATH, "wd/hub")
        server = AppiumDriverLocalService.buildService(builder);
        //server.start();
        //System.out.println(server.getUrl());
        //System.out.println(server.isRunning());
        //server.stop();
    }

}
