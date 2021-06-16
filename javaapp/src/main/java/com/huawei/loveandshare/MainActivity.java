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

package com.huawei.loveandshare;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.huawei.agconnect.config.AGConnectServicesConfig;
import com.huawei.hmf.tasks.OnCompleteListener;
import com.huawei.hmf.tasks.OnFailureListener;
import com.huawei.hmf.tasks.OnSuccessListener;
import com.huawei.hmf.tasks.Task;
import com.huawei.hms.aaid.HmsInstanceId;
import com.huawei.hms.aaid.entity.AAIDResult;
import com.huawei.hms.common.ApiException;
import com.huawei.hms.push.HmsMessaging;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "PushDemoLog";

    private TextView tvSetPush;

    private TextView tvSetAAID;

    private TextView tvSetAutoInit;

    private final static int GET_AAID = 1;

    private final static int DELETE_AAID = 2;

    private final static String CODELABS_ACTION = "com.huawei.codelabpush.action";

    private MyReceiver receiver;

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case GET_AAID:
                    tvSetAAID.setText(R.string.get_aaid);
                    break;
                case DELETE_AAID:
                    tvSetAAID.setText(R.string.delete_aaid);
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvSetPush = findViewById(R.id.btn_set_push);
        tvSetAAID = findViewById(R.id.btn_get_aaid);
        tvSetAutoInit = findViewById(R.id.btn_set_autoInit_enabled);

        tvSetPush.setOnClickListener(this);
        tvSetAAID.setOnClickListener(this);
        tvSetAutoInit.setOnClickListener(this);

        findViewById(R.id.btn_add_topic).setOnClickListener(this);
        findViewById(R.id.btn_get_token).setOnClickListener(this);
        findViewById(R.id.btn_delete_token).setOnClickListener(this);
        findViewById(R.id.btn_delete_topic).setOnClickListener(this);
        findViewById(R.id.btn_action).setOnClickListener(this);
        findViewById(R.id.btn_generate_intent).setOnClickListener(this);
        findViewById(R.id.btn_is_autoInit_enabled).setOnClickListener(this);

        receiver = new MyReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(CODELABS_ACTION);
        registerReceiver(receiver, filter);
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        switch (viewId) {
            case R.id.btn_get_aaid:
                setAAID(tvSetAAID.getText().toString().equals(getString(R.string.get_aaid)));
                break;
            case R.id.btn_get_token:
                getToken();
                break;
            case R.id.btn_delete_token:
                deleteToken();
                break;
            case R.id.btn_set_push:
                setReceiveNotifyMsg(tvSetPush.getText().toString().equals(getString(R.string.set_push_enable)));
                break;
            case R.id.btn_add_topic:
                addTopic();
                break;
            case R.id.btn_delete_topic:
                deleteTopic();
                break;
            case R.id.btn_action:
                openActivityByAction();
                break;
            case R.id.btn_generate_intent:
                generateIntentUri();
                break;
            case R.id.btn_is_autoInit_enabled:
                isAutoInitEnabled();
                break;
            case R.id.btn_set_autoInit_enabled:
                setAutoInitEnabled(tvSetAutoInit.getText().toString().equals(getString(R.string.AutoInitEnabled)));
                break;
            default:
                break;
        }
    }

    /**
     * getAAID(), This method is used to obtain an AAID in asynchronous mode. You need to add a listener to listen to the operation result.
     * deleteAAID(), delete a local AAID and its generation timestamp.
     * @param isGet getAAID or deleteAAID
     */
    private void setAAID(boolean isGet) {
        if (isGet) {
            Task<AAIDResult> idResult = HmsInstanceId.getInstance(this).getAAID();
            idResult.addOnSuccessListener(new OnSuccessListener<AAIDResult>() {
                @Override
                public void onSuccess(AAIDResult aaidResult) {
                    String aaId = aaidResult.getId();
                    Log.i(TAG, "getAAID success:" + aaId);
                    showLog("getAAID success:" + aaId);
                    handler.sendEmptyMessage(DELETE_AAID);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(Exception e) {
                    Log.e(TAG, "getAAID failed:" + e);
                    showLog("getAAID failed." + e);
                }
            });
        } else {
            new Thread() {
                @Override
                public void run() {
                    try {
                        HmsInstanceId.getInstance(MainActivity.this).deleteAAID();
                        showLog("delete aaid and its generation timestamp success.");
                        handler.sendEmptyMessage(GET_AAID);
                    } catch (Exception e) {
                        Log.e(TAG, "deleteAAID failed. " + e);
                        showLog("deleteAAID failed." + e);
                    }

                }
            }.start();
        }
    }

    /**
     * getToken(String appId, String scope), This method is used to obtain a token required for accessing HUAWEI Push Kit.
     * If there is no local AAID, this method will automatically generate an AAID when it is called because the Huawei Push server needs to generate a token based on the AAID.
     * This method is a synchronous method, and you cannot call it in the main thread. Otherwise, the main thread may be blocked.
     */
    private void getToken() {
        showLog("getToken:begin");

        new Thread() {
            @Override
            public void run() {
                try {
                    // read from agconnect-services.json
                    String appId = "Please enter your App_Id from agconnect-services.json ";
                    String token = HmsInstanceId.getInstance(MainActivity.this).getToken(appId, "HCM");
                    Log.i(TAG, "get token:" + token);
                    if(!TextUtils.isEmpty(token)) {
                        sendRegTokenToServer(token);
                    }

                    showLog("get token:" + token);
                } catch (ApiException e) {
                    Log.e(TAG, "get token failed, " + e);
                    showLog("get token failed, " + e);
                }
            }
        }.start();
    }

    /**
     * void deleteToken(String appId, String scope) throws ApiException
     * This method is used to obtain a token. After a token is deleted, the corresponding AAID will not be deleted.
     * This method is a synchronous method. Do not call it in the main thread. Otherwise, the main thread may be blocked.
     */
    private void deleteToken() {
        showLog("deleteToken:begin");
        new Thread() {
            @Override
            public void run() {
                try {
                    // read from agconnect-services.json
                    String appId = "Please enter your App_Id from agconnect-services.json ";
                    HmsInstanceId.getInstance(MainActivity.this).deleteToken(appId, "HCM");
                    Log.i(TAG, "deleteToken success.");
                    showLog("deleteToken success");
                } catch (ApiException e) {
                    Log.e(TAG, "deleteToken failed." + e);
                    showLog("deleteToken failed." + e);
                }
            }
        }.start();
    }

    /**
     * Set up enable or disable the display of notification messages.
     * @param enable enabled or not
     */
    private void setReceiveNotifyMsg(final boolean enable) {
        showLog("Control the display of notification messages:begin");
        if (enable) {
            HmsMessaging.getInstance(this).turnOnPush().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(Task<Void> task) {
                    if (task.isSuccessful()) {
                        showLog("turnOnPush Complete");
                        tvSetPush.setText(R.string.set_push_unable);
                    } else {
                        showLog("turnOnPush failed: cause=" + task.getException().getMessage());
                    }
                }
            });
        } else {
            HmsMessaging.getInstance(this).turnOffPush().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(Task<Void> task) {
                    if (task.isSuccessful()) {
                        showLog("turnOffPush Complete");
                        tvSetPush.setText(R.string.set_push_enable);
                    } else {
                        showLog("turnOffPush  failed: cause =" + task.getException().getMessage());
                    }
                }
            });
        }
    }

    /**
     * to subscribe to topics in asynchronous mode.
     */
    private void addTopic() {
        final TopicDialog topicDialog = new TopicDialog(this, true);
        topicDialog.setOnDialogClickListener(new OnDialogClickListener() {
            @Override
            public void onConfirmClick(String msg) {
                topicDialog.dismiss();
                try {
                    HmsMessaging.getInstance(MainActivity.this)
                        .subscribe(msg)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Log.i(TAG, "subscribe Complete");
                                    showLog("subscribe Complete");
                                } else {
                                    showLog("subscribe failed: ret=" + task.getException().getMessage());
                                }
                            }
                        });
                } catch (Exception e) {
                    showLog("subscribe failed: exception=" + e.getMessage());
                }
            }

            @Override
            public void onCancelClick() {
                topicDialog.dismiss();
            }
        });
        topicDialog.show();
    }

    /**
     * to unsubscribe to topics in asynchronous mode.
     */
    private void deleteTopic() {
        final TopicDialog topicDialog = new TopicDialog(this, false);
        topicDialog.setOnDialogClickListener(new OnDialogClickListener() {
            @Override
            public void onConfirmClick(String msg) {
                topicDialog.dismiss();
                try {
                    HmsMessaging.getInstance(MainActivity.this)
                        .unsubscribe(msg)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(Task<Void> task) {
                                if (task.isSuccessful()) {
                                    showLog("unsubscribe Complete");
                                } else {
                                    showLog("unsubscribe failed: ret=" + task.getException().getMessage());
                                }
                            }
                        });
                } catch (Exception e) {
                    showLog("unsubscribe failed: exception=" + e.getMessage());
                }
            }

            @Override
            public void onCancelClick() {
                topicDialog.dismiss();
            }
        });
        topicDialog.show();
    }

    /**
     * MyReceiver
     */
    public class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            if (bundle != null && bundle.getString("msg") != null) {
                String content = bundle.getString("msg");
                showLog(content);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    public void showLog(final String log) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                View tvView = findViewById(R.id.tv_log);
                View svView = findViewById(R.id.sv_log);
                if (tvView instanceof TextView) {
                    ((TextView) tvView).setText(log);
                }
                if (svView instanceof ScrollView) {
                    ((ScrollView) svView).fullScroll(View.FOCUS_DOWN);
                }
            }
        });
    }

    private void sendRegTokenToServer(String token) {
        Log.i(TAG, "sending token to server. token:" + token);
    }

    /**
     * In Opening a Specified Page of an App, how to Generate Intent parameters.
     */
    private void generateIntentUri() {
        Intent intent = new Intent(Intent.ACTION_VIEW);

        // You can add parameters in either of the following ways:
        // Define a scheme protocol, for example, pushscheme://com.huawei.codelabpush/deeplink?.
        // way 1 start: Use ampersands (&) to separate key-value pairs. The following is an example:
         intent.setData(Uri.parse("pushscheme://com.huawei.codelabpush/deeplink?name=abc&age=180"));
        // way 1 end. In this example, name=abc and age=180 are two key-value pairs separated by an ampersand (&).

        // way 2 start: Directly add parameters to the Intent.
        // intent.setData(Uri.parse("pushscheme://com.huawei.codelabpush/deeplink?"));
        // intent.putExtra("name", "abc");
        // intent.putExtra("age", 180);
        // way 2 end.

        // The following flag is mandatory. If it is not added, duplicate messages may be displayed.
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        String intentUri = intent.toUri(Intent.URI_INTENT_SCHEME);
        // The value of intentUri will be assigned to the intent parameter in the message to be sent.
        Log.d("intentUri", intentUri);
        showLog(intentUri);

        // You can start the deep link activity with the following code.
        //intent.setClass(this, DeeplinkActivity.class);
        //startActivity(intent);
    }

    /**
     * Simulate pulling up the application custom page by action.
     */
    private void openActivityByAction() {
        Intent intent = new Intent("com.huawei.codelabpush.intent.action.test");

        // You can start the deep link activity with the following code.
        intent.setClass(this, Deeplink2Activity.class);
        startActivity(intent);
    }

    private void isAutoInitEnabled() {
        Log.i(TAG, "isAutoInitEnabled:" + HmsMessaging.getInstance(this).isAutoInitEnabled());
        showLog("isAutoInitEnabled:" + HmsMessaging.getInstance(this).isAutoInitEnabled());
    }

    private void setAutoInitEnabled(final boolean enable) {
        if (enable) {
            HmsMessaging.getInstance(this).setAutoInitEnabled(true);
            Log.i(TAG, "setAutoInitEnabled: true");
            showLog("setAutoInitEnabled: true");
            tvSetAutoInit.setText(R.string.AutoInitDisabled);
        } else {
            HmsMessaging.getInstance(this).setAutoInitEnabled(false);
            Log.i(TAG, "setAutoInitEnabled: false");
            showLog("setAutoInitEnabled: false");
            tvSetAutoInit.setText(R.string.AutoInitEnabled);
        }
    }
}
