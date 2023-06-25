package com.itkhanz.constants;

import java.io.File;

public class Paths {
    public static final String BASE_DIR = System.getProperty("user.dir")
            + File.separator + "src"
            + File.separator + "main"
            + File.separator + "resources"
            ;

    public static final String BROWSER_DIR = BASE_DIR
            + File.separator + "browsers"
            + File.separator
            ;

    public static final String APPS_DIR = BASE_DIR
            + File.separator + "apps"
            + File.separator
            ;
}
