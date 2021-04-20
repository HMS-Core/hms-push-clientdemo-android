# Push Kit sample code for Android
[![Apache-2.0](https://img.shields.io/badge/license-Apache-blue)](http://www.apache.org/licenses/LICENSE-2.0)
[![Open Source Love](https://badges.frapsoft.com/os/v1/open-source.svg?v=103)](https://developer.huawei.com/consumer/en/hms)
[![Open Source Love](https://img.shields.io/badge/language-java-green.svg)](https://www.java.com/en/)

English | [中文](https://github.com/HMS-Core/hms-push-clientdemo-android/blob/master/README_ZH.md)

## Table of Contents

 * [Introduction](#introduction)
 * [Getting Started](#getting-started)
 * [Installation](#installation)
 * [Supported Environment](#supported-environment)
 * [Hardware Requirements](#hardware-requirements)
 * [License](#license)


## Introduction
In this Demo, you will use the Demo Project that has been created for you to call HUAWEI Push Kit APIs. Through the Demo Project, you will:
1. Apply for a token from HUAWEI Push Kit.
2. Receive notification/data messages from HUAWEI Push Kit. 

<img src="pushDemo.gif" width=250 title="ID Photo DIY" div align=center border=5>

For more information, please refer to: https://developer.huawei.com/consumer/en/doc/development/HMS-Guides/push-introduction?ha_source=hms1

You also can use HMS Toolkit to quickly integrate the kit and run the demo project, as well as debug the app using a remote device for free. For details, please visit https://developer.huawei.com/consumer/en/doc/development/Tools-Guides/getting-started-0000001077381096.

## Getting Started
1. Register as a developer
Register a [HUAWEI account](https://developer.huawei.com/consumer/en/doc/start/10104?ha_source=hms1).
2. Create an app
Create an app and enable APIs.
3. Build the demo
To build this demo, please first import the demo to Android Studio (3.X or later). Then download the agconnect-services.json file of the app from AppGallery Connect, and add the file to the app directory (\app) of the demo.
     You should also generate a signing certificate fingerprint and add the certificate file to the project, and add configuration to build.gradle.
     For details, please refer to [Preparations for Integrating HUAWEI HMS Core](https://developer.huawei.com/consumer/en/codelab/HMSPreparation/index.html?ha_source=hms1)

## Installation
To use functions provided by examples, please make sure Huawei Mobile Service 4.0 has been installed on your cellphone. There are two ways to install the sample demo: 
1. You can compile and build the codes in Android Studio. After building the APK, you can install it on the phone and debug it.  
2. Generate the APK file from Gradle. Use the ADB tool to install the APK on the phone and debug it adb install {YourPath}\pushkit-android-demo\app\release\app-release.apk

## Supported Environment
Android SDK Version >= 23 and JDK version >= 1.8 is recommended.

## Hardware Requirements
A computer (desktop or laptop) and a Huawei mobile phone with a USB cable, to be used for service debugging.

## Question or issues
If you want to evaluate more about HMS Core,
[r/HMSCore on Reddit](https://www.reddit.com/r/HuaweiDevelopers/) is for you to keep up with latest news about HMS Core, and to exchange insights with other developers.

If you have questions about how to use HMS samples, try the following options:
- [Stack Overflow](https://stackoverflow.com/questions/tagged/huawei-mobile-services) is the best place for any programming questions. Be sure to tag your question with 
`huawei-mobile-services`.
- [Huawei Developer Forum](https://forums.developer.huawei.com/forumPortal/en/home?fid=0101187876626530001?ha_source=hms1) HMS Core Module is great for general questions, or seeking recommendations and opinions.

If you run into a bug in our samples, please submit an [issue](https://github.com/HMS-Core/hms-push-clientdemo-android/issues) to the Repository. Even better you can submit a [Pull Request](https://github.com/HMS-Core/hms-push-clientdemo-android/pulls) with a fix.

## License
Push kit sample code for android is licensed under the [Apache License, version 2.0](http://www.apache.org/licenses/LICENSE-2.0).
