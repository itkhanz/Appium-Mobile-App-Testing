package com.itkhanz.constants;

import java.io.File;

public class Paths {
    public static final String BASE_DIR = System.getProperty("user.dir")
            + File.separator + "src"
            + File.separator + "main"
            + File.separator + "resources"
            ;

    public static final String TEST_DIR = System.getProperty("user.dir")
            + File.separator + "src"
            + File.separator + "test"
            + File.separator + "resources"
            ;

    public static final String APPS_DIR = BASE_DIR
            + File.separator + "apps"
            + File.separator
            ;

    public static final String TEST_DATA_DIR = TEST_DIR
            + File.separator + "test-data"
            + File.separator
            ;
}
