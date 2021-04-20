# 华为推送服务安卓示例代码
[![Apache-2.0](https://img.shields.io/badge/license-Apache-blue)](http://www.apache.org/licenses/LICENSE-2.0)
[![Open Source Love](https://badges.frapsoft.com/os/v1/open-source.svg?v=103)](https://developer.huawei.com/consumer/en/hms)
[![Open Source Love](https://img.shields.io/badge/language-java-green.svg)](https://www.java.com/en/)

[English](https://github.com/HMS-Core/hms-push-clientdemo-android/blob/master/README.md) | 中文

## 目录

 * [简介](#简介)
 * [开发准备](#开发准备)
 * [安装](#安装)
 * [环境要求](#环境要求)
 * [硬件要求](#硬件要求)
 * [授权许可](#授权许可)


## 简介
本示例代码中，你将使用已创建的代码工程来调用华为推送服务（HUAWEI Push Kit）的接口。通过该工程，你将：
1.	向华为推送服务申请令牌（token）。
2.	接收来自华为推送服务的通知栏/透传消息。

<img src="pushDemo.gif" width=250 title="ID Photo DIY" div align=center border=5>

更多内容，请参考
https://developer.huawei.com/consumer/cn/doc/development/HMS-Guides/push-introduction

该示例也可以通过HMS Toolkit快速启动运行，且支持各Kit一站式集成，并提供远程真机免费调测等功能。了解更多信息，请参考HMS Toolkit官方链接：https://developer.huawei.com/consumer/cn/doc/development/Tools-Guides/getting-started-0000001077381096

## 开发准备
1.	注册华为账号，成为华为开发者。
2.	创建应用，启动接口。
3.	构建本示例代码，需要先把它导入安卓集成开发环境（Android Studio）（3.X及以上版本）。然后从AppGallery Connect下载应用的agconnect-services.json文件，并添加到示例代码的app目录下（\app）。另外，需要生成签名证书指纹并将证书文件添加到项目中，然后将配置添加到build.gradle。详细信息，请参考[HUAWEI HMS Core](https://developer.huawei.com/consumer/cn/codelab/HMSPreparation/index.html)集成准备。


## 安装
为了使用示例提供的功能，请通过以下方法确保你的手机已安装华为移动服务4.0：
方法1：在Android Studio中进行代码的编译构建。构建APK完成后，将APK安装到手机上，并调试APK。
方法2：在Android Studio中生成APK。使用ADB（Android Debug Bridge）工具通过adb install {YourPath}\pushkit-android-demo\app\release\app-release.apk命令将APK安装到手机，并调试APK。

## 环境要求
推荐使用的安卓SDK版本为23及以上，JDK版本为1.8及以上。

## 硬件要求
安装有Windows 10/Windows 7操作系统的计算机（台式机或者笔记本）
带USB数据线的华为手机，用于业务调试。

## 技术支持
如果您对HMS Core还处于评估阶段，可在[Reddit社区](https://www.reddit.com/r/HuaweiDevelopers/)获取关于HMS Core的最新讯息，并与其他开发者交流见解。

如果您对使用HMS示例代码有疑问，请尝试：
- 开发过程遇到问题上[Stack Overflow](https://stackoverflow.com/questions/tagged/huawei-mobile-services)，在`huawei-mobile-services`标签下提问，有华为研发专家在线一对一解决您的问题。
- 到[华为开发者论坛](https://developer.huawei.com/consumer/cn/forum/blockdisplay?fid=18) HMS Core板块与其他开发者进行交流。

如果您在尝试示例代码中遇到问题，请向仓库提交[issue](https://github.com/HMS-Core/hms-push-clientdemo-android/issues)，也欢迎您提交[Pull Request](https://github.com/HMS-Core/hms-push-clientdemo-android/pulls)。

## 授权许可
该示例代码经过[Apache 2.0授权许可](http://www.apache.org/licenses/LICENSE-2.0)。
