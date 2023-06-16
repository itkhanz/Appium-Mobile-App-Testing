# Appium Tutorials by Wasiq Bhamla

* [Appium Tutorials](https://www.youtube.com/playlist?list=PLdZJM6yxhZyTKa7l6J5MVnJv663Wd5D3y)
* [Beginners Guide to Appium](https://www.youtube.com/playlist?list=PLdZJM6yxhZySIufG_a_NQM9NWSvOVeAA4)
* [Appium Tips](https://www.youtube.com/playlist?list=PLdZJM6yxhZySpolLYlBl09Q5uoXEagOfO)

---

## Libraries and Tools

---

## Project Boilerplate Design Patterns

### Lombok

* The `AppiumServiceManager` and `AndroidDriverManager` and `IOSDriverManager` use the lombok Builder annotation to
  create
  instance of classes with builder pattern.
* [Project Lombok Builder]{https://projectlombok.org/features/Builder}
* [Lombok builder API javadoc]{https://projectlombok.org/api/lombok/Builder}
* [Lombok builder javadoc]{https://www.javadoc.io/doc/org.projectlombok/lombok/latest/lombok/Builder.html}
* [Intro to lombok - Baeldung]{https://www.baeldung.com/intro-to-project-lombok}
* [lombok builder - Baeldung]{https://www.baeldung.com/lombok-builder}
* `@Builder` annotation creates a so-called 'builder' aspect to the class that is annotated.
* `builderMethodName` Name of the static method that creates a new builder instance.
* `buildMethodName` Name of the instance method in the builder class that creates an instance of your @Builder-annotated
  class.
* The above annotations help us to build the instance of class with custom parameters specific to the test case.
* `Builder.default` The field annotated with @Default must have an initializing expression; that expression is taken as
  the default to be used if not explicitly set during building.

### Driver Manager Interface

* Since the android and iOS needs to initialize driver differently i.e. for android new AndroidDriver() and for iOS new
  IOSDriver(), both these are child classes of AppiumDriver.
* Therefore, we implemented a generic interface `IDriverManager` that the classes `AndroidDriverManager`
  and `IOSDriverManager` implements accordingly to the platform for test case application.
* The `getDriver()` method is overridden in implementation classes to return the new driver instance as per the platform
  with the platform specific capabilities.

### Miscellaneous

* `Path.of (USER_DIR, MessageFormat.format ("src/test/resources/apps/{0}.app.zip", this.appName)` Returns a Path by
  converting a path string, or a sequence of strings that when joined form a path string.
* `MessageFormat` takes a set of objects, formats them, then inserts the formatted strings into the pattern at the appropriate places.
*

---

## Gestures


---

## Notes

* Traversing through the generated XML in Appium Inspector can be cumbersome. To reduce the XML to important nodes:
    * Go to the commands tab, choose Session and Settings, and click on Update Settings
    * Add the following text to settingsJson field `{"ignoreUnimportantViews": true}`
    * If the result of the command is null, then this means command is successfully applied
    * Go to the source tab, and click refresh. This enables finding of the elements to be much faster
* Alternatively you can also supply above settings as capability before starting the session:
    * add the `settings(ignoreUnimportantViews): true` as capability and start the sessoion.
* To avoid dealing with the permissions popup, add this capability `autoGrantPermissions: true`
* Right Click on app file and click `Copy <File Name> a sPathname` to copy the path of file
* Enter the command in terminal `emulator -list-avds` to get the name of virtual device emulators
* Supply this device name as `avd` property which will automatically start the emulator and you do not need to start it
  manually
