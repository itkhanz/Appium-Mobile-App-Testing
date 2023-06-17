package com.itkhanz.sauce.objects;

import lombok.AllArgsConstructor;
import org.openqa.selenium.WebElement;

import static io.appium.java_client.AppiumBy.accessibilityId;

@AllArgsConstructor
public class CartItem {
    private final WebElement container;

    public WebElement description () {
        return this.container.findElement (accessibilityId ("test-Description"));
    }
}
