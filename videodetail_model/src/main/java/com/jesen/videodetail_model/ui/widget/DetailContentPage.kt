package com.jesen.videodetail_model.ui.widget

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Upload
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.jesen.retrofit_lib.model.VideoDetailData
import com.jesen.retrofit_lib.model.VideoM
import com.jesen.videodetail_model.ui.theme.bili_50
import com.jesen.videodetail_model.util.SmallVideoCard
import com.jesen.videodetail_model.viewmodel.DetailViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * 视频简介Tab对应列表
 * */
@ExperimentalPagerApi
@ExperimentalMaterialApi
@ExperimentalComposeUiApi
@Composable
fun VideoDesContent(
    viewModel: DetailViewModel,
    detailData: VideoDetailData,
    itemCardClick: (VideoM) -> Unit,
    coroutineScope: CoroutineScope,
) {

    val lazyListState = rememberLazyListState()

    LazyColumn(state = lazyListState) {
        item {
            VideoInfoItem(viewModel = viewModel, detailData = detailData)
        }
        itemsIndexed(detailData.videoList) { index, itemData ->
            SmallVideoCard(video = itemData,
                onClick = { itemCardClick(itemData) }
            )
        }
        item {
            IconButton(
                modifier = Modifier.padding(horizontal = 30.dp),
                onClick = {
                    coroutineScope.launch {
                        lazyListState.animateScrollToItem(index = 0)
                    }
                },

                ) {
                Icon(
                    imageVector = Icons.Rounded.Upload,
                    contentDescription = "back top",
                    tint = bili_50
                )
            }
        }
    }
}