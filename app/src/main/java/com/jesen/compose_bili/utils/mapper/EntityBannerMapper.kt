package com.jesen.compose_bili.utils.mapper

import coil.map.Mapper
import com.jesen.bilibanner.bean.BannerData
import com.jesen.compose_bili.model.BannerM

/**
 * 对象转换，将banner转为简化的实体
 *
 * */
class EntityBannerMapper : Mapper<BannerM, BannerData> {
    override fun map(data: BannerM): BannerData {
        return BannerData(
            id = data.id.toInt(),
            url = data.url,
            imgUrl = data.cover
        )
    }
}