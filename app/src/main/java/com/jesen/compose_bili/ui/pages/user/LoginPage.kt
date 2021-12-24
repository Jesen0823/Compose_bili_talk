package com.jesen.compose_bili.ui.pages.user

import androidx.activity.viewModels
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Lock
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
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
import com.jesen.common_util_lib.utils.showAlertDialog
import com.jesen.compose_bili.R
import com.jesen.compose_bili.navigation.NavUtil
import com.jesen.compose_bili.navigation.PageRoute
import com.jesen.compose_bili.ui.widget.user.InputTextField
import com.jesen.compose_bili.ui.widget.user.InputTogButton
import com.jesen.compose_bili.ui.widget.user.TopBarView
import com.jesen.compose_bili.utils.LoadingLottieUI
import com.jesen.compose_bili.utils.replaceRegex
import com.jesen.compose_bili.viewmodel.InputViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@ExperimentalComposeUiApi
@ExperimentalFoundationApi
@ExperimentalMaterialApi
@ExperimentalAnimationApi
@ExperimentalPagerApi
@Composable
fun LoginPage() {
    val activity = LocalMainActivity.current
    val inputViewModel by activity.viewModels<InputViewModel>()
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    var isLoading by remember { mutableStateOf(false) }

    val navController = LocalNavController.current
    val context = LocalContext.current

    LaunchedEffect(key1 = inputViewModel.userUIState) {
        activity.lifecycleScope.launch {
            inputViewModel.userUIState.collect {
                when (it) {
                    is InputViewModel.UserUIState.Success -> {
                        // 登录成功
                        isLoading = false
                        oLog(" login page success :${it.result.code}")
                        NavUtil.doPageNavigationTo(
                            navController = navController,
                            route = replaceRegex(PageRoute.MAIN_PAGE, "0"),
                            allowBack = false
                        )
                    }
                    is InputViewModel.UserUIState.Error -> {
                        isLoading = false
                        scaffoldState.snackbarHostState.showSnackbar(it.message)
                        oLog(" login page error :${it.message}")
                    }
                    is InputViewModel.UserUIState.Loading -> {
                        isLoading = true
                        oLog(" login...")
                    }
                    else -> Unit
                }
            }
        }
    }

    // 前一个栈信息如果是注册，则弹出自动登录对话框
    val preBackDesRoute = navController.previousBackStackEntry?.destination?.route
    oLog("LoginPage ,preBackDesRoute: $preBackDesRoute")
    if (preBackDesRoute == PageRoute.REGISTER_ROUTE) {
        showAlertDialog(
            titleStr = context.getString(R.string.alert_login_title),
            contentStr = context.getString(R.string.alert_login_content),
            confirmText = context.getString(R.string.alert_confire_t),
            cancelText = context.getString(R.string.alert_cancel_t),
            confirmClick = { inputViewModel.defaultLogin() },
            dismissClick = {}
        )
    }

    Scaffold(
        topBar = { LoginTopBarView(scope) },
        scaffoldState = scaffoldState
    ) {
        Box(modifier = Modifier.fillMaxSize()) {

            HeaderEffect(inputViewModel)

            Column(
                modifier = Modifier
                    .padding(5.dp, 120.dp, 5.dp, 0.dp)
                    .fillMaxWidth()
            ) {
                Spacer(modifier = Modifier.height(2.dp))
                InputLoginScreen(inputViewModel, scaffoldState, scope)
            }
        }

        oLog("isLoad: $isLoading")
        if (isLoading) LoadingLottieUI(message = "登录中...")
    }

    DisposableEffect(key1 =Unit) {
        onDispose {
            inputViewModel.clearState()
        }
    }
}

@Composable
fun InputLoginScreen(
    viewModel: InputViewModel,
    scaffoldState: ScaffoldState,
    scope: CoroutineScope,
) {
    Column(
        Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
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

        InputTogButton(
            "登录",
            scope,
            viewModel,
            scaffoldState,
            onClick = { viewModel.doLogin() },
            true
        )
    }

}


@Composable
fun HeaderEffect(viewModel: InputViewModel) {
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
            modifier = Modifier
                .height(54.dp)
                .width(126.dp)
                .padding(top = 8.dp),
            alignment = Alignment.BottomCenter,
            painter = painterResource(id = R.drawable.logo2),
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
fun LoginTopBarView(scope: CoroutineScope) {
    val navController = LocalNavController.current
    TopBarView(
        iconEvent = {
            IconButton(onClick = {
                NavUtil.doPageNavBack(navController, route = null)
            }) {
                Icon(Icons.Filled.ArrowBack, null)
            }
        },
        actionEvent = {
            TextButton(onClick = {
                scope.launch { NavUtil.doPageNavigationTo(navController, PageRoute.REGISTER_ROUTE) }

            }) {
                Text(text = "注册", color = Color.Gray, fontSize = 18.sp)
            }
        },
        titleText = "密码登录"
    )
}
