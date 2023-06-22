package com.itkhanz.practice.utils;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ElementUtils {
    public static boolean isNotDisplayed(final By locator, final WebDriverWait wait) {
        try {
            return !wait.until(ExpectedConditions.visibilityOfElementLocated(locator)).isDisplayed();
        } catch (final TimeoutException e) {
            return true;
        }
    }
}
