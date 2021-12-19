package com.jesen.compose_bili.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jesen.compose_bili.repository.SearchRepository
import com.jesen.compose_bili.utils.mapper.EntityTransMapper
import com.jesen.retrofit_lib.model.TranslationM
import com.jesen.retrofit_lib.response.DataState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class SearchViewModel : ViewModel() {

    var searchResultState = MutableStateFlow<DataState<TranslationM>>(DataState.Empty)

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

}