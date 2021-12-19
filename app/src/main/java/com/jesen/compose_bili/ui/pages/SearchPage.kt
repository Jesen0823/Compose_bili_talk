package com.jesen.compose_bili.ui.pages

import androidx.activity.viewModels
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import com.google.accompanist.pager.ExperimentalPagerApi
import com.jesen.compose_bili.MainActivity
import com.jesen.compose_bili.navigation.doPageNavBack
import com.jesen.compose_bili.ui.widget.*
import com.jesen.compose_bili.viewmodel.SearchViewModel
import com.jesen.retrofit_lib.response.DataState
import soup.compose.material.motion.MaterialFadeThrough

/**
 * 搜索页面
 * */
@ExperimentalComposeUiApi
@ExperimentalFoundationApi
@ExperimentalMaterialApi
@ExperimentalAnimationApi
@ExperimentalPagerApi
@Composable
fun SearchPage(activity: MainActivity) {
    val viewModel by activity.viewModels<SearchViewModel>()

    // 是否需要指定输入内容，如点击热词项
    var needInput by remember { mutableStateOf("") }

    // 是否展示默认热词界面
    var showDefault = remember { mutableStateOf(true) }

    Scaffold(
        topBar = {
            SearchTopBar(
                needInputNow = needInput,
                onSearch = { keyword ->
                    viewModel.translatingInput(keyword)
                },
                onCancel = {
                    viewModel.searchResultState.value = DataState.Empty
                    doPageNavBack()
                },
                onClearInput = {
                    needInput = ""
                    viewModel.searchResultState.value = DataState.Empty
                }
            )
        }) {

        val searchDataState by viewModel.searchResultState.collectAsState()

        MaterialFadeThrough(
            targetState = searchDataState
        ) { searchResult ->
            when (searchResult) {
                is DataState.Empty -> {
                    showDefault.value = true
                }
                is DataState.Loading -> {
                    showDefault.value = false
                    // 数据加载中
                    SearchLoading()
                }
                is DataState.Success -> {
                    showDefault.value = false
                    // 展示搜索结果
                    searchResult.read().data?.entries?.let {
                        SearchResultScreen(viewModel, it)
                    }
                }
                is DataState.Error -> {
                    showDefault.value = false
                    SearchError()
                }
            }

        }
        if (showDefault.value) {
            SearchDefaultUIShow(hotClick = {
                // 点击热词自动更新输入框内容
                needInput = it
                viewModel.translatingInput(it)
            })
        }
    }
}
