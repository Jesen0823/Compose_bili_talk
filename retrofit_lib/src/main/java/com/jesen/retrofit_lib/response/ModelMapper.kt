package com.jesen.retrofit_lib.response

import com.jesen.retrofit_lib.com.BaseMapper
import com.jesen.retrofit_lib.com.BaseModel

/**
 * 将实际的实体类转换为Response,方便状态判断
 * @property T 实体类型
 * */
class ModelMapper<T : BaseModel> : BaseMapper<T, Response<T>>() {

    override fun map(data: T): Response<T> {
        return if (data.code == 0) {
            Response.success(data)
        } else {
            Response.failed(errorMessage = data.msg, code = data.code)
        }
    }

}