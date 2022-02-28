package com.jesen.compose_bili.utils

import android.content.ClipData
import java.io.IOException

import java.io.InputStream

import android.content.ClipData.Item
import android.content.Context
import android.util.Log
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.jesen.compose_bili.datasource.NoticeListPagingDataSource
import com.jesen.compose_bili.repository.SearchRepository
import com.jesen.compose_bili.utils.mapper.EntityTransMapper
import com.jesen.retrofit_lib.com.MoshiUtil.listFromJson
import com.jesen.retrofit_lib.model.BannerM
import com.jesen.retrofit_lib.model.Notice
import com.jesen.retrofit_lib.response.DataState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.lang.Exception

import java.util.ArrayList
import java.io.InputStreamReader

import java.io.BufferedReader

import android.content.res.AssetManager

/**
 * 将参数匹配替换进Route路径
 * @param routeModel route定义路径
 * @param params 要传递的参数
 * @return 真正路径
 * */
fun replaceRegex(routeModel: String, params: String): String {
    // 替换{}中的内容
    val regex1 = """(?<=\{).*?(?=\})""".toRegex()
    // 替换{}以及它里面的内容
    val regex2 = """\{\w+\}*""".toRegex()
    return routeModel.replace(regex2, params)
}

fun readAssetFile(fileName: String, context: Context): String {
    //将json数据变成字符串
    val stringBuilder = StringBuilder()
    try {
        //获取assets资源管理器
        val assetManager = context.assets
        //通过管理器打开文件并读取
        val bf = BufferedReader(
            InputStreamReader(
                assetManager.open(fileName)
            )
        )
        var line: String?
        while (bf.readLine().also { line = it } != null) {
            stringBuilder.append(line)
        }
    } catch (e: IOException) {
        e.printStackTrace()
    }
    return stringBuilder.toString()
}







