package com.itkhanz.wdio.pages;

import io.appium.java_client.AppiumBy;
import lombok.Getter;
import org.openqa.selenium.By;

@Getter
public class HomePage {
    private final By swipeTab = AppiumBy.accessibilityId("Swipe");
}
