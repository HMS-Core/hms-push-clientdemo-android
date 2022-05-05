# HMS Core Push Kit Sample Code (Android)
[![Apache-2.0](https://img.shields.io/badge/license-Apache-blue)](http://www.apache.org/licenses/LICENSE-2.0)
[![Open Source Love](https://badges.frapsoft.com/os/v1/open-source.svg?v=103)](https://developer.huawei.com/consumer/en/hms)
[![Open Source Love](https://img.shields.io/badge/language-java-green.svg)](https://www.java.com/en/)

English | [中文](README_ZH.md)

## Contents

 * [Introduction](#Introduction)
 * [Preparations](#Preparations)
 * [Installation](#Installation)
 * [Environment Requirements](#Environment-Requirements)
 * [Hardware Requirements](#Hardware-Requirements)
 * [License](#License)


## Introduction
In this sample code, you will use the created demo project to call APIs of Push Kit. Through the demo project, you will:
1.	Obtain a token of Push Kit.	
2.	Receive notification messages or data messages from Push Kit.

<img src="pushDemo.gif" width=250 title="ID Photo DIY" div align=center border=5>

For more information, please refer to
[Service Introduction](https://developer.huawei.com/consumer/en/doc/development/HMSCore-Guides/service-introduction-0000001050040060?ha_source=hms1).

You can use HMS Toolkit to quickly run the sample code. HMS Toolkit supports one-stop kit integration, and provides functions such as free app debugging on remote real devices. To learn more about the integration by HMS Toolkit, please refer to the [related section](https://developer.huawei.com/consumer/en/doc/development/HMSCore-Guides/dev-preparation-practice-0000001073320959?ha_source=hms1).

## Preparations
1.	Register as a Huawei developer.
2.	Create an app and start APIs.
3.	Import your demo project to Android Studio 3.*X* or later. Download the **agconnect-services.json** file of the app from AppGallery Connect, and add the file to the app-level directory (**\app**) of your project. Generate a signing certificate fingerprint, add the certificate file to your project, and add the configuration to the *build.gradle* file. For details, please refer to the [integration preparations](https://developer.huawei.com/consumer/en/doc/development/HMSCore-Guides/android-config-agc-0000001050170137?ha_source=hms1).


## Installation
To use the functions provided in the sample code, install HMS Core (APK) 4.0 on your phone in one of the following methods:
Method 1: Compile and build the APK in Android Studio. Then, install the APK on your phone and debug it.
Method 2: Generate the APK in Android Studio. Use the Android Debug Bridge (ADB) tool to run the **adb install {*YourPath*}\pushkit-android-demo\app\release\app-release.apk** command to install the APK on your phone and debug it.

## Environment Requirements
Android SDK 23 or later and JDK 1.8 or later are recommended.

## Hardware Requirements
A computer (desktop or laptop) running Windows 10 or Windows 7
A Huawei phone with a USB data cable, which is used for debugging

## Technical Support
You can visit the [Reddit community](https://www.reddit.com/r/HuaweiDevelopers/) to obtain the latest information about HMS Core and communicate with other developers.

If you have any questions about the sample code, try the following:
- Visit [Stack Overflow](https://stackoverflow.com/questions/tagged/huawei-mobile-services?tab=Votes), submit your questions, and tag them with `huawei-mobile-services`. Huawei experts will answer your questions.
- Visit the HMS Core section in the [HUAWEI Developer Forum](https://forums.developer.huawei.com/forumPortal/en/home?fid=0101187876626530001?ha_source=hms1) and communicate with other developers.

If you encounter any issues when using the sample code, submit your [issues](https://github.com/HMS-Core/hms-push-clientdemo-android/issues) or submit a [pull request](https://github.com/HMS-Core/hms-push-clientdemo-android/pulls).

## License
The sample code is licensed under [Apache License 2.0](http://www.apache.org/licenses/LICENSE-2.0).
