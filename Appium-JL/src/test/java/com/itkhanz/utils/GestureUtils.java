package com.itkhanz.utils;

import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Pause;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;

import java.time.Duration;
import java.util.Arrays;
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
     * Performs tap on the given coordinates
     * @param driver AppiumDriver
     * @param point coordinates of the point
     */
    public static void tap(AppiumDriver driver,Point point) {

        //models a pointer input device, like mouse, pen, touch
        PointerInput finger1 = new PointerInput(PointerInput.Kind.TOUCH, "finger1");

        //performs a sequence of actions for a given input source
        Sequence sequence = new Sequence(finger1, 1)
                .addAction(finger1.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), point.x, point.y))   //Move finger to the center of element
                .addAction(finger1.createPointerDown(PointerInput.MouseButton.LEFT.asArg()))                            //Press finger
                .addAction(new Pause(finger1, Duration.ofMillis(200)))                                                  //wait for few milliseconds
                .addAction(finger1.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));                             //take off finger

        //perform() accepts a collections of sequences
        driver.perform(Collections.singletonList(sequence));
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

    /**
     * Performs double tap on the element (quickly tap twice)
     * @param driver AppiumDriver
     * @param element WebElement to perform action on
     */
    public static void doubleTap(AppiumDriver driver, WebElement element) {
        Point location = element.getLocation();     //returns the top left coordinates of the element on page
        Dimension size = element.getSize();         //returns the width and length of element

        //Find the center of element to perform action on
        Point centerOfElement = getCenterOfElement(location, size);

        //models a pointer input device, like mouse, pen, touch
        PointerInput finger1 = new PointerInput(PointerInput.Kind.TOUCH, "finger1");

        //performs a sequence of actions for a given input source
        Sequence sequence = new Sequence(finger1, 1)
                .addAction(finger1.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), centerOfElement))   //Move finger to the center of element
                .addAction(finger1.createPointerDown(PointerInput.MouseButton.LEFT.asArg()))                            //Press finger
                .addAction(new Pause(finger1, Duration.ofMillis(100)))                                                  //wait for few milliseconds
                .addAction(finger1.createPointerUp(PointerInput.MouseButton.LEFT.asArg()))                              //take off finger
                .addAction(new Pause(finger1, Duration.ofMillis(300)))                                                  //wait for few milliseconds
                .addAction(finger1.createPointerDown(PointerInput.MouseButton.LEFT.asArg()))                            //Press finger
                .addAction(new Pause(finger1, Duration.ofMillis(100)))                                                  //wait for few milliseconds
                .addAction(finger1.createPointerUp(PointerInput.MouseButton.LEFT.asArg()))                              //take off finger
                ;

        //perform() accepts a collections of sequences
        driver.perform(Collections.singletonList(sequence));
    }

    /**
     * Performs long press on the element.
     * @param driver AppiumDriver
     * @param element WebElement to perform action on
     */
    public static void longPress(AppiumDriver driver, WebElement element) {
        Point location = element.getLocation();     //returns the top left coordinates of the element on page
        Dimension size = element.getSize();         //returns the width and length of element

        //Find the center of element to perform action on
        Point centerOfElement = getCenterOfElement(location, size);

        //models a pointer input device, like mouse, pen, touch
        PointerInput finger1 = new PointerInput(PointerInput.Kind.TOUCH, "finger1");

        //performs a sequence of actions for a given input source
        Sequence sequence = new Sequence(finger1, 1)
                .addAction(finger1.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), centerOfElement))   //Move finger to the center of element
                .addAction(finger1.createPointerDown(PointerInput.MouseButton.LEFT.asArg()))                            //Press finger
                .addAction(new Pause(finger1, Duration.ofSeconds(2)))                                                   //wait for few seconds
                .addAction(finger1.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));                             //take off finger

        //perform() accepts a collections of sequences
        driver.perform(Collections.singletonList(sequence));
    }

    /**
     * Performs pinch gesture on the element (equivalent of Zooming In).
     * @param driver AppiumDriver
     * @param element WebElement to perform action on
     */
    public static void zoomIn(AppiumDriver driver, WebElement element) {
        Point location = element.getLocation();     //returns the top left coordinates of the element on page
        Dimension size = element.getSize();         //returns the width and length of element

        //Find the center of element to perform action on
        Point centerOfElement = getCenterOfElement(location, size);

        //models a pointer input device, like mouse, pen, touch
        PointerInput finger1 = new PointerInput(PointerInput.Kind.TOUCH, "finger1");
        PointerInput finger2 = new PointerInput(PointerInput.Kind.TOUCH, "finger2");

        //performs a sequence of actions for a given input source
        Sequence sequence1 = new Sequence(finger1, 1)
                .addAction(finger1.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), centerOfElement))   //Move finger to the center of element
                .addAction(finger1.createPointerDown(PointerInput.MouseButton.LEFT.asArg()))                            //Press finger
                .addAction(new Pause(finger1, Duration.ofMillis(200)))                                                   //wait for few seconds
                .addAction(finger1.createPointerMove(Duration.ofMillis(200),                                            //Move the finger to the right in x-direction, and up in y-direction
                        PointerInput.Origin.viewport(), centerOfElement.getX() + 300,
                        centerOfElement.getY() - 300))
                .addAction(finger1.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));                             //take off finger

        Sequence sequence2 = new Sequence(finger2, 1)
                .addAction(finger2.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), centerOfElement))
                .addAction(finger2.createPointerDown(PointerInput.MouseButton.LEFT.asArg()))
                .addAction(new Pause(finger2, Duration.ofMillis(200)))
                .addAction(finger2.createPointerMove(Duration.ofMillis(200),                                            //Move the finger to the left in x-direction, and bottom in y-direction
                        PointerInput.Origin.viewport(), centerOfElement.getX() - 300,
                        centerOfElement.getY() + 300))
                .addAction(finger2.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

        driver.perform(Arrays.asList(sequence1, sequence2));
    }

    /**
     * Performs pinch gesture on the element (equivalent of Zooming Out).
     * @param driver AppiumDriver
     * @param element WebElement to perform action on
     */
    public static void zoomOut(AppiumDriver driver, WebElement element) {
        Point location = element.getLocation();     //returns the top left coordinates of the element on page
        Dimension size = element.getSize();         //returns the width and length of element

        //Find the center of element to perform action on
        Point centerOfElement = getCenterOfElement(location, size);

        //models a pointer input device, like mouse, pen, touch
        PointerInput finger1 = new PointerInput(PointerInput.Kind.TOUCH, "finger1");
        PointerInput finger2 = new PointerInput(PointerInput.Kind.TOUCH, "finger2");

        //performs a sequence of actions for a given input source
        Sequence sequence1 = new Sequence(finger1, 1)
                .addAction(finger1.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(),                     //Move the finger to the right in x-direction, and up in y-direction
                        centerOfElement.getX() + 300,
                        centerOfElement.getY() - 300))
                .addAction(finger1.createPointerDown(PointerInput.MouseButton.LEFT.asArg()))                            //Press finger
                .addAction(new Pause(finger1, Duration.ofMillis(200)))                                                   //wait for few seconds
                .addAction(finger1.createPointerMove(Duration.ofMillis(1000), PointerInput.Origin.viewport(), centerOfElement))  //Move to the center
                .addAction(finger1.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));                             //take off finger


        Sequence sequence2 = new Sequence(finger2, 2)
                .addAction(finger2.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(),                     //Move the finger to the left in x-direction, and bottom in y-direction
                        centerOfElement.getX() - 300,
                        centerOfElement.getY() + 300))
                .addAction(finger2.createPointerDown(PointerInput.MouseButton.LEFT.asArg()))                            //Press finger
                .addAction(new Pause(finger2, Duration.ofMillis(200)))                                                   //wait for few seconds
                .addAction(finger2.createPointerMove(Duration.ofMillis(1000), PointerInput.Origin.viewport(), centerOfElement))  //Move to the center
                .addAction(finger2.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));                             //take off finger

        driver.perform(Arrays.asList(sequence1, sequence2));
    }

    /**
     * Performs Scrolling from the center of screen
     * @param driver AppiumDriver
     * @param direction String UP, DOWN, LEFT, RIGHT
     */
    public static void scroll(AppiumDriver driver, String direction) {

        Dimension size = driver.manage().window().getSize();         //returns the size of window

        //calculate the coordinates for scrolling from center of the screen in direction
        int startX = size.getWidth() / 2;
        int startY = size.getHeight() / 2;
        int endX;
        int endY;
        switch (direction.toUpperCase()) {
            case "DOWN":
                endX = startX;
                endY = startY - 200;
                break;
            case "UP":
                endX = startX;
                endY = startY + 200;
                break;
            case "LEFT":
                endX = startX + 200;
                endY = startY;
                break;
            case "RIGHT":
                endX = startX - 200;
                endY = startY;
                break;
            default:
                throw new RuntimeException("Invalid scroll direction: " + direction + " .Valid directions are UP, DOWN, LEFT, RIGHT");
        }

        //models a pointer input device, like mouse, pen, touch
        PointerInput finger1 = new PointerInput(PointerInput.Kind.TOUCH, "finger1");

        //performs a sequence of actions for a given input source
        Sequence sequence = new Sequence(finger1, 1)
                .addAction(finger1.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), startX, startY))        //Move finger to the center of screen
                .addAction(finger1.createPointerDown(PointerInput.MouseButton.LEFT.asArg()))                                //Press finger
                .addAction(new Pause(finger1, Duration.ofMillis(100)))                                                      //wait for few seconds
                .addAction(finger1.createPointerMove(Duration.ofMillis(100), PointerInput.Origin.viewport(), endX, endY))   //Move the finger to the scroll direction towards end point
                .addAction(finger1.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));                                 //take off finger

        //perform() accepts a collections of sequences
        driver.perform(Collections.singletonList(sequence));
    }

    /**
     * Performs Swipe from the center of screen
     * @param driver AppiumDriver
     * @param direction String UP, DOWN, LEFT, RIGHT
     */
    public static void swipe(AppiumDriver driver, String direction) {

        Dimension size = driver.manage().window().getSize();         //returns the size of window

        //calculate the coordinates for swiping from center of the screen in direction
        int startX = size.getWidth() / 2;
        int startY = size.getHeight() / 2;
        int endX;
        int endY;
        switch (direction.toUpperCase()) {
            case "DOWN":
                endX = startX;
                endY = startY + 200;
                break;
            case "UP":
                endX = startX;
                endY = startY - 200;
                break;
            case "LEFT":
                endX = startX - 200;
                endY = startY;
                break;
            case "RIGHT":
                endX = startX + 200;
                endY = startY;
                break;
            default:
                throw new RuntimeException("Invalid scroll direction: " + direction + " .Valid directions are UP, DOWN, LEFT, RIGHT");
        }

        //models a pointer input device, like mouse, pen, touch
        PointerInput finger1 = new PointerInput(PointerInput.Kind.TOUCH, "finger1");

        //performs a sequence of actions for a given input source
        Sequence sequence = new Sequence(finger1, 1)
                .addAction(finger1.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), startX, startY))        //Move finger to the center of screen
                .addAction(finger1.createPointerDown(PointerInput.MouseButton.LEFT.asArg()))                                //Press finger
                .addAction(finger1.createPointerMove(Duration.ofMillis(50), PointerInput.Origin.viewport(), endX, endY))   //Move the finger to the quickly to swipe
                .addAction(finger1.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));                                 //take off finger

        //perform() accepts a collections of sequences
        driver.perform(Collections.singletonList(sequence));
    }

    /**
     * Drags the target element and drops it on to the source element
     * @param driver AppiumDriver
     * @param sourceElement element to drag
     * @param targetElement element to drop on to
     */
    public static void dragAndDrop(AppiumDriver driver, WebElement sourceElement, WebElement targetElement) {

        //Find the center of element to perform action on
        Point sourceElementCenter = getCenterOfElement(sourceElement.getLocation(), sourceElement.getSize());
        Point targetElementCenter = getCenterOfElement(targetElement.getLocation(), targetElement.getSize());

        //models a pointer input device, like mouse, pen, touch
        PointerInput finger1 = new PointerInput(PointerInput.Kind.TOUCH, "finger1");

        //performs a sequence of actions for a given input source
        Sequence sequence = new Sequence(finger1, 1)
                .addAction(finger1.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), sourceElementCenter))   //Move finger to the center of source element
                .addAction(finger1.createPointerDown(PointerInput.MouseButton.LEFT.asArg()))                                //Press finger
                .addAction(new Pause(finger1, Duration.ofMillis(500)))                                                      //pause for few milliseconds
                .addAction(finger1.createPointerMove(Duration.ofMillis(500), PointerInput.Origin.viewport(), targetElementCenter))   //Move the finger to the quickly to swipe
                .addAction(finger1.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));                                 //take off finger

        //perform() accepts a collections of sequences
        driver.perform(Collections.singletonList(sequence));
    }
}
