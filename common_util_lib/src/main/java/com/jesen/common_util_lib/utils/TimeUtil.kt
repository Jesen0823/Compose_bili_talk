package com.jesen.common_util_lib.utils

import java.text.ParsePosition
import java.text.SimpleDateFormat

object TimeUtil {

    fun transToYMDhms(time: Long): String = SimpleDateFormat("yyyy-MM-DD-hh-mm-ss").format(time)

    fun transToYMD(time: Long): String = SimpleDateFormat("yyyy-MM-DD").format(time)

    fun transToTimeStamp(date: String): Long =
        SimpleDateFormat("yyyy-MM-DD-hh-mm-ss").parse(date, ParsePosition(0)).time
}