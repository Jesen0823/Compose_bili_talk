package com.jesen.videodetail_model.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jesen.common_util_lib.utils.oLog
import com.jesen.retrofit_lib.com.fromJson
import com.jesen.retrofit_lib.model.VideoDetailM
import com.jesen.retrofit_lib.model.VideoM
import com.jesen.retrofit_lib.response.DataState
import com.jesen.retrofit_lib.response.ModelMapper
import com.jesen.videodetail_model.repository.VideoDetailRepository
import com.jesen.videodetail_model.util.videoDataDemoJson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class DetailViewModel : ViewModel() {

    val videoDetailState = MutableStateFlow<DataState<VideoDetailM>>(DataState.Empty)

    var testVideoM: VideoM = videoDataDemoJson.fromJson<VideoM>()!!

    /*fun loadVideoInfo(vid: String) {
        videoDetailState.value = DataState.Loading

        viewModelScope.launch {
            val response = VideoDetailRepository.getVideDetailData(vid).also {
                ModelMapper<VideoDetailM>().map(it)
            }
            if (response.isSuccess()) {
                videoDetailState.value = DataState.Success(response.read())

                // insert history
            } else {
                videoDetailState.value = DataState.Error(response.errorMessage(), response.code())
            }
        }
    }*/

    fun loadVideoInfo2(vid: String) = viewModelScope.launch {
        videoDetailState.value = DataState.Loading
        flow {
            val result = VideoDetailRepository.getVideDetailData(vid)

            oLog(" login result: ${result.msg}")
            emit(result)

        }.flowOn(Dispatchers.IO)
            .map { model ->
                ModelMapper<VideoDetailM>().map(model)
            }

            .collect { response ->
                if (response.isSuccess()) {
                    videoDetailState.value = DataState.Success(response.read())

                    // insert history
                } else {
                    videoDetailState.value =
                        DataState.Error(response.errorMessage(), response.code())
                }
            }
    }


}