# Appium Mobile Testing

* This repo covers the Appium tutorials and courses from Testing Mini Bytes by Amuthan Sakthivel.
* The code has been extended to use the custom developed `DriverManager` with custom properties files and constants and
  utils.
*

## Appium 2.0 Latest Tutorials 2023

* [Link to YT Videos](https://www.youtube.com/playlist?list=PL9ok7C7Yn9A99LiTcemmKmupBdNB38bbo)

## Tools and Libraries

* Maven 3.9.2
* Java 11
* Selenium 4.9.1
* Appium 2.0.0-beta.71
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

* For the tests, we will be using [Appium Wait Plugin](https://github.com/AppiumTestDistribution/appium-wait-plugin
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
* By default, the tests are running in your local host with default port 4723 set in `server.properties`.
* Similarly, the default device `UDID` are set in `android.properties` and `ios.properties`
* To override the default device for running tests on different device, provide UDID string
  in `DriverManager.initializeDriver()`. If UDID and port is null, then it will use the default UDID and port.
* Tests are located at this path: `com/itkhanz/practice/tests/GesturesTest.java`
* Run the desired tests from Intellij IDE.
* `test_android_setup` and `test_ios_setup` tests will validate if the devices and apps are successfully setup and appium server is up and running.
* 

