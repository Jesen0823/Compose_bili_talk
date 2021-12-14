package com.jesen.retrofit_lib.response

/**
 * Response封装，方便判断UI展示
 * */
sealed class Response<T>(
    private val data: T? = null,
    private val msg: String? = null,
    private val code: Int = -1
) {
    companion object {
        fun <T> success(data: T) = Success(data)
        fun <T> failed(errorMessage: String = "null", code: Int = -1) =
            Failed<T>(errorMessage, code)
    }

    fun isSuccess() = this is Success
    fun isFailed() = this is Failed

    fun read() = data!!
    fun errorMessage() = msg!!
    fun code() = code

    fun toDataState() =
        if (isSuccess()) DataState.Success(read()) else DataState.Error(errorMessage(), code)

    class Success<T> internal constructor(data: T) : Response<T>(data = data)
    class Failed<T> internal constructor(errorMessage: String, code: Int) :
        Response<T>(msg = errorMessage, code = code)
}