# Appium Mobile Testing

* This repo covers the Appium tutorials and courses from qavbox by Sunil Patro.
* The code has been extended to use the custom developed `DriverManager` and `AppFactory` with custom properties files and constants and
  utils.
* Furthermore, the application specific options are loaded dynamically from the `applications.json` based on the ENUM App constant id.
* Gestures are placed into separate utility class.
* Original code base has been refactored and extended to improve framework structure. This enables the code repo to be
  cloned and reused for multiple frameworks since all the custom settings can be easily configured from properties file.
* Code base is refactored to support the custom application capabilities specific to the test case.
* [Gestures Using Appium](https://www.youtube.com/playlist?list=PLPO0LFyCaSo2X4NLeZsIwnRcq32asQiI9)
* [Appium Gestures Plugin](https://www.youtube.com/watch?v=12Qx-jl34GI&list=PLPO0LFyCaSo1DKak8ZhEJ3NXrj2shNM0N)
* [Appium Tutorial Series](https://www.youtube.com/playlist?list=PLPO0LFyCaSo1DKak8ZhEJ3NXrj2shNM0N)


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
* TestNG 7.8.0
* Selenium 4.9.1
* Appium Java client 8.5.1
* Appium server 2.0.0-beta.71
    * drivers
        * uiautomator2@2.25.2
        * xcuitest@4.30.2
    * plugins
        * element-wait@1.5.0
        * gestures@2.1.0
* Appium inspector 2023.5.2
* SLF4J Logback 1.4.7
* Jackson databind 2.15.2
* Lombok 1.18.28
* [Appium doctor](https://github.com/appium/appium/tree/master/packages/doctor)
* Demo Apps
    * [Sauce Labs My Demo App React Native v1.3.0](https://github.com/saucelabs/my-demo-app-rn/releases/tag/v1.3.0)
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
* To execute tests from cmd, add maven surefire plugin and maven compiler plugin in `POM.xml`

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
* Start the Appium server with the above plugins `appium --use-drivers=uiautomator2,xcuitest --use-plugins=element-wait,gestures`
* Configure all the server related settings in `server.properties` file to start the appium server programmatically.
* Run the Android Device Emulator for Pixel 5 for Android Tests (from Android Studio)
* Run the iOS Device Simulator for iPhone 14 for iOS Tests(form XCode)
* Make sure that the apps are already installed in these devices, otherwise you will need to provide `Apps` options
  while creating the driver session in `AppFactory.launchApp()`, so then it will install the app first when running the session.
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
  in `AppFactory.launchApp()`. If UDID and port is null, then it will use the default UDID and port.
* Run the command `adb devices` to get the UDID of android devices or emulators running.
* Run the command `xcrun simctl list` to get the UDID of iOS devices or emulators running.
* `BundleID` of iOS can be found in `info.plist` file.
    * Copy the .ipa file and rename the extension to .zip. (So, for example, Pages.ipa will become Pages.zip.)
    * Unzip the ZIP file. You will get a new folder named the same way as the original ZIP file or with Payload name.
    * Right Click the application, and select show package contents.
    * Open the info.plist by double clicking it that opens it in XCode, and there you will find XCode.
* Place all your applications in directory `/src/main/resources/apps/`
* Tests are located at this path: `test/java/com/itkhanz/tests/`
    * `GesuresTest` contains the code snippets from Appium W3C complaint actions with sequence class. Also
      see `GestureUtils` which hides the implementation details of these gestures.
    * `GesturesPluginTest` contains the code snippets for performing different gestures with `appium-gesture-plugin`
* Initially `log4j2` was used for logging with configuration defined in `log4j2.properties`.
* Then SLF4J binding with simple (`slf4j-simple`) was used for configuration defined in `simplelogger.properties`.
* There was a limitation with `slf4j-simple` since we cannot log to multiple appenders at the same time e.g. console and
  file. To solve this, we used `Logback`
* `Logback` is used for logging and the logging configuration is defined in `simplelogger.properties`
* To use Logback, include `logback-classic` dependency. Note that logback-classic transitively includes the `slf4j-api` and
  `logback-core`, so only having the logback-classic is enough to setup logback with slf4j.
* [SLF4J Logback Tutorial - mykong](https://mkyong.com/logging/slf4j-logback-tutorial/)
* [SLF4J Logback Tutorial - mykong](https://mkyong.com/logging/slf4j-logback-tutorial/)
* [A Guide To Logback](https://www.baeldung.com/logback)
* `Lombok` annotations are used to build the POJOs for `appliations.json`, and then the POJOs are converted to Map of
  app specific options via `jackson-databind`
* Run the desired tests from Intellij IDE.
* `test_androidLaunch` and `test_iOSLaunch` tests will validate if the devices and apps are successfully setup and
  appium server is up and running.
* You can also run the tests through maven command line with `mvn test`




