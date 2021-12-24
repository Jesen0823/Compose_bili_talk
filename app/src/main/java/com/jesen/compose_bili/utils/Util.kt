package com.jesen.compose_bili.utils


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





