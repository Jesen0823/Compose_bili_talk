package com.jesen.compose_bili.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jesen.common_util_lib.datastore.DataStoreUtil
import com.jesen.common_util_lib.utils.oLog
import com.jesen.compose_bili.repository.UserRepository
import com.jesen.retrofit_lib.com.AUTH_TOKEN_K
import com.jesen.retrofit_lib.com.AUTH_TOKEN_V
import com.jesen.retrofit_lib.com.BOARDING_PASS
import com.jesen.retrofit_lib.model.UserResult
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

    // 默认登录 仅供测试
    fun defaultLogin() = viewModelScope.launch {
        _loginUIState.value = UserUIState.Loading(UserAction.LOGIN)
        delay(2000)
        flow {
            val result: UserResult = UserResult(
                code = 0,
                msg = "登录成功",
                data = "E793ED7A61088AAA70DD32614448F2C4AF",
                extra = "默认账号"
            )
            emit(result)
        }.flowOn(Dispatchers.IO)
            .collect { userResult ->
                // 存储登录令牌，方便其他API调用
                saveUserToken(userResult, UserAction.LOGIN)
                userResult.data = AUTH_TOKEN_V
                saveUserToken(userResult, UserAction.REGISTER)

                // 更新UI状态
                _loginUIState.value =
                    UserUIState.Success(result = userResult, action = UserAction.LOGIN)
            }
    }

    /**
     * 登录行为
     * */
    fun doLogin() = userRequest(UserAction.LOGIN)

    /**
     * 注册行为
     * */
    fun doRegister() = userRequest(UserAction.REGISTER)


    private fun userRequest(action: UserAction) = viewModelScope.launch {
        _loginUIState.value = UserUIState.Loading(action)
        delay(1500)
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
                // 存储登录令牌，方便其他API调用
                saveUserToken(userResult, action = action)

                // 更新UI状态
                _loginUIState.value =
                    UserUIState.Success(result = userResult, action = action)
            }
    }

    /**
     * 保存登录令牌
     * 登录后保存，其他页面API会用到
     */
    private fun saveUserToken(userResult: UserResult, action: UserAction) {
        userResult.data?.let {
            if (userResult.code == 0 && it.isNotEmpty()) {
                viewModelScope.launch {
                    if (action == UserAction.LOGIN) {
                        DataStoreUtil.saveStringData(BOARDING_PASS, it)
                    } else {
                        DataStoreUtil.saveStringData(AUTH_TOKEN_K, it)
                    }
                }
            }
        }
    }

    fun clearState() {
        _loginUIState.value = UserUIState.Empty(action = null)
    }

    /**
     * UI状态管理
     * @param action 用户操作类型 是注册/登录/登出/
     */
    sealed class UserUIState(action: UserAction?) {
        data class Success(val result: UserResult, val action: UserAction) : UserUIState(action)
        data class Error(val message: String, val action: UserAction) : UserUIState(action)
        data class Loading(val action: UserAction) : UserUIState(action)
        data class Empty(val action: UserAction?) : UserUIState(action)
    }

    // 用户行为
    enum class UserAction { LOGIN, REGISTER }

}