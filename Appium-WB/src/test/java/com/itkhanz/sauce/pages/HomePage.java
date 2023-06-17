package com.itkhanz.sauce.pages;

import com.itkhanz.sauce.objects.ProductItem;
import io.appium.java_client.AppiumDriver;
import lombok.Getter;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;

import static io.appium.java_client.AppiumBy.accessibilityId;
import static io.appium.java_client.AppiumBy.className;
import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated;

@Getter
public class HomePage<D extends AppiumDriver> extends BasePage<D> {
    public HomePage(final D driver) {
        super(driver);
    }

    public WebElement cart() {
        var cart = accessibilityId("test-Cart");
        return getWait().until(visibilityOfElementLocated(cart));
    }

    public int cartCount() {
        try {
            var countText = cart().findElement(className("android.widget.TextView")).getText();
            return Integer.parseInt(countText);
        } catch (NoSuchElementException e) {
            System.out.println("Cart is Empty");
            return 0;
        }
    }

    public WebElement cartDropZone() {
        final var cartDropZone = accessibilityId("test-Cart drop zone");
        return getWait().until(visibilityOfElementLocated(cartDropZone));
    }

    public ProductItem productItem(final String title) {
        final var productList = getWait().until(visibilityOfElementLocated(accessibilityId("test-PRODUCTS")));
        final var items = productList.findElements(accessibilityId("test-Item"))
                .stream()
                .map(ProductItem::new)
                .filter(item -> item.title()
                        .equalsIgnoreCase(title))
                .findFirst();
        return items.orElse(null);
    }
}
