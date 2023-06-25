# Advanced Appium by Jonathan Lipps

* This repo contains the various code snippets and examples of advanced appium.
* The code examples are mainly derived from the advanced appium course from [Jonathan Lipps](https://github.com/jlipps), the project lead of appium.
* Topics covered including application state management, device management, and backdooring into app. It also demonstrates sending and receieving files to and from device.
* Tests for making and receiving phone calls and sms are also demonstrated.
* Further this course also demonstrates usage of appium with espresso driver to backdoor into app.
* The project architecture has been modified to accommodate all the tests and apply clean coding principles.

--

## Important Links

* [Advanced Appium](https://www.linkedin.com/learning/advanced-appium)
* [Calling Methods Inside Your App From Appium](https://www.headspin.io/blog/calling-methods-inside-your-app-from-appium)
* [Using Espresso with Appium](https://appiumpro.com/editions/18-using-espresso-with-appium)
* [White-box testing with Appium Espresso Driver](https://medium.com/bumble-tech/white-box-testing-with-appium-espresso-driver-f51cda81c1bb)
* [Example of appium backdoors using espresso driver](https://github.com/rajdeepv/appium_backdoors)
* [https://github.com/rajdeepv/appium_backdoors](https://medium.com/bumble-tech/add-superpowers-to-your-appium-android-tests-f408ac2f1b59)
* [Application Backdoor via Appium - Rajdeep Varma | Appium Conf 2018](https://www.youtube.com/watch?v=ap4Zp6X-sWc)
* [Appium Espresso Driver](https://github.com/appium/appium-espresso-driver)

--

## Libraries and Tools

* Maven 3.9.2
* Java 11
* TestNG 7.8.0
* Selenium 4.10.0
* Appium Java client 8.5.1
* Appium server 2.0.0-rc.3
    * drivers
        * uiautomator2@2.25.2
        * espresso@2.23.1
        * xcuitest@4.30.2
* Appium inspector 2023.5.2
* Maven surefire plugin 3.1.2
* [Appium doctor](https://github.com/appium/appium/tree/master/packages/doctor)
* Demo Apps
    * [TheApp](https://github.com/appium-pro/TheApp)
    * [Api Demos](https://github.com/appium-boneyard/sample-code/blob/master/sample-code/apps/ApiDemos/bin/ApiDemos-debug.apk)
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

-- 

## Running tests

* Start the appium server `appium --use-drivers=uiautomator2,xcuitest,espresso`
* Start the android emulator from Android Studio, and iOS simulator from XCode.
* Test properties are configured in properties file located at `src/test/resources` directory
* Maven surefire plugin enables to execute the tests from commandline.
* Test suites are configured in `test-suites/` folder for both the applications.
* Running command `mvn clean test` will execute all the tests.
* To execute the Application device management tests:
    * execute command `mvn test -Dtest=AppDeviceManagementTest`
* To execute the Application state management tests:
    * execute command `mvn test -Dtest=AppStateTest`
* To execute the Espresso Backdoor tests:
    * execute command `mvn test -Dtest=EspressoBackdoorTest`
* Alternatively you can execute the tests from  Intellij IDE by hitting the play icon next to test or class.


