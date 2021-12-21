package com.jesen.retrofit_lib.response

/**
 * 数据加载状态
 * */
sealed class DataState<out T> {
    data class Empty<T>(val code: Int = -1) : DataState<T>()
    data class Loading<T>(val code: Int = -1) : DataState<T>()

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