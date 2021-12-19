package com.jesen.compose_bili.utils.mapper

import com.jesen.retrofit_lib.com.BaseMapper
import com.jesen.retrofit_lib.model.TranslationM
import com.jesen.retrofit_lib.response.Response

/**
 * 对象转换，将banner转为简化的实体
 *
 * */
class EntityTransMapper : BaseMapper<TranslationM, Response<TranslationM>>() {
    override fun map(data: TranslationM): Response<TranslationM> {
        return if (data.result?.code == 200) {
            Response.success(data)
        } else {
            Response.failed(
                errorMessage = data.result?.msg ?: "error",
                code = data.result?.code ?: -1
            )
        }
    }
}