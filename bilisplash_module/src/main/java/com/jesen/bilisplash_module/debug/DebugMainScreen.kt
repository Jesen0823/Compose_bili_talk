package com.jesen.bilisplash_module.debug

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

/**
 * 假设这是主页
 * */
@Composable
fun DebugMainScreen(
    navController: NavController,
    name: String,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "首页") },
                actions = {
                    IconButton(
                        modifier = Modifier.padding(end = 16.dp),
                        onClick = {
                            val username = "火狐"
                            val age = 3
                            val route = "common/profile?userName=$username&age=$age"
                            navController.navigate(route)
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = "去个人中心"
                        )
                    }
                }
            )
        },
    ) {
        val columnState = rememberScrollState()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(enabled = true, state = columnState)
        ) {
            Text(text = "Hello $name! ", fontSize = 20.sp, fontStyle = FontStyle.Italic)
            Text(text = "$test", modifier = Modifier.padding(top = 8.dp))
        }
    }


}