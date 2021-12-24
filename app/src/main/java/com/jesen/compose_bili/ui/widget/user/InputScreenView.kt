package com.jesen.compose_bili.ui.widget.user

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jesen.compose_bili.ui.theme.primaryColor
import com.jesen.compose_bili.ui.theme.primaryDeepColor
import com.jesen.compose_bili.viewmodel.InputViewModel
import kotlinx.coroutines.CoroutineScope

/**
 * 顶部TopBar
 * */
@Composable
fun TopBarView(
    iconEvent: @Composable (() -> Unit)? = null,
    titleText: String,
    actionEvent: @Composable RowScope.() -> Unit = {},
) {
    TopAppBar(
        title = {
            Text(
                text = titleText,
                color = Color.Black
            )
        },
        navigationIcon = iconEvent,
        actions = actionEvent,
        // below line is use to give background color
        backgroundColor = Color.White,
        contentColor = Color.Black,
        elevation = 12.dp
    )
}

/**
 * 登录注册输入框
 * */
@Composable
fun InputTextField(
    label: String,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    value: String,
    hint: String,
    onValueChanged: (String) -> Unit,
    type: String? = null,
    viewModel: InputViewModel,
    leadingIcon: ImageVector,
    focusManager: FocusManager,
) {

    // 密码输入类型设置
    val visualTransformation =
        if (!viewModel.showPwd && (type == "password" || type == "rePassword")) PasswordVisualTransformation() else VisualTransformation.None
    TextField(
        value = value,
        onValueChange = onValueChanged,
        textStyle = LocalTextStyle.current,
        label = { Text(text = label) },
        visualTransformation = visualTransformation,
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Next
        ),
        shape = MaterialTheme.shapes.medium,
        modifier = Modifier
            .fillMaxWidth()
            .onFocusChanged {
                viewModel.onFocusHide(it.isFocused && (type == "password" || type == "rePassword"))
            },
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = Color.White,
            leadingIconColor = primaryColor,
            focusedIndicatorColor = primaryColor,
        ),
        placeholder = { Text(hint) },
        leadingIcon = {
            Icon(
                imageVector = leadingIcon,
                contentDescription = null
            )
        },
        keyboardActions = KeyboardActions(
            onNext = {
                focusManager.moveFocus(FocusDirection.Down)
            }
        ),
        trailingIcon = {
            if (type == "password") {
                if (viewModel.showPwd) {
                    IconButton(onClick = { viewModel.onShowPwdChange(false) }) {
                        Icon(
                            Icons.Outlined.Visibility,
                            contentDescription = null,
                            Modifier.size(30.dp)
                        )
                    }
                } else {
                    IconButton(onClick = { viewModel.onShowPwdChange(true) }) {
                        Icon(
                            Icons.Outlined.VisibilityOff,
                            contentDescription = null,
                            Modifier.size(30.dp)
                        )
                    }
                }
            }
        }
    )
}

/**
 * 登录注册按钮
 * */
@Composable
fun InputTogButton(
    text: String,
    scope: CoroutineScope,
    viewModel: InputViewModel,
    scaffoldState: ScaffoldState,
    onClick: () -> Unit,
    isLoginType: Boolean = false,
) {
    val isEnabled = if (isLoginType) {
        viewModel.name.isNotBlank() && viewModel.pwd.isNotBlank()
    } else {
        viewModel.name.isNotBlank() && viewModel.pwd.isNotBlank() &&
                viewModel.rePassword.isNotBlank() && viewModel.mocId.isNotBlank() &&
                viewModel.orderId.isNotBlank()
    }
    Button(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 10.dp, top = 20.dp, end = 10.dp, bottom = 0.dp),
        onClick = onClick,
        enabled = isEnabled,
        shape = RoundedCornerShape(50),
        colors = ButtonDefaults.buttonColors(
            disabledBackgroundColor = primaryColor,
            backgroundColor = primaryDeepColor
        ),
        contentPadding = PaddingValues(12.dp, 16.dp)
    ) {
        Text(text, color = Color.White, fontSize = 18.sp)
    }
}
