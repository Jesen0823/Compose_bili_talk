package com.jesen.videodetail_model.ui.widget

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jesen.videodetail_model.R
import com.jesen.videodetail_model.ui.theme.bili_50
import com.jesen.videodetail_model.ui.theme.gray200
import com.jesen.videodetail_model.ui.theme.gray400

/**
 * 评论底部输入框
 * */
@Composable
fun CommentBottomInput(
    modifier: Modifier,
    onSend: (String) -> Unit,
) {
    var inputValue by remember { mutableStateOf("") }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(60.dp)
            .background(color = Color.White, shape = RectangleShape)
    ) {
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(color = gray200)
        )
        TextField(
            value = inputValue,
            onValueChange = {
                inputValue = it
            },
            modifier = Modifier
                .height(48.dp)
                .width(320.dp)
                .padding(start = 16.dp)
                .align(Alignment.CenterStart),
            placeholder = {
                Text(
                    modifier = Modifier.padding(start = 8.dp),
                    text = "发一条友善的评论 ~☺~",
                    style = TextStyle(fontSize = 14.sp)
                )
            },
            shape = MaterialTheme.shapes.medium.copy(all = CornerSize(24.dp)),
            colors = TextFieldDefaults.textFieldColors(
                cursorColor = bili_50,
                // 将指示器所有状态颜色都设置透明
                focusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                backgroundColor = gray200
            ),
            // 完成时动作自定义处理
            keyboardActions = KeyboardActions(onDone = { onSend(inputValue) }),
            textStyle = TextStyle(color = gray400),
            // 会指定一个按钮，比如Search, Done,等等
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done
            )
        )
        // 这里设计为：如果有输入文字，展示按钮“发送”；否则展示Image
        if (inputValue.isBlank()) {
            Image(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 16.dp, top = 4.dp, bottom = 4.dp)
                    .size(48.dp),
                painter = painterResource(id = R.drawable.emj), contentDescription = "",
                colorFilter = ColorFilter.tint(color = bili_50),
            )
        } else {
            TextButton(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 16.dp, top = 4.dp, bottom = 4.dp),
                onClick = { onSend(inputValue) },
                colors = ButtonDefaults.textButtonColors(contentColor = bili_50)
            ) {
                Text(text = "发送", fontSize = 18.sp)
            }
        }
    }
}