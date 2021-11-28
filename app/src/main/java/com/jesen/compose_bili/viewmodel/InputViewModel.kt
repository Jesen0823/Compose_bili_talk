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
import kotlinx.coroutines.delay
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

    private val _loginUIState = MutableStateFlow<UserUIState>(UserUIState.Empty(action = null))

    val userUIState: StateFlow<UserUIState> = _loginUIState

    /**
     * 登录行为
     * */
    fun doLogin() = userRequest(UserAction.LOGIN)

    /**
     * 登录行为
     * */
    fun doRegister() = userRequest(UserAction.REGISTER)


    private fun userRequest(action: UserAction) = viewModelScope.launch {
        _loginUIState.value = UserUIState.Loading(action)
        delay(2000)
        flow {
            val result: UserResult = if (action == UserAction.LOGIN) {
                UserRepository.startLogin(name, pwd)
            } else {
                UserRepository.startRegister(name, pwd, mocId.toInt(), orderId.toInt())
            }

            oLog(" login result: ${result.msg}")
            emit(result)

        }.flowOn(Dispatchers.IO)
            // 下游异常 接口返回错误码
            .onEach {
                oLog("onEach : ${it.code}")
                _loginUIState.value =
                    UserUIState.Error(
                        message = it.msg ?: "login || register error",
                        action = action
                    )
            }
            // 上游异常捕获 如网络异常功能异常
            .catch { e ->
                oLog(" login || register result catch: ${e.message}")
                e.printStackTrace()

                _loginUIState.value = when (e) {
                    is IOException -> UserUIState.Error("网络异常，请检查网络", action = action)
                    else -> UserUIState.Error(
                        message = e.message ?: "unKnow error",
                        action = action
                    )
                }
            }
            .collect { userResult ->
                _loginUIState.value =
                    UserUIState.Success(result = userResult, action = action)
            }
    }


    // UI状态管理
    sealed class UserUIState(action: UserAction?) {
        data class Success(val result: UserResult, val action: UserAction) : UserUIState(action)
        data class Error(val message: String, val action: UserAction) : UserUIState(action)
        data class Loading(val action: UserAction) : UserUIState(action)
        data class Empty(val action: UserAction?) : UserUIState(action)
    }

    // 用户行为
    enum class UserAction { LOGIN, REGISTER }

}