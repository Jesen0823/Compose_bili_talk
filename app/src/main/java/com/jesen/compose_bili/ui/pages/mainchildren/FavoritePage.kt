package com.jesen.compose_bili.ui.pages.mainchildren

import android.widget.Toast
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.compose.collectAsLazyPagingItems
import coil.annotation.ExperimentalCoilApi
import com.google.accompanist.insets.ui.Scaffold
import com.jesen.compose_bili.MainActivity
import com.jesen.compose_bili.base.SwipeRefreshColumnLayout
import com.jesen.compose_bili.ui.theme.gray600
import com.jesen.compose_bili.ui.widget.SmallVideoCard
import com.jesen.compose_bili.viewmodel.FavoriteListViewModel

/**
 * 收藏列表页面
 * */
@ExperimentalCoilApi
@ExperimentalMaterialApi
@Composable
fun FavoritePage(activity: MainActivity) {
    val viewModel by activity.viewModels<FavoriteListViewModel>()

    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier.fillMaxWidth(),
                title = {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(vertical = 10.dp, horizontal = 23.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = "收藏",
                            fontSize = 18.sp,
                            color = MaterialTheme.colors.onSecondary,
                            textAlign = TextAlign.Center
                        )
                    }
                },
                backgroundColor = Color.White,
                contentColor = gray600,
                elevation = 10.dp
            )
        }
    ) {

        val lazyPagingItems = viewModel.favoriteListData.collectAsLazyPagingItems()

        Column(modifier = Modifier
            .fillMaxSize()
            .padding(top = 56.dp)) {
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
                        SmallVideoCard(
                            video = it,
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