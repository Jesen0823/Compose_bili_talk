package com.jesen.compose_bili.ui.pages.user

import androidx.activity.viewModels
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import com.google.accompanist.pager.ExperimentalPagerApi
import com.jesen.common_util_lib.utils.LocalMainActivity
import com.jesen.common_util_lib.utils.LocalNavController
import com.jesen.common_util_lib.utils.oLog
import com.jesen.compose_bili.R
import com.jesen.compose_bili.navigation.NavUtil
import com.jesen.compose_bili.navigation.PageRoute
import com.jesen.compose_bili.ui.widget.user.InputTextField
import com.jesen.compose_bili.ui.widget.user.InputTogButton
import com.jesen.compose_bili.ui.widget.user.TopBarView
import com.jesen.compose_bili.utils.LoadingLottieUI
import com.jesen.compose_bili.viewmodel.InputViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@ExperimentalPagerApi
@ExperimentalAnimationApi
@ExperimentalMaterialApi
@ExperimentalFoundationApi
@Composable
fun RegisterPage() {

    val activity = LocalMainActivity.current
    val inputViewModel by activity.viewModels<InputViewModel>()
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    var isLoading by remember { mutableStateOf(false) }

    val navController = LocalNavController.current
    // 注册UI更新，结果处理
    LaunchedEffect(key1 = inputViewModel.userUIState) {
        activity.lifecycleScope.launch {
            inputViewModel.userUIState.collect {
                when (it) {
                    is InputViewModel.UserUIState.Success -> {
                        // 注册成功
                        isLoading = false
                        scaffoldState.snackbarHostState.showSnackbar("注册成功")
                        oLog(" register page success :${it.result.code}")

                        NavUtil.doPageNavigationTo(navController=navController, route = PageRoute.LOGIN_ROUTE, allowBack = false)
                    }
                    is InputViewModel.UserUIState.Error -> {
                        isLoading = false
                        scaffoldState.snackbarHostState.showSnackbar(it.message)
                        oLog(" register page error :${it.message}")
                    }
                    is InputViewModel.UserUIState.Loading -> {
                        isLoading = true
                    }
                    else -> Unit
                }
            }
        }
    }

    Scaffold(
        topBar = { RegisterTopBarView(scope) },
        scaffoldState = scaffoldState
    ) {

        Box(modifier = Modifier.fillMaxSize()) {

            headPicEffect(inputViewModel)

            Column(
                modifier = Modifier
                    .padding(5.dp, 120.dp, 5.dp, 0.dp)
                    .fillMaxWidth()
            ) {
                Spacer(
                    modifier = Modifier
                        .height(2.dp)
                        .background(color = Color.Gray)
                )
                InputRegisterScreen(inputViewModel, scaffoldState, scope)
            }

            if (isLoading) LoadingLottieUI(message = "正在注册...")
        }
    }
}


@Composable
fun InputRegisterScreen(
    viewModel: InputViewModel,
    scaffoldState: ScaffoldState,
    scope: CoroutineScope,
) {
    Column(
        Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 密码是否明文
        val showPwd by remember {
            mutableStateOf(true)
        }
        val focusManager = LocalFocusManager.current

        InputTextField(
            label = "用户名",
            value = viewModel.name,
            hint = "请输入用户名",
            onValueChanged = { viewModel.onNameChange(it) },
            type = "userName",
            viewModel = viewModel,
            leadingIcon = Icons.Default.AccountBox,
            focusManager = focusManager,
        )
        InputTextField(
            label = "密码",
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            value = viewModel.pwd,
            hint = "请输入密码",
            onValueChanged = { viewModel.onPwdChange(it) },
            type = "password",
            viewModel = viewModel,
            leadingIcon = Icons.Default.Lock,
            focusManager = focusManager,
        )
        InputTextField(
            label = "密码确认",
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            value = viewModel.rePassword,
            hint = "请再次输入密码",
            onValueChanged = { viewModel.onRePwdChange(it) },
            type = "rePassword",
            viewModel = viewModel,
            leadingIcon = Icons.Default.LockOpen,
            focusManager = focusManager,
        )
        InputTextField(
            label = "身份",
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            value = viewModel.mocId,
            hint = "请输入身份凭证",
            onValueChanged = { viewModel.onMocIdChange(it) },
            type = "mocId",
            viewModel = viewModel,
            leadingIcon = Icons.Default.VpnKey,
            focusManager = focusManager,
        )
        InputTextField(
            label = "服务id",
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            value = viewModel.orderId,
            hint = "请输入服务ID后四位",
            onValueChanged = { viewModel.onOrderIdChange(it) },
            type = "orderId",
            viewModel = viewModel,
            leadingIcon = Icons.Default.AdminPanelSettings,
            focusManager = focusManager,
        )

        InputTogButton("注册", scope, viewModel, scaffoldState, onClick = { viewModel.doRegister() })
    }

}


@Composable
fun headPicEffect(viewModel: InputViewModel) {
    Row(
        modifier = Modifier
            .fillMaxSize(), horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Image(
            modifier = Modifier.size(120.dp, 120.dp),
            painter = painterResource(id = if (viewModel.isHide) R.drawable.head_left_protect else R.drawable.head_left),
            contentDescription = "left image"
        )
        Image(
            alignment = Alignment.BottomCenter,
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "mid image"
        )
        Image(
            modifier = Modifier.size(120.dp, 120.dp),
            painter = painterResource(id = if (viewModel.isHide) R.drawable.head_right_protect else R.drawable.head_right),
            contentDescription = "right image"
        )
    }
}


@ExperimentalPagerApi
@ExperimentalAnimationApi
@ExperimentalMaterialApi
@ExperimentalFoundationApi
@Composable
fun RegisterTopBarView(scope: CoroutineScope) {
    val navController = LocalNavController.current
    TopBarView(
        iconEvent = {
            IconButton(onClick = {
                scope.launch{
                    NavUtil.doPageNavBack(navController,route = null)
                }
            }) {
                Icon(Icons.Filled.ArrowBack, null)
            }
        },
        actionEvent = {
            TextButton(onClick = {
                scope.launch { NavUtil.doPageNavigationTo(navController,PageRoute.LOGIN_ROUTE) }

            }) {
                Text(text = "登录", color = Color.Gray, fontSize = 18.sp)
            }
        },
        titleText = "账号注册"
    )
}