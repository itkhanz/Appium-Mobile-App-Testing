package com.itkhanz.practice.utils;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Pause;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;

import java.time.Duration;
import java.util.Collections;

public class GestureUtils {
    /*
    Finds the center of the element
     */
    private static Point getCenterOfElement(Point location, Dimension size) {
        return new Point(location.getX() + size.getWidth() / 2,
                location.getY() + size.getHeight() / 2);
    }

    /**
     * Performs tap on the element.
     * @param driver AppiumDriver
     * @param element WebElement to perform tap on
     */
    public static void tap(AppiumDriver driver, WebElement element) {
        Point location = element.getLocation();     //returns the top left coordinates of the element on page
        Dimension size = element.getSize();         //returns the width and length of element

        //Find the center of element to perform tap on.
        Point centerOfElement = getCenterOfElement(location, size);

        //models a pointer input device, like mouse, pen, touch
        PointerInput finger1 = new PointerInput(PointerInput.Kind.TOUCH, "finger1");

        //performs a sequence of actions for a given input source
        Sequence sequence = new Sequence(finger1, 1)
                .addAction(finger1.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), centerOfElement))   //Move finger to the center of element
                .addAction(finger1.createPointerDown(PointerInput.MouseButton.LEFT.asArg()))                            //Press finger
                .addAction(new Pause(finger1, Duration.ofMillis(200)))                                                  //wait for few milliseconds
                .addAction(finger1.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));                             //take off finger

        //perform() accepts a collections of sequences
        driver.perform(Collections.singletonList(sequence));
    }
}
