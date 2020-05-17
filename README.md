## Push Kit sample code for Android


## Table of Contents

 * [Introduction](#introduction)
 * [Getting Started](#Getting-Started)
 * [Installation](#installation)
 * [Configuration ](#configuration )
 * [Supported Environments](#supported-environments)
 * [Hardware Requirements](#Hardware-Requirements)
 * [License and Terms](#license-and-terms)


## Introduction
    In this Demo, you will use the Demo Project that has been created for you to call HUAWEI Push Kit APIs. Through the Demo Project, you will:
    1. Apply for a token from HUAWEI Push Kit.
    2. Receive notification/data messages from HUAWEI Push Kit. 

    For more information, see
    https://developer.huawei.com/consumer/en/hms/huawei-pushkit

## Getting Started
    1. Register as a developer.
    Register a [HUAWEI account](https://developer.huawei.com/consumer/en/).
    2. Create an app.
    Create an app and enable APIs.
    3. Build the demo.
     To build this demo, please first import the demo to Android Studio (3.X or later). Then download the agconnect-services.json file of the app from AppGallery Connect, and add the file to the app directory (\app) of the demo.
     You should also generate a signing certificate fingerprint and add the certificate file to the project, and add configuration to build.gradle.
     For details, please refer to [Preparations for Integrating HUAWEI HMS Core](https://developer.huawei.com/consumer/en/codelab/HMSPreparation/index.html)

## Installation
    To use functions provided by examples, please make sure Huawei Mobile Service 4.0 has been installed on your cellphone.
    way 1. You can compile and build the codes in Android Studio. After building the APK, you can install it on the phone and debug it.
    way 2. Generate the APK file from Android Studio. Use the ADB tool to install the APK on the phone and debug it
       adb install {YourPath}\pushkit-android-demo\app\release\app-release.apk

## Supported Environments
    Android SDK Version >= 23 and JDK version >= 1.8 is recommended.

## Hardware Requirements
    A computer (desktop or laptop) that runs the Windows 10/Windows 7 operating system
    A Huawei mobile phone with a USB cable, to be used for service debugging

## License and Terms
    pushkit-android-demo is licensed under the [Apache License, version 2.0](http://www.apache.org/licenses/LICENSE-2.0).

