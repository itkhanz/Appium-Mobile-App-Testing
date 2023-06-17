package com.itkhanz.utils;

import io.appium.java_client.AppiumDriver;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Pause;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;

import java.text.MessageFormat;
import java.time.Duration;
import java.util.Collections;

import static java.text.MessageFormat.format;
import static java.util.Collections.singletonList;

//This class is instantiate din test class with the instance of AndroidDriver or IOSDriver
//Driver is required to perform the sequence actions therefore the @AllArgsConstructor annotation from lombok will create the default constructor with driver
@AllArgsConstructor
public class FingerGestureUtils<D extends AppiumDriver> {
    private final D driver;

    //lombok annotations create the getter and constructor automatically
    //x and y coordinates are used to add/subtract the px based on swipe direction for the end point
    @Getter
    @AllArgsConstructor
    public enum Direction {
        LEFT(-1, 0),
        RIGHT(1, 0),
        UP(0, -1),
        DOWN(0, 1);

        private final int x;
        private final int y;
    }

    private static final String FINGER_1 = "Finger 1";
    private static final String FINGER_2 = "Finger 2";


    //Returns screen dimensions (Height and width)
    //Used in calculating the swipe position relative to the screen size
    private Dimension getScreenSize() {
        return this.driver.manage()
                .window().getSize();
    }

    /**
     * This method is used to update the center point of the element if it is outside the scree bounds a e.g. In Swipe action
     * In some cases, the element might be located at the edge of screen so its center could go out of the screen
     * To adjust for these edge cases, we subtract an offset of 5 from center coordinates
     *
     * @param element WebElement
     * @param point   center point of the WebElement
     * @return center point of the element with offset applied
     */
    private Point getCorrectedCoordinates(final WebElement element, final Point point) {
        final var screenSize = getScreenSize();
        var x = point.getX();
        var y = point.getY();
        var w = screenSize.getHeight();
        var h = screenSize.getHeight();

        //If the element is not null (e.g. when we want to tap/swipe within bounds of the element)
        //then instead of the entire screen, height and width is calculated relative to element
        if (element != null) {
            final var size = element.getSize();
            final var location = element.getLocation();
            w = size.getWidth() + location.getX();
            h = size.getHeight() + location.getY();
        }
        //center point beyond the screen width in x direction
        if (x >= w) {
            x = w - 5;  //subtract 5 px from the X
        }
        //center point beyond the screen height in bottom direction
        if (y >= h) {
            y = h - 5;  //subtract 5 px from the Y
        }
        //center point beyond the screen width in left direction
        if (x < 0) {
            x = 5;  //Bring the X to 5 px (starting from left)
        }
        //center point beyond the screen height in top direction
        if (y < 0) {
            y = 5;  //Bring the Y to 5 px (starting from top)
        }

        return new Point(x, y);
    }

    //Returns the center point of element based on its location and size
    private final Point getElementCenter(final WebElement element) {
        final var location = element.getLocation(); //top left point of the element
        final var size = element.getSize();
        final var x = (size.getWidth() / 2) + location.getX();
        final var y = (size.getHeight() / 2) + location.getY();

        return getCorrectedCoordinates(element, new Point(x, y));
    }

    //Prints the screen dimensions to console
    private void printDimension(final String type, final Dimension dimension) {
        System.out.println(format("{0}: [w: {1}, h: {2}]", type, dimension.getWidth(), dimension.getHeight()));
    }

    //Prints the coordinates to console (e.g. center of point, swipe start point, swipe end point)
    private void printPoint(final String type, final Point point) {
        System.out.println(format("{0}: [x: {1}, y: {2}]", type, point.getX(), point.getY()));
    }

    //returns the center point of swipe beginning
    //if element is null the center of entire screen is swipe start point
    //else the center of element is swipe start position
    private Point getSwipeStartPosition(final WebElement element) {
        final var screenSize = getScreenSize();
        var x = screenSize.getWidth() / 2;
        var y = screenSize.getHeight() / 2;
        if (element != null) {
            final var point = getElementCenter(element);
            x = point.getX();
            y = point.getY();
        }
        return new Point(x, y);
    }

    /**
     * Calculates the Point where swipe gesture should end
     *
     * @param direction Direction of the swipe (with X and Y for Dimensions since enum is annotated with @Getter)
     * @param element   WebElement for the swipe
     * @param distance  How far to swipe in px (must be in range 0-100)
     * @return ending point of the swipe with adjustment applied for any offset beyond screen/element
     */
    private Point getSwipeEndPosition(final Direction direction, final WebElement element, final int distance) {
        verifyDistance(distance);
        final var start = getSwipeStartPosition(element);
        //How much from the start position (center of screen/element) to move finger based on direction and distance
        //e.g. for UP direction,
        // the X coordinate stays same because for UP and DOWN, the x is ZERO in Direction enum
        //the Y coordinate gets subtracted 70% from the start point
        //if start.y is 100 (vertical center), then moving 70 in upward will mean (100 * -1 * 70)/100 = -70
        //and 100 - 70 = 30 (because we subtracted 70% from total vertical position)
        final var x = start.getX() + ((start.getX() * direction.getX() * distance) / 100);
        final var y = start.getY() + ((start.getY() * direction.getY() * distance) / 100);
        return getCorrectedCoordinates(element, new Point(x, y));
    }

    //If the distance is above 100, the end point of the swipe will go out of the screen
    //if the distance is below O, then the calculation will not be correct because multiplication with negative will invert sign
    private void verifyDistance(final int distance) {
        if (distance <= 0 || distance >= 100) {
            throw new IllegalArgumentException("Distance should be between 0 and 100 exclusive...");
        }
    }

    //Returns the sequence of actions for swipe or tap based on start and end point
    private Sequence singleFingerSwipe(final String fingerName, final int index, final Point start, final Point end) {
        final var finger = new PointerInput(PointerInput.Kind.TOUCH, fingerName);
        final var sequence = new Sequence(finger, index);

        sequence.addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), start.getX(), start.getY()));
        sequence.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));

        if (end != null) {
            sequence.addAction(new Pause(finger, Duration.ofMillis(500)));
            sequence.addAction(finger.createPointerMove(Duration.ofMillis(500), PointerInput.Origin.viewport(), end.getX(), end.getY()));
        }

        sequence.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

        return sequence;
    }

    //Performs the tap on screen element
    //Ending coordinates are passed as null for singleFingerSwipe so it will only move to start point, press, and lift finger
    public void tap(final WebElement element) {
        final var start = getElementCenter(element);
        final var sequence = singleFingerSwipe(FINGER_1, 0, start, null);

        this.driver.perform(singletonList(sequence));   //perform method requires a collection of sequence
    }

    //Performs the swipe gesture in given direction with provided distance
    //This method is overloaded in case we want to swipe from the middle of screen, the element is null
    public void swipe(final Direction direction, final int distance) {
        swipe(direction, null, distance);
    }

    //Performs the swipe gesture from the middle of element in given direction with given distance
    //Calculates the swipe start and end points based on element/screen and pass to singleFingerSwipe()
    public void swipe(final Direction direction, final WebElement element, final int distance) {
        final var start = getSwipeStartPosition(element);
        final var end = getSwipeEndPosition(direction, element, distance);

        System.out.println("Swipe.....");
        if (element != null) {
            printDimension("Element Size: ", element.getSize());
            printPoint("Element location: ", element.getLocation());
        }
        printPoint("Start: ", start);
        printPoint("End: ", end);

        final var sequence = singleFingerSwipe(FINGER_1, 0, start, end);
        this.driver.perform(singletonList(sequence));
    }

    /**
     * Performs drag and drop
     * @param source element to drag
     * @param target element to drop on
     */
    public void dragTo (final WebElement source, final WebElement target) {
        final var start = getElementCenter (source);
        final var end = getElementCenter (target);

        System.out.println ("Drag Drop...");
        printPoint ("Start", start);
        printPoint ("End", end);

        final var sequence = singleFingerSwipe (FINGER_1, 0, start, end);

        this.driver.perform (singletonList (sequence));
    }


}
