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

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

/**
 * Opening a Specified Page of an App, and Receive data in the customized Activity class.
 */
public class DeeplinkActivity extends Activity {
    private static final String TAG = "PushDemoLog";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deeplink);
        getIntentData(getIntent());
    }
    private void getIntentData(Intent intent) {
        if (intent != null) {
            // Obtain data set in way 1.
            int iAge1 = 0;
            String name1 = null;
            try {
                Uri uri = intent.getData();
                if (uri == null) {
                    Log.e(TAG, "getData null");
                    return;
                }
                String age1 = uri.getQueryParameter("age");
                name1 = uri.getQueryParameter("name");

                if (age1 != null) {
                    iAge1 = Integer.parseInt(age1);
                }
            } catch (NullPointerException e) {
                Log.e(TAG, "NullPointer," + e);
            } catch (NumberFormatException e) {
                Log.e(TAG, "NumberFormatException," + e);
            } catch (UnsupportedOperationException e) {
                Log.e(TAG, "UnsupportedOperationException," + e);
            } finally {
                Log.i(TAG, "name " + name1 + ",age " + iAge1);
                Toast.makeText(this, "name " + name1 + ",age " + iAge1, Toast.LENGTH_SHORT).show();
            }

            // Obtain data set in way 2.
            // String name2 = intent.getStringExtra("name");
            // int age2 = intent.getIntExtra("age", -1);
        }
    }
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        getIntentData(intent);
    }
}
