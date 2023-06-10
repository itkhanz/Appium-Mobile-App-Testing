# Appium Mobile Testing

* This repo covers the Appium tutorials and courses from Testing Mini Bytes by Amuthan Sakthivel.
* The code has been extended to use the custom developed `DriverManager` with custom properties files and constants and
  utils.
* Gestures are placed into separate utility class.
* [Appium 2.0 Latest Tutorials 2023](https://www.youtube.com/playlist?list=PL9ok7C7Yn9A99LiTcemmKmupBdNB38bbo)


## Migration to Appium Java Client v8

* [The transition from Touch to W3C Actions in Selenium](https://www.thegreenreport.blog/articles/the-transition-from-touch-to-w3c-actions-in-selenium/the-transition-from-touch-to-w3c-actions-in-selenium.html)
* Last year, when Appium Java Client 8.0 released, it introduced a couple of changes, the main one being the migration
  to Selenium 4.
* With these changes, Appium also deprecated the use of the `TouchActions` and `MultiTouchActions` classes. Appium will
  fully drop support for these classes in a future release, and developers are recommended to use the `W3C` actions
  instead.

## Reading Resources

* [What’s New in Appium Java Client 8.0.0](https://applitools.com/blog/whats-new-appium-java-client-8/)
* [Key learnings from Appium Java Client v7 to v8 Migration](https://blogs.halodoc.io/appium-java-client-v7-to-v8-migration/)
* [Appium java client Deprecated APIs](https://javadoc.io/doc/io.appium/java-client/8.1.1/deprecated-list.html)
* [W3C Actions](https://w3c.github.io/webdriver/#dfn-actions)
* [W3C Working Draft](https://www.w3.org/TR/webdriver/#actions)
* [Swiping your way through Appium by Wim Selles](https://www.youtube.com/watch?v=oAJ7jwMNFVU)
* [Automating Complex Gestures with the W3C Actions API](https://appiumpro.com/editions/29-automating-complex-gestures-with-the-w3c-actions-api)
* [iOS-Specific Touch Action Methods](https://appiumpro.com/editions/30-ios-specific-touch-action-methods)
* [How to Automate Gesture Testing with Appium](https://applitools.com/blog/how-to-automate-gesture-testing-appium/)
* [appium-gesture-plugin](https://github.com/AppiumTestDistribution/appium-gestures-plugin/tree/main)


## Tools and Libraries

* Maven 3.9.2
* Java 11
* Selenium 4.9.1
* Appium Java client 8.5.1
* Appium server 2.0.0-beta.71
    * drivers
        * uiautomator2@2.25.2
        * xcuitest@4.30.2
    * plugins
        * element-wait@1.5.0
        * gestures@2.0.0
* Appium inspector 2023.5.2
* [Appium doctor](https://github.com/appium/appium/tree/master/packages/doctor)
* Demo Apps
    * [Sauce Labs My Demo App React Native v1.3.0](https://github.com/saucelabs/my-demo-app-rn/releases/tag/v1.3.0)
    * [ApiDemos App](https://github.com/appium/appium/tree/master/packages/appium/sample-code/apps)
* Android Studio
    * Install android SDK with SDK Manager
        * Android 13.0
            * Android SDK Platform 33
            * Sources for Android 33
            * Google APIs Intel x86_64 Atom System Image (for Intel processors)
            * Google play Intel x86_64 Atom System Image (for Intel processors)
    * Setup Emulators with Virtual Device Manager
        * Pixel 5 with Android 13.0 Google Play | x86_64
* XCode 14.3 (with XCode commandline)
* IntelliJ IDEA

## Setup

* Before Appium installation, install the necessary pre-requisites:
    * Homebrew (for macOS)
    * Node
    * NPM
* Install the appium either via `npm install -g appium@next` or use
  the [appium-installer](https://github.com/AppiumTestDistribution/appium-installer) utility plugin.
* Set the environment variables. For macOS, the environment variable needs to be inside `~/.zshrc` file

```shell
export JAVA_HOME=/Library/Java/JavaVirtualMachines/jdk-11.0.2.jdk/Contents/Home
export PATH=$PATH:$JAVA_HOME/bin

export MAVEN_HOME=/Library/Maven/apache-maven-3.9.2
export PATH=$PATH:$MAVEN_HOME/bin
eval "$(/usr/local/bin/brew shellenv)"

export NVM_DIR="$HOME/.nvm"
[ -s "$NVM_DIR/nvm.sh" ] && \. "$NVM_DIR/nvm.sh"  # This loads nvm
[ -s "$NVM_DIR/bash_completion" ] && \. "$NVM_DIR/bash_completion"  # This loads nvm bash_completion

export ANDROID_HOME=${HOME}/Library/Android/sdk
export PATH=$PATH:$ANDROID_HOME/platform-tools
export PATH=$PATH:$ANDROID_HOME/cmdline-tools
```

* Run following appium-doctor commands to verify the installation of required libraries:

```shell
appium-doctor --ios
appium-doctor --android
```

* For the tests, we will be using [Appium Wait Plugin](https://github.com/AppiumTestDistribution/appium-wait-plugin)
* Start the Appium server with the above plugins `appium --use-plugins=element-wait`
* Run the Android Device Emulator for Pixel 5 for Android Tests (from Android Studio)
* Run the iOS Device Simulator for iPhone 14 for iOS Tests(form XCode)
* Make sure that the apps are already installed in these devices, otherwise you will need to provide `Apps` options
  while creating the driver session in `DriverManager`, so then it will install the app first when running the session.
  Or alternatively before executing tests, Open the app via Appium Inspector with the `App` option set to Url of the app
  in your system, this will install the app in device and open the app in appium inspector. so then you dont need to
  install app while running your tests.
* If the apps are already installed, then you just need `AppActivity` and `AppPackage` in case of Android App,
  and `bundleId` in case of iOS app.
* Run the command `adb shell "dumpsys window | grep -E mCurrentFocus"` to get the AppActivity and AppPackage.
  Alternatively you can install apps like `Apk info` that can tell all the activities associated with the app, and the
  app package.
* The preinstalled app packages and budle iD can be found from the platform websites also.
* By default, the tests are running in your local host with default port 4723 set in `server.properties`.
* Similarly, the default device `UDID` are set in `android.properties` and `ios.properties`
* To override the default device for running tests on different device, provide UDID string
  in `DriverManager.initializeDriver()`. If UDID and port is null, then it will use the default UDID and port.
* Run the command `adb devices` to get the UDID of android devices or emulators running.
* Run the command `xcrun simctl list` to get the UDID of iOS devices or emulators running.
* `BundleID` of iOS can be found in `info.plist` file.
    * Copy the .ipa file and rename the extension to .zip. (So, for example, Pages.ipa will become Pages.zip.)
    * Unzip the ZIP file. You will get a new folder named the same way as the original ZIP file or with Payload name.
    * Right Click the application, and select show package contents.
    * Open the info.plist by double clicking it that opens it in XCode, and there you will find XCode.
* Tests are located at this path: `com/itkhanz/practice/tests/GesturesTest.java`
* Run the desired tests from Intellij IDE.
* `test_android_setup` and `test_ios_setup` tests will validate if the devices and apps are successfully setup and
  appium server is up and running.


## Gestures

Following gestures have been covered and implemented in this repo:
### Tap
```java
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
```

### Double Tap
```java
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
```

### Long Press
```java
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
```
> Or alternatively instead of Sequence class, longPress can also be performed using Actions class 
````java
 new Actions(driver).clickAndHold(element).perform();
````

### Zoom (Pinch)

#### Zoom In
```java
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
```

#### Zoom Out
```java
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
```

### Swipe
```java
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
```

### Scroll
```java
/**
     * Performs Scrolling from the center of screen
     * @param driver AppiumDriver
     * @param direction String UP, DOWN, LEFT, RIGHT
     */
    public static void scroll(AppiumDriver driver, String direction) {

        Dimension size = driver.manage().window().getSize();         //returns the size of window

        //calculate the coordinates for scrolling from center of the screen in direction up
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
```

### Drag and Drop
```java
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
```





