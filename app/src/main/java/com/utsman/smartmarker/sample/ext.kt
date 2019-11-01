package com.utsman.smartmarker.sample

import android.content.Context
import android.util.Log
import android.widget.Toast

fun logi(msg: String?) = Log.i("anjay", msg)
fun loge(msg: String?) = Log.e("anjay", msg)

fun Context.toast(msg: String) = Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()