package com.jesen.compose_bili.utils

import com.jesen.compose_bili.BiliApp

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

fun getResString(resId: Int) = BiliApp.mContext.applicationContext.getString(resId)
