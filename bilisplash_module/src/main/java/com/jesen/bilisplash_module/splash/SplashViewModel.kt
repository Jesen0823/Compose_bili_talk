package com.jesen.bilisplash_module.splash

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlin.random.Random

class SplashViewModel : ViewModel() {

    var isEndTask by mutableStateOf(false)
    var checkingLoginState = mutableStateOf(false)
    var loginState by mutableStateOf(-1)

    // 检查登录
    private fun checkLogin() {
        checkingLoginState.value = true
        viewModelScope.launch {
            // 模拟判断是否登录
            flow {
                delay(5000)
                val result = Random.nextInt(0, 2)
                emit(result)
            }.flowOn(Dispatchers.IO).collect {
                Log.d("splash--:", "islogin:$it")
                loginState = it
                checkingLoginState.value = false
            }
        }
    }

    fun checkLoginState() {
        viewModelScope.launch {
            delay(1000)
            checkLogin()
            isEndTask = true
        }
    }
}