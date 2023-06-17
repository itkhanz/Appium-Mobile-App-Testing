package com.itkhanz.utils;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static com.itkhanz.base.DriverManager.getDriver;

public class WaitUtils {

    public static WebElement waitForElementToBePresent(AppiumDriver driver, By locator, int waitInMillis) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofMillis(waitInMillis));
        return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    public static WebElement waitForElementToBeVisible(AppiumDriver driver, By locator, int waitInMillis) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofMillis(waitInMillis));
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public static WebElement waitForWebelementToBeVisible(AppiumDriver driver, WebElement element, int waitInMillis) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofMillis(waitInMillis));
        return wait.until(ExpectedConditions.visibilityOf(element));
    }
    public static WebElement waitForElementToBeClickable(AppiumDriver driver, By locator, int waitInMillis) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofMillis(waitInMillis));
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    public static WebElement waitForWebelementToBeClickable(AppiumDriver driver, WebElement element, int waitInMillis) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofMillis(waitInMillis));
        return wait.until(ExpectedConditions.elementToBeClickable(element));
    }

}
