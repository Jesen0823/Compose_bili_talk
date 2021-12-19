package com.jesen.compose_bili.utils.mapper

import com.jesen.bilibanner.bean.BannerData
import com.jesen.retrofit_lib.com.BaseMapper
import com.jesen.retrofit_lib.model.BannerM

/**
 * 对象转换，将banner转为简化的实体
 *
 * */
class EntityBannerMapper : BaseMapper<BannerM, BannerData>() {
    override fun map(data: BannerM): BannerData {
        return BannerData(
            id = data.id.toInt(),
            url = data.url,
            imgUrl = data.cover
        )
    }
}