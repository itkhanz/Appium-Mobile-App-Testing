package com.itkhanz.sauce.pages;

import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.WebElement;

import static io.appium.java_client.AppiumBy.accessibilityId;
import static io.appium.java_client.AppiumBy.className;
import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated;

public class ProductDetailPage<D extends AppiumDriver> extends BasePage<D> {
    public ProductDetailPage(final D driver) {
        super(driver);
    }

    public WebElement productImage() {
        return getWait().until(visibilityOfElementLocated(accessibilityId("test-Image Container")));
    }

    public WebElement productDescription() {
        return getWait().until(visibilityOfElementLocated(accessibilityId("test-Description")));
    }

    public String productName() {
        return productDescription()
                .findElements(className("android.widget.TextView"))
                .stream()
                .findFirst()
                .map(WebElement::getText)
                .orElse(null)
                ;

    }
}
