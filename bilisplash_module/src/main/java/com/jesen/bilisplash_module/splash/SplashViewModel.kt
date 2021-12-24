package com.jesen.bilisplash_module.splash

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jesen.common_util_lib.datastore.DataStoreUtil
import com.jesen.retrofit_lib.com.BOARDING_PASS
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

class SplashViewModel : ViewModel() {

    var isEndTask by mutableStateOf(false)
    var checkingLoginState = mutableStateOf(false)
    var loginState by mutableStateOf(-1)

    // 检查登录
    private fun checkLogin() {
        checkingLoginState.value = true
        viewModelScope.launch {

            // 查询登录令牌是否已保存来模拟判断是否已经登录
            flow {
                delay(5000)
                val boardingPass = DataStoreUtil.readStringData(BOARDING_PASS)
                val result = if (boardingPass.isBlank()) 0 else 1
                emit(result)
            }.flowOn(Dispatchers.IO).collect {
                Log.d("splash--:", "loginState:$it")
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