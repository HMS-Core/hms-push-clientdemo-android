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
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

class Deeplink2Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_deeplink2)
        getIntentData(intent)
    }

    private fun getIntentData(intent: Intent?) {
        if (null != intent) {
            //
            val msgId = intent.getStringExtra("_push_msgId")
            val cmdType = intent.getStringExtra("_push_cmd_type")
            val notifyId = intent.getIntExtra("_push_notifyId", -1)
            val bundle = intent.extras
            bundle?.keySet()?.forEach { key ->
                val content = bundle.getString(key)
                Log.i(TAG, "receive data from push, key = $key, content = $content")
            }
            Log.i(TAG, "receive data from push, msgId = $msgId, cmd = $cmdType, notifyId = $notifyId")
        } else {
            Log.i(TAG, "intent = null")
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        getIntentData(intent)
    }

    companion object {
        private const val TAG: String = "PushDemoLog"
    }
}