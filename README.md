oneclipboard
============

An Android + Desktop app to sync clipboard automatically

###Requirements
1. Java 1.7+
2. Gradle 1.12
3. Android Tools 19.0
4. Android SDK 16

###Build Instructions
```shell
gradle :oneclipboardlib:build
gradle :oneclipboardandroid:build
gradle :oneclipboarddesktop:build
gradle :oneclipboardserver:build
```

####Generate Eclipse Project Files
```shell
gradle :oneclipboardlib:eclipse
gradle :oneclipboardandroid:eclipse
gradle :oneclipboarddesktop:eclipse
gradle :oneclipboardserver:eclipse
```

####Creating Fat Jar
A single runnable jar can be created for the desktop and server with the following commands:
```shell
gradle :oneclipboarddesktop:shadowJar
gradle :oneclipboardserver:shadowJar
```
This should create a *-all.jar under build/libs.

###Usage
1. Run oneclipboardserver - gradle :oneclipboardserver:run
2. Run oneclipboarddesktop - gradle :oneclipboarddesktop:run
3. Install oneclipboardandroid/build/apk/oneclipboardandroid-debug-unaligned.apk on your Android device.
4. Copy some text on your desktop/android, paste it on your android/desktop.

Note: You can use any username/password combination but make sure its the same on all the devices.
