package com.jesen.retrofit_lib.response

/**
 * 数据加载状态
 * */
sealed class DataState<out T> {
    object Empty : DataState<Nothing>()
    object Loading : DataState<Nothing>()

    data class Error(
        val message: String,
        val code: Int
    ) : DataState<Nothing>()

    data class Success<T>(
        val data: T
    ) : DataState<T>()

    fun read(): T = (this as Success<T>).data

    fun readSafely(): T? = if (this is Success<T>) read() else null
}