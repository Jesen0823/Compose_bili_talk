package com.jesen.compose_bili.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jesen.bilibanner.bean.BannerData
import com.jesen.common_util_lib.utils.oLog
import com.jesen.compose_bili.repository.ProfileRepository
import com.jesen.compose_bili.utils.mapper.EntityBannerMapper
import com.jesen.retrofit_lib.model.ProfileM
import com.jesen.retrofit_lib.response.DataState
import com.jesen.retrofit_lib.response.ModelMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ProfileViewModel : ViewModel() {

    private val _profileDataState = MutableStateFlow<DataState<ProfileM>>(DataState.Empty())

    val profileDataState: StateFlow<DataState<ProfileM>> = _profileDataState

    var bannerDataList = mutableStateListOf<BannerData>()

    fun loadProfileInfo() = viewModelScope.launch {
        oLog("profileViewModel--, loadProfileInfo")
        _profileDataState.value = DataState.Loading()
        flow {
            val result = ProfileRepository.getProfileData()

            oLog(" profile page result: ${result.msg}")
            emit(result)

        }.flowOn(Dispatchers.IO)
            .map { model ->
                ModelMapper<ProfileM>().map(model)
            }
            .collect { response ->
                if (response.isSuccess()) {
                    _profileDataState.value = DataState.Success(response.read())

                    // 提前把banner数据转换一下，因为封装时为了兼容指定了banner数据实体
                    if (bannerDataList.isEmpty()) {
                        response.read().data.bannerList?.let {
                            val bannerList = it.map { banner ->
                                EntityBannerMapper().map(banner)
                            }
                            bannerDataList.addAll(bannerList)
                        }
                    }
                } else {
                    _profileDataState.value =
                        DataState.Error(response.errorMessage(), response.code())
                }
            }
    }

}