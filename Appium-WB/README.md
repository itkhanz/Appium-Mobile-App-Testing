# Appium Tutorials by Wasiq Bhamla


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
* Supply this device name as `avd` property which will automatically start the emulator and you do not need to start it manually
* 
* 