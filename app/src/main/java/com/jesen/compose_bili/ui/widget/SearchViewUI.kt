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
 * ??????????????????????????????
 * [ALL]
 * */


/**
 * ???????????????
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
        // ?????????????????????
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
                        text = "????????????",
                        style = TextStyle(fontSize = 14.sp)
                    )
                },
                colors = TextFieldDefaults.textFieldColors(
                    cursorColor = bili_50,
                    // ?????????????????????????????????????????????
                    focusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    backgroundColor = gray200
                ),
                // ??????????????????????????????
                keyboardActions = KeyboardActions(onSearch = {
                    onSearch(inputValue)
                    keyboardController?.hide()
                }),
                textStyle = TextStyle(color = gray400),
                // ??????????????????????????????Search, Done,??????
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Search
                )
            )
            // ????????????????????????????????????????????????????????????
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
            Text(text = "??????", fontSize = 18.sp, color = Color.Gray)
        }

    }
}


/**
 * ????????????????????????
 * ????????????
 * */
@ExperimentalFoundationApi
@Composable
fun SearchDefaultUIShow(hotClick: (String, String) -> Unit, hotInfo: Map<String, String>) {
    val context = LocalContext.current
    val hotKeys = context.resources.getStringArray(R.array.hot_keys)

    Column(
        modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 18.dp)
    ) {
        Text(text = "??????", fontWeight = FontWeight.Bold, color = Color.Black)
        LazyVerticalGrid(
            modifier = Modifier
                .wrapContentHeight()
                .fillMaxWidth(),
            cells = GridCells.Fixed(2)
        ) {

            items(hotInfo.size) { index ->

                Box(
                    modifier = Modifier
                        .padding(horizontal = 4.dp, vertical = 12.dp)
                        .clickable { hotClick(hotKeys[index], hotInfo[hotKeys[index]] ?: "") }) {
                    Text(
                        text = buildAnnotatedString {
                            withStyle(style = SpanStyle(color = black87)) {
                                append(
                                    "$index  ${
                                        hotInfo.getValue(
                                            hotKeys[index]
                                        )
                                    } "
                                )
                            }
                            if (index % 5 == 0) {
                                withStyle(
                                    style = SpanStyle(
                                        color = Color.White,
                                        background = deepOrange_800
                                    )
                                ) { append("???") }
                            }
                            if (index % 3 == 0) {
                                withStyle(
                                    style = SpanStyle(
                                        color = Color.White,
                                        background = pink_400
                                    )
                                ) { append("???") }
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
 * ??????????????????
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
                    .makeText(context, "???$index ??????", Toast.LENGTH_SHORT)
                    .show()
            }
    ) {
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .padding(horizontal = 16.dp)
                .background(color = gray200)
        )
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
    }
}


@Composable
fun SearchLoading() {
    Box(modifier = Modifier.wrapContentSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

@Composable
fun SinglePageError() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Image(
            painter = painterResource(id = R.drawable.ic_empty_page3),
            contentDescription = "??????"
        )
    }
}

/**
 * ????????????
 * */
@Composable
fun SearchResultScreen(viewModel: SearchViewModel, entries: List<TransEntry>) {
    LazyColumn {
        itemsIndexed(items = entries) { index, item ->
            SearchItem(index = index, entry = item)
        }
    }

}