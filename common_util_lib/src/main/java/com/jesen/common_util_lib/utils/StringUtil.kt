package com.jesen.common_util_lib.utils

import android.util.Log

/// 时间转换将秒转换为分钟:秒
fun durationTransform(seconds: Int): String {
    val m: Int = (seconds / 60)
    val s = seconds - m * 60
    return if (s < 10) "$m:0$s" else "$m:$s"
}

/// 数字转万
fun countFormat(count: Int): String = if (count > 9999) {
    "${(count / 10000)}万"
} else {
    count.toString()
}

fun oLog(msg: String) {
    Log.d("Bi--", msg)
}
