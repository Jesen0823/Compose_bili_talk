package com.jesen.compose_bili.viewmodel

import android.app.Application
import androidx.compose.runtime.mutableStateListOf
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.jesen.bilibanner.bean.BannerData
import com.jesen.common_util_lib.utils.oLog
import com.jesen.compose_bili.R
import com.jesen.compose_bili.navigation.context
import com.jesen.compose_bili.repository.ProfileRepository
import com.jesen.compose_bili.utils.mapper.EntityBannerMapper
import com.jesen.retrofit_lib.model.ProfileM
import com.jesen.retrofit_lib.response.DataState
import com.jesen.retrofit_lib.response.ModelMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ProfileViewModel(application: Application) : AndroidViewModel(application) {

    private val _profileDataState = MutableStateFlow<DataState<ProfileM>>(DataState.Empty())

    val profileDataState: StateFlow<DataState<ProfileM>> = _profileDataState

    var bannerDataList = mutableStateListOf<BannerData>()

    private fun getDrawable(resId: Int) =
        ResourcesCompat.getDrawable(context.resources, resId, null)


    private val defaultBannerSource = mapOf(
        getDrawable(R.drawable.banner1) to "https://docs.compose.net.cn/",
        getDrawable(R.drawable.banner2) to "https://docs.compose.net.cn/design/gesture/nested_scroll/",
        getDrawable(R.drawable.banner3) to "https://blog.csdn.net/weixin_40611659/article/details/119645712",
        getDrawable(R.drawable.banner4) to "https://howtodoandroid.com/getting-started-with-workmanager/",
        getDrawable(R.drawable.banner5) to "https://howtodoandroid.com/category/jetpack/"
    ).map {
        it
    }


    val loadProfileInfo = viewModelScope.launch {
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

                                // 这里api给的广告太恶心了转为本地图片
                                val index = it.indexOf(banner)
                                val bannerData = EntityBannerMapper().map(banner)
                                bannerData.url = defaultBannerSource[index].value
                                bannerData.imgUrl = defaultBannerSource[index].key!!
                                bannerData
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

    // 退出登录账号
    fun logoutAccount() {
        viewModelScope.launch {
            ProfileRepository.logoutAccount()
        }
    }
}