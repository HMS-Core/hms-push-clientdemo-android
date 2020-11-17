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
import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import android.widget.TextView.OnEditorActionListener

class TopicDialog @SuppressLint("InflateParams") constructor(context: Context, isAdd: Boolean) : Dialog(context, R.style.custom_dialog), View.OnClickListener {
    private val view: View = LayoutInflater.from(context).inflate(R.layout.dialog_add_topic, null)
    private var onDialogClickListener: OnDialogClickListener? = null
    private lateinit var edTopic: EditText
    private fun initView(isAdd: Boolean, context: Context?) {
        view.findViewById<TextView>(R.id.tv_cancel).setOnClickListener(this)
        view.findViewById<TextView>(R.id.tv_confirm).setOnClickListener(this)
        edTopic = view.findViewById(R.id.ed_topic)
        edTopic.setHint(if (isAdd) R.string.add_topic else R.string.delete_topic)
        edTopic.setOnEditorActionListener(OnEditorActionListener { _, i, _ ->
            if (i == EditorInfo.IME_ACTION_UNSPECIFIED) {
                //
                val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(window?.decorView?.windowToken, 0)
                return@OnEditorActionListener true
            }
            false
        })
        setCanceledOnTouchOutside(false)
        setContentView(view)
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.tv_cancel -> onDialogClickListener?.onCancelClick()
            R.id.tv_confirm -> onDialogClickListener?.onConfirmClick(edTopic.text.toString())
            else -> {
            }
        }
    }

    fun setOnDialogClickListener(onDialogClickListener: OnDialogClickListener?) {
        this.onDialogClickListener = onDialogClickListener
    }

    init {
        initView(isAdd, context)
    }
}