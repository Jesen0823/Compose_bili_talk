package com.jesen.compose_bili.viewmodel

import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.jesen.bilibanner.bean.BannerData
import com.jesen.compose_bili.datasource.HomeContentPagingDataSource
import com.jesen.compose_bili.datasource.NoticeListPagingDataSource
import com.jesen.compose_bili.repository.SearchRepository
import com.jesen.compose_bili.utils.mapper.EntityBannerMapper
import com.jesen.compose_bili.utils.mapper.EntityTransMapper
import com.jesen.compose_bili.utils.readAssetFile
import com.jesen.retrofit_lib.com.MoshiUtil
import com.jesen.retrofit_lib.model.*
import com.jesen.retrofit_lib.response.DataState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.io.IOException
import java.io.InputStream
import java.lang.Exception
import java.lang.reflect.Type
import java.sql.Types

class HomeViewModel : ViewModel() {

    var categoryList = mutableStateListOf(CategoryM(count = 1, name = "推荐"))

    var selectedIndex = 0

    var bannerDataList = mutableStateListOf<BannerData>(BannerData())

    var allCategoryVideoMap: MutableMap<CategoryM, Pair<CategoryM, Flow<PagingData<VideoM>>>>? =
        mutableMapOf()

    fun getVideoPagingDataList(category: CategoryM): Pair<CategoryM, Flow<PagingData<VideoM>>> =
        Pair(
            category, Pager(
                config = PagingConfig(
                    pageSize = 5,
                    initialLoadSize = 10, // 第一次加载数量
                    prefetchDistance = 1,
                )
            ) {

                HomeContentPagingDataSource(category, this, selectedIndex, EntityBannerMapper())
            }.flow.cachedIn(viewModelScope)
        ).also {
            allCategoryVideoMap?.put(category, it)
        }

    fun getDefaultBannerList(context: Context) =
        viewModelScope.launch {
            flow {
                var inputStream: InputStream? = null
                try {
                    inputStream = context.assets.open("json/defaultBanner.json")
                    emit(MoshiUtil.listFromJson<BannerM>(inputStream))
                } catch (e: IOException) {
                    e.printStackTrace()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                emit(null)
            }.flowOn(Dispatchers.IO)
                .map { bannerMList ->
                    val newList = mutableListOf<BannerData>()
                    bannerMList?.onEach { bannerM ->
                        newList.add(EntityBannerMapper().map(bannerM))
                    }
                    newList
                }
                .collect { dataList ->
                    if (dataList.isNotEmpty()) {
                        bannerDataList.clear()
                        bannerDataList.addAll(dataList)
                    }
                }
        }

    fun getDefaultBannerList2(context: Context) =
        viewModelScope.launch {
            flow {
                var readString: String = ""
                    readString = readAssetFile("json/defaultBanner.json",context)
                    emit(MoshiUtil.listFromJson<BannerM>(readString))
            }.flowOn(Dispatchers.IO)
                .map { bannerMList ->
                    val newList = mutableListOf<BannerData>()
                    bannerMList?.onEach { bannerM ->
                        newList.add(EntityBannerMapper().map(bannerM))
                    }
                    newList
                }
                .collect { dataList ->
                    if (dataList.isNotEmpty()) {
                        bannerDataList.clear()
                        bannerDataList.addAll(dataList)
                    }
                }
        }

    fun getNoticePagingDataList(): Flow<PagingData<Notice>> =
        Pager(
            config = PagingConfig(
                pageSize = 5,
                initialLoadSize = 10, // 第一次加载数量
                prefetchDistance = 1,
            )
        ) {

            NoticeListPagingDataSource()
        }.flow.cachedIn(viewModelScope)

}