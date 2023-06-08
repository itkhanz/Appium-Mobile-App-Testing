package com.itkhanz.practice.annotations;

import com.itkhanz.practice.constants.Apps;
import com.itkhanz.practice.constants.Platform;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This is a useful annotation for use in conjunction with the test methods to provide any additional information to the test method
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface TestMetaData {
    String category() default "none";
    Apps app() default Apps.APIDEMOS;
    Platform platform() default Platform.ANDROID;
}
