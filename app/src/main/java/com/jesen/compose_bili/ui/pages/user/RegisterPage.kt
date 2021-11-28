package com.jesen.compose_bili.ui.pages.user

import androidx.activity.ComponentActivity
import androidx.activity.viewModels
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import com.jesen.compose_bili.R
import com.jesen.compose_bili.navigation.PageRoute
import com.jesen.compose_bili.navigation.doPageNavBack
import com.jesen.compose_bili.navigation.doPageNavigationTo
import com.jesen.compose_bili.ui.widget.user.ActionResult
import com.jesen.compose_bili.ui.widget.user.InputTextField
import com.jesen.compose_bili.ui.widget.user.InputTogButton
import com.jesen.compose_bili.ui.widget.user.TopBarView
import com.jesen.compose_bili.utils.oLog
import com.jesen.compose_bili.viewmodel.InputViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@Composable
fun RegisterPage(activity: ComponentActivity) {

    val inputViewModel by activity.viewModels<InputViewModel>()
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()

    // 注册UI更新，结果处理
    ActionResult(inputViewModel, scaffoldState, scope)

    Scaffold(
        topBar = { RegisterTopBarView(scope) },
        scaffoldState = scaffoldState
    ) {

        Box(Modifier.fillMaxSize()) {

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

        InputTextField(
            label = "用户名",
            value = viewModel.name,
            hint = "请输入用户名",
            onValueChanged = { viewModel.onNameChange(it) },
            type = "userName",
            viewModel = viewModel,
            leadingIcon = Icons.Default.AccountBox,
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


@Composable
fun RegisterTopBarView(scope: CoroutineScope) {
    TopBarView(
        iconEvent = {
            IconButton(onClick = {
                doPageNavBack(route = null)
            }) {
                Icon(Icons.Filled.ArrowBack, null)
            }
        },
        actionEvent = {
            TextButton(onClick = {
                scope.launch { doPageNavigationTo(PageRoute.LOGIN_ROUTE) }

            }) {
                Text(text = "登录", color = Color.Gray, fontSize = 18.sp)
            }
        },
        titleText = "账号注册"
    )
}