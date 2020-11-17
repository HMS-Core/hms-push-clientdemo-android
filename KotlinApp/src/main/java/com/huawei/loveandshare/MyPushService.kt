/*
 *  Copyright 2020. Huawei Technologies Co., Ltd. All rights reserved.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at

 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.huawei.loveandshare

import android.content.Intent
import android.util.Log
import com.huawei.hms.push.HmsMessageService
import com.huawei.hms.push.RemoteMessage
import com.huawei.hms.push.SendException
import java.util.*

class MyPushService : HmsMessageService() {
    /**
     * When an app calls the getToken method to apply for a token from the server,
     * if the server does not return the token during current method calling, the server can return the token through this method later.
     * This method callback must be completed in 10 seconds. Otherwise, you need to start a new Job for callback processing.
     * @param token token
     */
    override fun onNewToken(token: String?) {
        Log.i(TAG, "received refresh token:$token")
        // send the token to your app server.
        if (!token.isNullOrEmpty()) {
            // This method callback must be completed in 10 seconds. Otherwise, you need to start a new Job for callback processing.
            refreshedTokenToServer(token)
        }
        val intent = Intent()
        intent.action = CODELABS_ACTION
        intent.putExtra("method", "onNewToken")
        intent.putExtra("msg", "onNewToken called, token: $token")
        sendBroadcast(intent)
    }

    private fun refreshedTokenToServer(token: String) {
        Log.i(TAG, "sending token to server. token:$token")
    }

    /**
     * This method is used to receive downstream data messages.
     * This method callback must be completed in 10 seconds. Otherwise, you need to start a new Job for callback processing.
     *
     * @param message RemoteMessage
     */
    override fun onMessageReceived(message: RemoteMessage?) {
        Log.i(TAG, "onMessageReceived is called")
        if (message == null) {
            Log.e(TAG, "Received message entity is null!")
            return
        }
        // getCollapseKey() Obtains the classification identifier (collapse key) of a message.
        // getData() Obtains valid content data of a message.
        // getMessageId() Obtains the ID of a message.
        // getMessageType() Obtains the type of a message.
        // getNotification() Obtains the notification data instance from a message.
        // getOriginalUrgency() Obtains the original priority of a message.
        // getSentTime() Obtains the time when a message is sent from the server.
        // getTo() Obtains the recipient of a message.
        Log.i(TAG, """getCollapseKey: ${message.collapseKey}
            getData: ${message.data}
            getFrom: ${message.from}
            getTo: ${message.to}
            getMessageId: ${message.messageId}
            getMessageType: ${message.messageType}
            getSendTime: ${message.sentTime}
            getTtl: ${message.ttl}
            getSendMode: ${message.sendMode}
            getReceiptMode: ${message.receiptMode}
            getOriginalUrgency: ${message.originalUrgency}
            getUrgency: ${message.urgency}
            getToken: ${message.token}""".trimIndent())


        // getBody() Obtains the displayed content of a message
        // getTitle() Obtains the title of a message
        // getTitleLocalizationKey() Obtains the key of the displayed title of a notification message
        // getTitleLocalizationArgs() Obtains variable parameters of the displayed title of a message
        // getBodyLocalizationKey() Obtains the key of the displayed content of a message
        // getBodyLocalizationArgs() Obtains variable parameters of the displayed content of a message
        // getIcon() Obtains icons from a message
        // getSound() Obtains the sound from a message
        // getTag() Obtains the tag from a message for message overwriting
        // getColor() Obtains the colors of icons in a message
        // getClickAction() Obtains actions triggered by message tapping
        // getChannelId() Obtains IDs of channels that support the display of messages
        // getImageUrl() Obtains the image URL from a message
        // getLink() Obtains the URL to be accessed from a message
        // getNotifyId() Obtains the unique ID of a message
        val notification = message.notification
        if (notification != null) {
            Log.i(TAG, """
                getTitle: ${notification.title}
                getTitleLocalizationKey: ${notification.titleLocalizationKey}
                getTitleLocalizationArgs: ${Arrays.toString(notification.titleLocalizationArgs)}
                getBody: ${notification.body}
                getBodyLocalizationKey: ${notification.bodyLocalizationKey}
                getBodyLocalizationArgs: ${Arrays.toString(notification.bodyLocalizationArgs)}
                getIcon: ${notification.icon}                
                getImageUrl: ${notification.imageUrl}
                getSound: ${notification.sound}
                getTag: ${notification.tag}
                getColor: ${notification.color}
                getClickAction: ${notification.clickAction}
                getIntentUri: ${notification.intentUri}
                getChannelId: ${notification.channelId}
                getLink: ${notification.link}
                getNotifyId: ${notification.notifyId}
                isDefaultLight: ${notification.isDefaultLight}
                isDefaultSound: ${notification.isDefaultSound}
                isDefaultVibrate: ${notification.isDefaultVibrate}
                getWhen: ${notification.`when`}
                getLightSettings: ${Arrays.toString(notification.lightSettings)}
                isLocalOnly: ${notification.isLocalOnly}
                getBadgeNumber: ${notification.badgeNumber}
                isAutoCancel: ${notification.isAutoCancel}
                getImportance: ${notification.importance}
                getTicker: ${notification.ticker}
                getVibrateConfig: ${notification.vibrateConfig}
                getVisibility: ${notification.visibility}""".trimIndent())
        }
        val intent = Intent()
        intent.action = CODELABS_ACTION
        intent.putExtra("method", "onMessageReceived")
        intent.putExtra("msg", "onMessageReceived called, message id:" + message.messageId + ", payload data:"
                + message.data)
        sendBroadcast(intent)
        val judgeWhetherIn10s = false

        // If the messages are not processed in 10 seconds, the app needs to use WorkManager for processing.
        if (judgeWhetherIn10s) {
            startWorkManagerJob(message)
        } else {
            // Process message within 10s
            processWithin10s(message)
        }
    }

    private fun startWorkManagerJob(message: RemoteMessage?) {
        Log.d(TAG, "Start new Job processing.")
    }

    private fun processWithin10s(message: RemoteMessage?) {
        Log.d(TAG, "Processing now.")
    }

    override fun onMessageSent(msgId: String?) {
        Log.i(TAG, "onMessageSent called, Message id:$msgId")
        val intent = Intent()
        intent.action = CODELABS_ACTION
        intent.putExtra("method", "onMessageSent")
        intent.putExtra("msg", "onMessageSent called, Message id:$msgId")
        sendBroadcast(intent)
    }

    override fun onSendError(msgId: String?, exception: Exception?) {
        Log.i(TAG, "onSendError called, message id:$msgId, ErrCode:${(exception as SendException).errorCode}, " +
                "description:${exception.message}")
        val intent = Intent()
        intent.action = CODELABS_ACTION
        intent.putExtra("method", "onSendError")
        intent.putExtra("msg", "onSendError called, message id:$msgId, ErrCode:${exception.errorCode}, " +
                "description:${exception.message}")
        sendBroadcast(intent)
    }

    override fun onTokenError(e: Exception) {
        super.onTokenError(e)
    }

    companion object {
        private const val TAG: String = "PushDemoLog"
        private const val CODELABS_ACTION: String = "com.huawei.codelabpush.action"
    }
}