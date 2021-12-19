package com.jesen.compose_bili.ui.widget

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jesen.common_util_lib.ui.theme.gray300
import com.jesen.compose_bili.R
import com.jesen.compose_bili.ui.theme.*
import com.jesen.compose_bili.viewmodel.SearchViewModel
import com.jesen.retrofit_lib.model.TransEntry

/**
 * 搜索页要用到的小部件
 * [ALL]
 * */


/**
 * 搜索输入框
 * */
@ExperimentalComposeUiApi
@Composable
fun SearchTopBar(
    needInputNow: String,
    onSearch: (String) -> Unit,
    onCancel: () -> Unit,
    onClearInput: () -> Unit
) {
    var inputValue by remember { mutableStateOf("") }

    if (needInputNow.isNotEmpty()) {
        inputValue = needInputNow
    }

    Row(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
            .height(48.dp)
            .background(color = Color.White, shape = RectangleShape),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 用来管理软键盘
        val keyboardController = LocalSoftwareKeyboardController.current
        val focusRequester = FocusRequester()

        Row(
            modifier = Modifier
                .width(320.dp)
                .background(color = gray200, shape = RoundedCornerShape(24.dp))
                .padding(end = 8.dp, start = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier
                    .size(36.dp)
                    .padding(start = 8.dp),
                imageVector = Icons.Rounded.Search,
                contentDescription = "search",
                tint = Color.Gray
            )
            TextField(
                modifier = Modifier
                    .focusRequester(focusRequester)
                    .onFocusChanged {
                        if (it.isFocused) {
                            keyboardController?.show()
                        }
                    }
                    .wrapContentHeight()
                    .width(210.dp),
                value = inputValue,
                onValueChange = {
                    inputValue = it
                    onSearch(inputValue)
                },
                placeholder = {
                    Text(
                        text = "面粉社长",
                        style = TextStyle(fontSize = 14.sp)
                    )
                },
                colors = TextFieldDefaults.textFieldColors(
                    cursorColor = bili_50,
                    // 将指示器所有状态颜色都设置透明
                    focusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    backgroundColor = gray200
                ),
                // 完成时动作自定义处理
                keyboardActions = KeyboardActions(onSearch = {
                    onSearch(inputValue)
                    keyboardController?.hide()
                }),
                textStyle = TextStyle(color = gray400),
                // 会指定一个按钮，比如Search, Done,等等
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Search
                )
            )
            // 这里设计为：如果有输入文字，展示清除按钮
            if (inputValue.isNotEmpty()) {
                IconButton(
                    modifier = Modifier
                        .padding(4.dp),
                    onClick = {
                        inputValue = ""
                        onClearInput()
                    },
                ) {
                    Icon(
                        modifier = Modifier
                            .size(28.dp)
                            .background(
                                color = gray300.copy(alpha = 0.3f),
                                shape = CircleShape
                            ),
                        imageVector = Icons.Rounded.Clear,
                        contentDescription = "clear",
                        tint = gray400
                    )
                }
            }
        }

        DisposableEffect(Unit) {
            focusRequester.requestFocus()
            onDispose { }
        }

        TextButton(
            modifier = Modifier
                .padding(start = 4.dp)
                .wrapContentSize(),
            onClick = { onCancel() },
            contentPadding = PaddingValues(5.dp),
            colors = ButtonDefaults.textButtonColors(contentColor = Color.White)
        ) {
            Text(text = "取消", fontSize = 18.sp, color = Color.Gray)
        }

    }
}


/**
 * 初始页面展示效果
 * 热词列表
 * */
@ExperimentalFoundationApi
@Composable
fun SearchDefaultUIShow(hotClick: (String) -> Unit) {
    val context = LocalContext.current
    val hotSearch = context.resources.getStringArray(R.array.hot_search)
    Column(
        modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 18.dp)
    ) {
        Text(text = "热搜", fontWeight = FontWeight.Bold, color = Color.Black)
        LazyVerticalGrid(
            modifier = Modifier
                .wrapContentHeight()
                .fillMaxWidth(),
            cells = GridCells.Fixed(2)
        ) {
            items(hotSearch.size) { index ->
                Box(
                    modifier = Modifier
                        .padding(horizontal = 4.dp, vertical = 12.dp)
                        .clickable { hotClick(hotSearch[index]) }) {
                    Text(
                        text = buildAnnotatedString {
                            withStyle(style = SpanStyle(color = black87)) { append("$index  ${hotSearch[index]} ") }
                            if (index % 5 == 0) {
                                withStyle(
                                    style = SpanStyle(
                                        color = Color.White,
                                        background = deepOrange_800
                                    )
                                ) { append("热") }
                            }
                            if (index % 3 == 0) {
                                withStyle(
                                    style = SpanStyle(
                                        color = Color.White,
                                        background = pink_400
                                    )
                                ) { append("新") }
                            }
                        },
                        fontSize = 16.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

/**
 * 搜索展示条目
 * */
@Composable
fun SearchItem(index: Int, entry: TransEntry) {
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable {
                Toast
                    .makeText(context, "第$index 条目", Toast.LENGTH_SHORT)
                    .show()
            }
    ) {
        Text(
            text = buildAnnotatedString {
                withStyle(style = SpanStyle(color = bili_20)) { append("$index. |") }
                withStyle(style = SpanStyle(color = bili_120)) { append(entry?.entry ?: "") }
                withStyle(
                    style = SpanStyle(
                        color = black87,
                        fontSize = 14.sp,
                        fontStyle = FontStyle.Italic,
                        shadow = Shadow(color = bili_20.copy(alpha = 0.4f), blurRadius = 5f)
                    )
                ) { append(entry?.explain ?: "") }
            },
            fontSize = 16.sp,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(
            modifier = Modifier
                .height(1.dp)
                .background(color = Color.Gray)
        )
    }
}


@Composable
fun SearchLoading() {
    Box(modifier = Modifier.wrapContentSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

@Composable
fun SearchError() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Image(
            painter = painterResource(id = R.drawable.ic_empty_page3),
            contentDescription = "失败"
        )
    }
}

/**
 * 搜索结果
 * */
@Composable
fun SearchResultScreen(viewModel: SearchViewModel, entries: List<TransEntry>) {
    LazyColumn {
        itemsIndexed(items = entries) { index, item ->
            SearchItem(index = index, entry = item)
        }
    }

}