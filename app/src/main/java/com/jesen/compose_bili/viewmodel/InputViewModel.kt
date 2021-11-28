package com.jesen.compose_bili.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jesen.compose_bili.network.UserRepository
import com.jesen.compose_bili.model.UserResult
import com.jesen.compose_bili.utils.oLog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import okio.IOException

/**
 * 登录注册页面状态保持
 * */
class InputViewModel : ViewModel() {

    var rePassword by mutableStateOf("")
    var mocId by mutableStateOf("")
    var orderId by mutableStateOf("")

    // 状态
    var name by mutableStateOf("")
    var pwd by mutableStateOf("")
    var isHide by mutableStateOf(false)

    // 密码是否展示明文
    var showPwd by mutableStateOf(true)

    // 事件
    fun onNameChange(str: String) {
        name = str
    }

    fun onPwdChange(str: String) {
        pwd = str
    }

    fun onFocusHide(hide: Boolean) {
        isHide = hide
    }

    fun onRePwdChange(str: String) {
        rePassword = str
    }

    fun onMocIdChange(str: String) {
        mocId = str
    }

    fun onOrderIdChange(str: String) {
        orderId = str
    }

    fun onShowPwdChange(show: Boolean) {
        showPwd = show
    }


    /***********************************************************/

    private val _loginUIState = MutableStateFlow<LoginUIState>(LoginUIState.Empty)

    val loginUIState:StateFlow<LoginUIState> = _loginUIState

    fun doLogin() = viewModelScope.launch {
        _loginUIState.value = LoginUIState.Loading
        flow {
            val result = UserRepository.startLogin(name,pwd)
            oLog(" login result: ${result.msg}")

            emit(result)
        }.flowOn(Dispatchers.IO)
            // 下游异常 接口返回错误码
            .onEach {
                oLog("onEach : ${it.code}")
                _loginUIState.value = LoginUIState.Error(message = it.msg?:"login error")
            }
            // 上游异常捕获 如网络异常功能异常
            .catch { e ->
                oLog(" login result catch: ${e.message}")
                e.printStackTrace()

                _loginUIState.value = when(e){
                    is IOException -> LoginUIState.Error("网络异常，请检查网络")
                    else -> LoginUIState.Error(message = e.message?:"unKnow error")
                }
            }
            .collect { userResult ->
                _loginUIState.value = LoginUIState.Success(result=userResult)
            }
    }

    sealed class LoginUIState{
        data class Success(val result: UserResult):LoginUIState()
        data class Error(val message:String):LoginUIState()
        object Loading:LoginUIState()
        object Empty:LoginUIState()
    }

}