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

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.huawei.agconnect.config.AGConnectServicesConfig
import com.huawei.hms.aaid.HmsInstanceId
import com.huawei.hms.common.ApiException
import com.huawei.hms.push.HmsMessaging

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var tvSetPush: TextView
    private lateinit var tvSetAAID: TextView
    private lateinit var tvSetAutoInit: TextView
    private var receiver: MyReceiver? = null

    @SuppressLint("HandlerLeak")
    var handler: Handler? = object : Handler() {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                GET_AAID -> tvSetAAID.setText(R.string.get_aaid)
                DELETE_AAID -> tvSetAAID.setText(R.string.delete_aaid)
                else -> {
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        tvSetPush = findViewById(R.id.btn_set_push)
        tvSetAAID = findViewById(R.id.btn_get_aaid)
        tvSetAutoInit = findViewById(R.id.btn_set_autoInit_enabled)
        tvSetPush.setOnClickListener(this)
        tvSetAAID.setOnClickListener(this)
        tvSetAutoInit.setOnClickListener(this)

        findViewById<Button>(R.id.btn_add_topic).setOnClickListener(this)
        findViewById<Button>(R.id.btn_get_token).setOnClickListener(this)
        findViewById<Button>(R.id.btn_delete_token).setOnClickListener(this)
        findViewById<Button>(R.id.btn_delete_topic).setOnClickListener(this)
        findViewById<Button>(R.id.btn_action).setOnClickListener(this)
        findViewById<Button>(R.id.btn_generate_intent).setOnClickListener(this)
        findViewById<Button>(R.id.btn_is_autoInit_enabled).setOnClickListener(this)
        receiver = MyReceiver()
        val filter = IntentFilter()
        filter.addAction(CODELABS_ACTION)
        registerReceiver(receiver, filter)
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.btn_get_aaid -> setAAID(tvSetAAID.text.toString() == getString(R.string.get_aaid))
            R.id.btn_get_token -> getToken()
            R.id.btn_delete_token -> deleteToken()
            R.id.btn_set_push -> setReceiveNotifyMsg(tvSetPush.text.toString() == getString(R.string.set_push_enable))
            R.id.btn_add_topic -> addTopic()
            R.id.btn_delete_topic -> deleteTopic()
            R.id.btn_action -> openActivityByAction()
            R.id.btn_generate_intent -> generateIntentUri()
            R.id.btn_is_autoInit_enabled -> isAutoInitEnabled()
            R.id.btn_set_autoInit_enabled -> setAutoInitEnabled(tvSetAutoInit.text.toString() == getString(R.string.AutoInitEnabled))
            else -> {
            }
        }
    }

    /**
     * getAAID(), This method is used to obtain an AAID in asynchronous mode. You need to add a listener to listen to the operation result.
     * deleteAAID(), delete a local AAID and its generation timestamp.
     * @param isGet getAAID or deleteAAID
     */
    private fun setAAID(isGet: Boolean) {
        if (isGet) {
            val idResult = HmsInstanceId.getInstance(this).aaid
            idResult.addOnSuccessListener { aaidResult ->
                val aaId = aaidResult.id
                Log.i(TAG, "getAAID success:$aaId")
                showLog("getAAID success:$aaId")
                handler?.sendEmptyMessage(DELETE_AAID)
            }.addOnFailureListener { e ->
                Log.e(TAG, "getAAID failed:$e")
                showLog("getAAID failed.$e")
            }
        } else {
            object : Thread() {
                override fun run() {
                    try {
                        HmsInstanceId.getInstance(this@MainActivity).deleteAAID()
                        showLog("delete aaid and its generation timestamp success.")
                        handler?.sendEmptyMessage(GET_AAID)
                    } catch (e: Exception) {
                        Log.e(TAG, "deleteAAID failed. $e")
                        showLog("deleteAAID failed.$e")
                    }
                }
            }.start()
        }
    }

    /**
     * getToken(String appId, String scope), This method is used to obtain a token required for accessing HUAWEI Push Kit.
     * If there is no local AAID, this method will automatically generate an AAID when it is called because the Huawei Push server needs to generate a token based on the AAID.
     * This method is a synchronous method, and you cannot call it in the main thread. Otherwise, the main thread may be blocked.
     */
    private fun getToken() {
        showLog("getToken:begin")
        object : Thread() {
            override fun run() {
                try {
                    // read from agconnect-services.json
                    val appId = "Please enter your App_Id from agconnect-services.json "
                    val token = HmsInstanceId.getInstance(this@MainActivity).getToken(appId, "HCM")
                    Log.i(TAG, "get token:$token")
                    if (!TextUtils.isEmpty(token)) {
                        sendRegTokenToServer(token)
                    }
                    showLog("get token:$token")
                } catch (e: ApiException) {
                    Log.e(TAG, "get token failed, $e")
                    showLog("get token failed, $e")
                }
            }
        }.start()
    }

    /**
     * void deleteToken(String appId, String scope) throws ApiException
     * This method is used to obtain a token. After a token is deleted, the corresponding AAID will not be deleted.
     * This method is a synchronous method. Do not call it in the main thread. Otherwise, the main thread may be blocked.
     */
    private fun deleteToken() {
        showLog("deleteToken:begin")
        object : Thread() {
            override fun run() {
                try {
                    // read from agconnect-services.json
                    val appId = "Please enter your App_Id from agconnect-services.json "
                    HmsInstanceId.getInstance(this@MainActivity).deleteToken(appId, "HCM")
                    Log.i(TAG, "deleteToken success.")
                    showLog("deleteToken success")
                } catch (e: ApiException) {
                    Log.e(TAG, "deleteToken failed.$e")
                    showLog("deleteToken failed.$e")
                }
            }
        }.start()
    }

    /**
     * Set up enable or disable the display of notification messages.
     * @param enable enabled or not
     */
    private fun setReceiveNotifyMsg(enable: Boolean) {
        showLog("Control the display of notification messages:begin")
        if (enable) {
            HmsMessaging.getInstance(this).turnOnPush().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    showLog("turnOnPush Complete")
                    tvSetPush.setText(R.string.set_push_unable)
                } else {
                    showLog("turnOnPush failed: cause=" + task.exception.message)
                }
            }
        } else {
            HmsMessaging.getInstance(this).turnOffPush().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    showLog("turnOffPush Complete")
                    tvSetPush.setText(R.string.set_push_enable)
                } else {
                    showLog("turnOffPush  failed: cause =" + task.exception.message)
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
            override fun onConfirmClick(msg: String?) {
                topicDialog.dismiss()
                try {
                    HmsMessaging.getInstance(this@MainActivity)
                            .subscribe(msg)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    Log.i(TAG, "subscribe Complete")
                                    showLog("subscribe Complete")
                                } else {
                                    showLog("subscribe failed: ret=" + task.exception.message)
                                }
                            }
                } catch (e: Exception) {
                    showLog("subscribe failed: exception=" + e.message)
                }
            }

            override fun onCancelClick() {
                topicDialog.dismiss()
            }
        })
        topicDialog.show()
    }

    /**
     * to unsubscribe to topics in asynchronous mode.
     */
    private fun deleteTopic() {
        val topicDialog = TopicDialog(this, false)
        topicDialog.setOnDialogClickListener(object : OnDialogClickListener {
            override fun onConfirmClick(msg: String?) {
                topicDialog.dismiss()
                try {
                    HmsMessaging.getInstance(this@MainActivity)
                            .unsubscribe(msg)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    showLog("unsubscribe Complete")
                                } else {
                                    showLog("unsubscribe failed: ret=" + task.exception.message)
                                }
                            }
                } catch (e: Exception) {
                    showLog("unsubscribe failed: exception=" + e.message)
                }
            }

            override fun onCancelClick() {
                topicDialog.dismiss()
            }
        })
        topicDialog.show()
    }

    /**
     * MyReceiver
     */
    inner class MyReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val bundle = intent?.extras
            if (bundle?.getString("msg") != null) {
                val content = bundle.getString("msg")
                showLog(content)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(receiver)
    }

    fun showLog(log: String?) {
        runOnUiThread {
            val tvView = findViewById<View?>(R.id.tv_log)
            val svView = findViewById<View?>(R.id.sv_log)
            if (tvView is TextView) {
                tvView.text = log
            }
            if (svView is ScrollView) {
                svView.fullScroll(View.FOCUS_DOWN)
            }
        }
    }

    private fun sendRegTokenToServer(token: String?) {
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
        showLog(intentUri)

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
        Log.i(TAG, "isAutoInitEnabled:" + HmsMessaging.getInstance(this).isAutoInitEnabled)
        showLog("isAutoInitEnabled:" + HmsMessaging.getInstance(this).isAutoInitEnabled)
    }

    private fun setAutoInitEnabled(enable: Boolean) {
        if (enable) {
            HmsMessaging.getInstance(this).isAutoInitEnabled = true
            Log.i(TAG, "setAutoInitEnabled: true")
            showLog("setAutoInitEnabled: true")
            tvSetAutoInit.setText(R.string.AutoInitDisabled)
        } else {
            HmsMessaging.getInstance(this).isAutoInitEnabled = false
            Log.i(TAG, "setAutoInitEnabled: false")
            showLog("setAutoInitEnabled: false")
            tvSetAutoInit.setText(R.string.AutoInitEnabled)
        }
    }

    companion object {
        private const val TAG: String = "PushDemoLog"
        private const val GET_AAID = 1
        private const val DELETE_AAID = 2
        private const val CODELABS_ACTION: String = "com.huawei.codelabpush.action"
    }
}
