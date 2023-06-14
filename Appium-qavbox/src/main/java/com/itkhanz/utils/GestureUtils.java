package com.itkhanz.utils;

import com.google.common.collect.ImmutableList;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Pause;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;

import java.time.Duration;
import java.time.Instant;

import static com.itkhanz.base.DriverManager.getDriver;

/**
 * These Gestures are valid for both IOS and Android
 */
public class GestureUtils {

    public enum ScrollDirection {
        UP, DOWN, LEFT, RIGHT
    }

    /*
    Finds the center of the element
     */
    private static Point getCenterOfElement(Point location, Dimension size) {
        return new Point(location.getX() + size.getWidth() / 2,
                location.getY() + size.getHeight() / 2);
    }

    /*
    Finds the elapsed time in Seconds (long) between two instants
     */
    private static long getElapsedTime(Instant start, Instant end) {
        return Duration.between(start, end).toSeconds();
    }

    /**
     * Finds out the end of Page if after scrolling the page source is same
     * This method can take longer if the page sources are big, and therefore can be slow. only use it if the page content is loaded dynamically.
     * Alternative way to find out the end of page is to check if the last element in specific list is the same as previous, or compare the list of elements
     * @param pageSource page source before scroll
     * @return true if the current page source is same as previous page source else false
     */
    public static boolean isEndOfPage(String pageSource){
        return pageSource.equals(getDriver().getPageSource());
    }

    /**
     * This method scrolls in the scroll direction for a maximum of timeout duration till the element is visible
     * @param byEl locator of the element to scroll to
     * @param direction ScrollDirection enum constant
     * @param scrollRatio will determine how far too scroll. Higher ratio will mean scrolling to longer distance and vice-versa. It must be between 0-1.
     * @param scrollDurationMillis milliSeconds to be taken to complete the pointer move action
     * @param timeout maximum time duration elapsed for scroll in seconds. If element is not found within timeout, method returns false.
     * @return true if element is found within timeout else false
     */
    public static boolean scrollTillElement(By byEl, ScrollDirection direction, double scrollRatio, int scrollDurationMillis, long timeout){
        Instant start = Instant.now();
        Instant end = Instant.now();
        long timeElapsed = getElapsedTime(start, end);
        String prevPageSource = "";

        while(!isEndOfPage(prevPageSource) && timeElapsed < timeout) {
            prevPageSource = getDriver().getPageSource();
            timeElapsed = getElapsedTime(start, Instant.now()); //Only needed for the following print statements
            try{
                getDriver().findElement(byEl);
                System.out.println("Element found after " + timeElapsed + " seconds");
                return true;
            }catch(org.openqa.selenium.NoSuchElementException e){
                System.out.println("Element not found after " + timeElapsed + " seconds");
                scroll(direction, scrollRatio, scrollDurationMillis);   //Since the equality check on page source can take longer therefore delay may occur till next try
                timeElapsed = getElapsedTime(start, Instant.now()); //update the elapsedTime again after scroll
            }
        }
        return false;
    }

    /**
     * This method calculates the coordinates for the scroll/swipe gesture and pass it to swipe action with coordinates of endPoint based on ScrollDirection.
     * @param direction ScrollDirection enum constant e.g. UP Direction will mean scrolling down (swiping up)
     * @param scrollRatio will determine how far too scroll. Higher ratio will mean scrolling to longer distance and vice-versa. It must be between 0-1.
     * @param scrollDurationMillis milliSeconds to be taken to complete the pointer move action
     */
    public static void scroll(ScrollDirection direction, double scrollRatio, int scrollDurationMillis) {
        //duration for the swipe gesture to happen
        Duration SCROLL_DUR = Duration.ofMillis(scrollDurationMillis);

        //throw Exception if scrollRatio is not between range [0,1]
        if (scrollRatio < 0 || scrollRatio > 1) {
            throw new Error("Scroll distance must be between 0 and 1");
        }

        //Get the screen size
        Dimension size = getDriver().manage().window().getSize();

        //Calculate Coordinates for the midPoint, and offsets for the all directions
        Point midPoint = new Point((int)(size.width * 0.5),(int)(size.height * 0.5));
        int bottom = midPoint.y + (int)(midPoint.y * scrollRatio);
        int top = midPoint.y - (int)(midPoint.y * scrollRatio);
        int left = midPoint.x - (int)(midPoint.x * scrollRatio);
        int right = midPoint.x + (int)(midPoint.x * scrollRatio);

        //Perform swipe gesture by passing coordinates of the start and end point based on the scroll direction
        //Swipe and Scroll happen in opposite direction i.e. scrolling up is equivalent to swiping down and vice-versa
        if (direction == ScrollDirection.UP) {
            swipe(new Point(midPoint.x, top), new Point(midPoint.x, bottom), SCROLL_DUR);   //swipe down (or scroll up)
        } else if (direction == ScrollDirection.DOWN) {
            swipe(new Point(midPoint.x, bottom), new Point(midPoint.x, top), SCROLL_DUR);   //swipe up (or scroll down)
        } else if (direction == ScrollDirection.LEFT) {
            swipe(new Point(left, midPoint.y), new Point(right, midPoint.y), SCROLL_DUR);   //swipe right (or scroll left)
        } else {
            swipe(new Point(right, midPoint.y), new Point(left, midPoint.y), SCROLL_DUR);   //swipe left (or scroll right)
        }
    }

    /**
     * Performs the sequence of actions emulating the swipe gesture
     * @param start coordinates of the starting point
     * @param end coordinates of the ending point
     * @param duration time taken to complete gesture
     */
    public static void swipe(Point start, Point end, Duration duration) {
        //models a pointer input device, like mouse, pen, touch
        PointerInput input = new PointerInput(PointerInput.Kind.TOUCH, "finger");

        //creates a sequence of actions for a given input source
        Sequence sequence = new Sequence(input, 0);
        sequence
                .addAction(input.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), start.x, start.y))    //Move finger to the start point
                .addAction(input.createPointerDown(PointerInput.MouseButton.LEFT.asArg()))                              //press finger
                .addAction(input.createPointerMove(duration, PointerInput.Origin.viewport(), end.x, end.y))             //Move finger to the end point
                .addAction(input.createPointerUp(PointerInput.MouseButton.LEFT.asArg()))                                //lift finger
                ;
        //perform sequence action
        getDriver().perform(ImmutableList.of(sequence));
    }

    /**
     * Performs long press on the element.
     * @param element WebElement to perform action on
     * @param pressDurationSeconds How long to press
     */
    public static void longPress(WebElement element, int pressDurationSeconds) {
        Point location = element.getLocation();     //returns the top left coordinates of the element on page
        Dimension size = element.getSize();         //returns the width and length of element

        //Find the center of element to perform action on
        Point centerOfElement = getCenterOfElement(location, size);

        //models a pointer input device, like mouse, pen, touch
        PointerInput input = new PointerInput(PointerInput.Kind.TOUCH, "finger1");

        Sequence sequence = new Sequence(input, 0);
        sequence.addAction(input.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), centerOfElement)); //Move finger to the start point
        sequence.addAction(input.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));                                 //press finger
        sequence.addAction(new Pause(input, Duration.ofSeconds(pressDurationSeconds)));                                     //wait for few seconds
        sequence.addAction(input.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));                                   //lift finger

        //perform sequence action
        getDriver().perform(ImmutableList.of(sequence));
    }
}

