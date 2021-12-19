package com.jesen.compose_bili.ui.pages

import android.widget.Toast
import androidx.activity.viewModels
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.paging.compose.collectAsLazyPagingItems
import com.google.accompanist.pager.ExperimentalPagerApi
import com.jesen.common_util_lib.paging.SwipeRefreshColumnLayout
import com.jesen.compose_bili.MainActivity
import com.jesen.compose_bili.navigation.doPageNavBack
import com.jesen.compose_bili.ui.widget.NoticeItemView
import com.jesen.compose_bili.viewmodel.HomeViewModel

@ExperimentalFoundationApi
@ExperimentalMaterialApi
@ExperimentalAnimationApi
@ExperimentalPagerApi
@Composable
fun NoticeListPage(activity: MainActivity) {
    val viewModel by activity.viewModels<HomeViewModel>()
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "通知",
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                navigationIcon = {
                    IconButton(modifier = Modifier.size(48.dp), onClick = {
                        doPageNavBack()
                    }) {
                        Icon(imageVector = Icons.Rounded.ArrowBack, contentDescription = "返回")
                    }
                },
                backgroundColor = MaterialTheme.colors.surface,
                elevation = 12.dp
            )
        }) {
        val lazyPagingItems = viewModel.getNoticePagingDataList().collectAsLazyPagingItems()

        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            val columnState = rememberLazyListState()

            SwipeRefreshColumnLayout(
                columnState = columnState,
                collectAsLazyPagingItems = lazyPagingItems,
            ) {

                items(count = lazyPagingItems.itemCount,
                    key = { index ->
                        index
                    }
                ) { index ->
                    lazyPagingItems[index]?.let {
                        NoticeItemView(
                            notice = it,
                            onClick = {
                                Toast.makeText(activity, "ccc", Toast.LENGTH_SHORT).show()
                            },
                        )
                    }
                }
            }
        }
    }
}