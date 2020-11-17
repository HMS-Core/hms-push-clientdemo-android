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
import android.app.Dialog;
import android.content.Context;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

public class TopicDialog extends Dialog implements View.OnClickListener {
    private View view;

    private OnDialogClickListener onDialogClickListener;

    private EditText edTopic;

    @SuppressLint("InflateParams")
    public TopicDialog(Context context, boolean isAdd) {
        super(context, R.style.custom_dialog);
        view = LayoutInflater.from(context).inflate(R.layout.dialog_add_topic, null);
        initView(isAdd, context);
    }

    private void initView(boolean isAdd, final Context context) {
        view.findViewById(R.id.tv_cancel).setOnClickListener(this);
        view.findViewById(R.id.tv_confirm).setOnClickListener(this);
        edTopic = view.findViewById(R.id.ed_topic);

        edTopic.setHint(isAdd ? R.string.add_topic : R.string.delete_topic);

        edTopic.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_UNSPECIFIED) {
                    //
                    InputMethodManager imm =
                        (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm != null) {
                        imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
                    }
                    return true;
                }
                return false;
            }
        });

        setCanceledOnTouchOutside(false);
        setContentView(view);
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        switch (viewId) {
            case R.id.tv_cancel:
                if (onDialogClickListener != null) {
                    onDialogClickListener.onCancelClick();
                }
                break;
            case R.id.tv_confirm:
                if (onDialogClickListener != null) {
                    onDialogClickListener.onConfirmClick(edTopic.getText().toString());
                }
                break;
            default:
                break;
        }
    }

    public void setOnDialogClickListener(OnDialogClickListener onDialogClickListener) {
        this.onDialogClickListener = onDialogClickListener;
    }
}
