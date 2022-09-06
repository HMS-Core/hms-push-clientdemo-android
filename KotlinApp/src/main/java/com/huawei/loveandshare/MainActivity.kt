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

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.huawei.hms.aaid.HmsInstanceId
import com.huawei.hms.common.ApiException
import com.huawei.hms.push.HmsMessaging
import com.huawei.loveandshare.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private val receiver: MyReceiver = MyReceiver()

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.btnSetPush.setOnClickListener {
            setReceiveNotifyMsg(binding.btnSetPush.text.toString() == getString(R.string.set_push_enable))
        }

        binding.btnGetAaid.setOnClickListener {
            setAAID(binding.btnGetAaid.text.toString() == getString(R.string.get_aaid))
        }

        binding.btnSetAutoInitEnabled.setOnClickListener {
            setAutoInitEnabled(binding.btnSetAutoInitEnabled.text.toString() == getString(R.string.AutoInitEnabled))
        }

        binding.btnAddTopic.setOnClickListener {
            addTopic()
        }

        binding.btnGetToken.setOnClickListener {
            getToken()
        }

        binding.btnDeleteToken.setOnClickListener {
            deleteToken()
        }

        binding.btnDeleteTopic.setOnClickListener {
            deleteTopic()
        }

        binding.btnAction.setOnClickListener {
            openActivityByAction()
        }

        binding.btnGenerateIntent.setOnClickListener {
            generateIntentUri()
        }

        binding.btnIsAutoInitEnabled.setOnClickListener {
            isAutoInitEnabled()
        }

        val filter = IntentFilter()
        filter.addAction(CODELABS_ACTION)
        registerReceiver(receiver, filter)
    }

    /**
     * getAAID(), This method is used to obtain an AAID in asynchronous mode. You need to add a listener to listen to the operation result.
     * deleteAAID(), delete a local AAID and its generation timestamp.
     * @param isGet getAAID or deleteAAID
     */
    private fun setAAID(isGet: Boolean) {
        lifecycleScope.launch {
            if(isGet) {
                try {
                    val idResult = HmsInstanceId.getInstance(this@MainActivity).aaid.await()
                    val aaId = idResult.id
                    Log.i(TAG, "getAAID success:$aaId")
                    printLogInUI("getAAID success:$aaId")
                    binding.btnGetAaid.setText(R.string.delete_aaid)
                } catch (e : Exception) {
                    Log.e(TAG, "getAAID failed:$e")
                    printLogInUI("getAAID failed.$e")
                }
            } else {
                try {
                  withContext(Dispatchers.IO) {
                      HmsInstanceId.getInstance(this@MainActivity).deleteAAID()
                  }
                  printLogInUI("delete aaid and its generation timestamp success.")
                  binding.btnGetAaid.setText(R.string.get_aaid)
                } catch (e: Exception) {
                    Log.e(TAG, "deleteAAID failed. $e")
                    printLogInUI("deleteAAID failed.$e")
                }
            }
        }
    }

    /**
     * getToken(String appId, String scope), This method is used to obtain a token required for accessing HUAWEI Push Kit.
     * If there is no local AAID, this method will automatically generate an AAID when it is called because the Huawei Push server needs to generate a token based on the AAID.
     * This method is a synchronous method, and you cannot call it in the main thread. Otherwise, the main thread may be blocked.
     */
    private fun getToken() {
        printLogInUI("getToken:begin")
        lifecycleScope.launch {
            try {
                val token = withContext(Dispatchers.IO) {
                    // read from agconnect-services.json
                    val pushToken = HmsInstanceId.getInstance(this@MainActivity).getToken(APP_ID, "HCM")
                    Log.i(TAG, "get token:$pushToken")
                    if (!pushToken.isNullOrEmpty()) {
                        sendRegTokenToServer(pushToken)
                    }
                    pushToken
                }
                printLogInUI("get token:$token")
            } catch (e: ApiException) {
                Log.e(TAG, "get token failed, $e")
                printLogInUI("get token failed, $e")
            }
        }
    }

    /**
     * void deleteToken(String appId, String scope) throws ApiException
     * This method is used to obtain a token. After a token is deleted, the corresponding AAID will not be deleted.
     * This method is a synchronous method. Do not call it in the main thread. Otherwise, the main thread may be blocked.
     */
    private fun deleteToken() {
        printLogInUI("deleteToken:begin")
        lifecycleScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    HmsInstanceId.getInstance(this@MainActivity).deleteToken(APP_ID, "HCM")
                }
                Log.i(TAG, "deleteToken success.")
                printLogInUI("deleteToken success")
            } catch (e: ApiException) {
                Log.e(TAG, "deleteToken failed.$e")
                printLogInUI("deleteToken failed.$e")
            }
        }
    }

    /**
     * Set up enable or disable the display of notification messages.
     * @param enable enabled or not
     */
    private fun setReceiveNotifyMsg(enable: Boolean) {
        printLogInUI("Control the display of notification messages:begin")
        lifecycleScope.launch {
            if (enable) {
                try {
                    HmsMessaging.getInstance(this@MainActivity).turnOnPush().await()
                    printLogInUI("turnOnPush Complete")
                    binding.btnSetPush.setText(R.string.set_push_unable)
                } catch (e : Exception) {
                    printLogInUI("turnOnPush failed: cause= ${e.message}")
                }
            } else {
                try {
                    HmsMessaging.getInstance(this@MainActivity).turnOffPush().await()
                    printLogInUI("turnOffPush Complete")
                    binding.btnSetPush.setText(R.string.set_push_enable)
                } catch (e : Exception) {
                    printLogInUI("turnOffPush  failed: cause = ${e.message}")
                }
            }
        }
    }

    /**
     * to subscribe to topics in asynchronous mode.
     */
    private fun addTopic() {
        val topicDialog = TopicDialog(this, true)
        topicDialog.setOnDialogClickListener(object : OnDialogClickListener {
            override fun onConfirmClick(msg: String) {
                topicDialog.dismiss()
                subscribeTopic(msg)
            }

            override fun onCancelClick() {
                topicDialog.dismiss()
            }
        })
        topicDialog.show()
    }

    private fun subscribeTopic(topic : String) {
        lifecycleScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    HmsMessaging.getInstance(this@MainActivity).subscribe(topic).await()
                }
                Log.i(TAG, "subscribe Complete")
                printLogInUI("subscribe Complete")
            } catch (e : Exception) {
                printLogInUI("subscribe failed: exception= ${e.message}")
            }
        }
    }

    /**
     * to unsubscribe to topics in asynchronous mode.
     */
    private fun deleteTopic() {
        val topicDialog = TopicDialog(this, false)
        topicDialog.setOnDialogClickListener(object : OnDialogClickListener {
            override fun onConfirmClick(msg: String) {
                topicDialog.dismiss()
                unsubscribeTopic(msg)
            }

            override fun onCancelClick() {
                topicDialog.dismiss()
            }
        })
        topicDialog.show()
    }

    private fun unsubscribeTopic(topic : String) {
        lifecycleScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    HmsMessaging.getInstance(this@MainActivity).unsubscribe(topic).await()
                }
                printLogInUI("unsubscribe Complete")
            } catch (e : Exception) {
                printLogInUI("unsubscribe failed: ret= ${e.message}")
            }
        }
    }

    private inner class MyReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            intent.extras?.getString("msg")?.let {
                printLogInUI(it)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(receiver)
    }

    private fun printLogInUI(log: String) {
        binding.layoutLog.run {
            tvLog.append("$log\n")
            svLog.fullScroll(View.FOCUS_DOWN)
        }
    }

    private fun sendRegTokenToServer(token: String) {
        Log.i(TAG, "sending token to server. token:$token")
    }

    /**
     * In Opening a Specified Page of an App, how to Generate Intent parameters.
     */
    private fun generateIntentUri() {
        val intent = Intent(Intent.ACTION_VIEW)

        // You can add parameters in either of the following ways:
        // Define a scheme protocol, for example, pushscheme://com.huawei.codelabpush/deeplink?.
        // way 1 start: Use ampersands (&) to separate key-value pairs. The following is an example:
        intent.data = Uri.parse("pushscheme://com.huawei.codelabpush/deeplink?name=abc&age=180")
        // way 1 end. In this example, name=abc and age=180 are two key-value pairs separated by an ampersand (&).

        // way 2 start: Directly add parameters to the Intent.
        // intent.setData(Uri.parse("pushscheme://com.huawei.codelabpush/deeplink?"));
        // intent.putExtra("name", "abc");
        // intent.putExtra("age", 180);
        // way 2 end.

        // The following flag is mandatory. If it is not added, duplicate messages may be displayed.
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val intentUri = intent.toUri(Intent.URI_INTENT_SCHEME)
        // The value of intentUri will be assigned to the intent parameter in the message to be sent.
        Log.d("intentUri", intentUri)
        printLogInUI(intentUri)

        // You can start the deep link activity with the following code.
        //intent.setClass(this, DeeplinkActivity.class);
        //startActivity(intent);
    }

    /**
     * Simulate pulling up the application custom page by action.
     */
    private fun openActivityByAction() {
        val intent = Intent("com.huawei.codelabpush.intent.action.test")

        // You can start the deep link activity with the following code.
        intent.setClass(this, Deeplink2Activity::class.java)
        startActivity(intent)
    }

    private fun isAutoInitEnabled() {
        Log.i(TAG, "isAutoInitEnabled: ${HmsMessaging.getInstance(this).isAutoInitEnabled}")
        printLogInUI("isAutoInitEnabled: ${HmsMessaging.getInstance(this).isAutoInitEnabled}")
    }

    private fun setAutoInitEnabled(enable: Boolean) {
        if (enable) {
            HmsMessaging.getInstance(this).isAutoInitEnabled = true
            Log.i(TAG, "setAutoInitEnabled: true")
            printLogInUI("setAutoInitEnabled: true")
            binding.btnSetAutoInitEnabled.setText(R.string.AutoInitDisabled)
        } else {
            HmsMessaging.getInstance(this).isAutoInitEnabled = false
            Log.i(TAG, "setAutoInitEnabled: false")
            printLogInUI("setAutoInitEnabled: false")
            binding.btnSetAutoInitEnabled.setText(R.string.AutoInitEnabled)
        }
    }

    companion object {
        private const val APP_ID = "" //TODO add your appID here

        private const val TAG: String = "PushDemoLog"
        private const val CODELABS_ACTION: String = "com.huawei.codelabpush.action"
    }
}
