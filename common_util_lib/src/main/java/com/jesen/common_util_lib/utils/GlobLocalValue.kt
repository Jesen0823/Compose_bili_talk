package com.jesen.common_util_lib.utils

import android.content.res.Configuration
import androidx.activity.ComponentActivity
import androidx.compose.runtime.compositionLocalOf
import androidx.navigation.NavHostController

val LocalScreenOrientation = compositionLocalOf { Configuration.ORIENTATION_PORTRAIT }

val LocalNavController = compositionLocalOf<NavHostController> {
    error("Not Init")
}

val LocalMainActivity = compositionLocalOf<ComponentActivity> { error("Not init your Activity.") }
