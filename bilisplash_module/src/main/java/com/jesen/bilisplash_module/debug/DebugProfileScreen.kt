package com.jesen.bilisplash_module.debug

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.google.accompanist.insets.statusBarsHeight

@Composable
fun DebugProfileScreen(navController: NavController, userName: String?, age: String?) {

    Column() {
        Spacer(modifier = Modifier
            .statusBarsHeight()
            .fillMaxWidth())
        Text("个人中心")
        Text("$userName:$age")
    }
}