package com.jesen.compose_bili.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jesen.compose_bili.R
import com.jesen.compose_bili.navigation.context
import com.jesen.compose_bili.repository.SearchRepository
import com.jesen.compose_bili.utils.mapper.EntityTransMapper
import com.jesen.retrofit_lib.model.TranslationM
import com.jesen.retrofit_lib.response.DataState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class SearchViewModel : ViewModel() {

    var searchResultState = MutableStateFlow<DataState<TranslationM>>(DataState.Empty)

    val searchHotInfo = MutableStateFlow<MutableMap<String, String>>(mutableMapOf())


    fun translatingInput(key: String) =
        viewModelScope.launch {
            searchResultState.value = DataState.Loading

            flow {
                val result = SearchRepository.searchTranslation(keyword = key)
                Log.d("ViewModel--", "result: ${result.result?.msg}")
                emit(result)
            }.flowOn(Dispatchers.IO)
                .map { model ->
                    EntityTransMapper().map(model)
                }
                .collect { response ->
                    if (response.isSuccess()) {
                        searchResultState.value = DataState.Success(response.read())
                    } else {
                        searchResultState.value =
                            DataState.Error(response.errorMessage(), response.code())
                    }
                }
        }


    /**
     * 热词准备
     * 是为了配合搜索api,搜索长度太长API会返回异常
     */

    fun getHotWordsInfo() =
        viewModelScope.launch {
            val hotSearch = context.resources.getStringArray(R.array.hot_search)
            val hotKeys = context.resources.getStringArray(R.array.hot_keys)
            (1..hotKeys.size).asFlow().flowOn(Dispatchers.IO).map {
                (hotKeys[it - 1] to hotSearch[it - 1])
            }.collect {
                searchHotInfo.value[it.first] = it.second
            }
        }

}