package com.jesen.biliwebview_module.debug.localprovidertest

import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jesen.common_util_lib.utils.CoilCircleImage

// 页面入口
@Composable
fun TestEnterScreen() {
    val advert = "https://img0.baidu.com/it/u=2586663849,2136659347&fm=26&fmt=auto"
    val name = "孙子兵法"
    val book = Book(name = name, photoUrl = advert)
    // 提供参数者
    ReadBook(book)
    CompositionLocalProvider(ActiveBook provides book) {
        // 爹告诉大儿子，书我上架了，自己想读就去拿
        TestComposable1()
    }
}

// 大儿子
@Composable
fun TestComposable1() {
    // 大儿子找来一本瓶梅自己看
    val newBook = Book(
        name = "金瓶梅",
        photoUrl = "https://bkimg.cdn.bcebos.com/pic/0eb30f2442a7d9333a74267fad4bd11372f001da?x-bce-process=image/resize,m_lfit,w_268,limit_1/format,f_auto"
    )
    ReadBook(newBook)
    CompositionLocalProvider(ActiveBook provides newBook) {
        //TestComposable2()
    }
    // 读完上架了，没有告诉小儿子
    TestComposable2()
}

// 二儿子
@Composable
fun TestComposable2() {
    val book = ActiveBook.current
    ReadBook(book)
    TestComposable3()
}

// 最小
@Composable
fun TestComposable3() {
    val book = ActiveBook.current
    ReadBook(book)
}

@Composable
fun ReadBook(book: Book) {
    CoilCircleImage(url = book.photoUrl, modifier = Modifier.size(120.dp))
}

/***
在Composable树的某个地方，使用CompositionLocalProvider可以在树的“根”处为CompositionLocal提供一个值。
，但也可以在任何地方，也可以在多个地方使用，以覆盖为父为子树提供的值。
 */


/***
中间组件不需要知道CompositionLocal的值，并且对它的依赖关系可以为零。例如，SomeScreen可能是这样的:
公共属性current
https://developer.android.com/reference/kotlin/androidx/compose/runtime/CompositionLocal#current()
返回最近的compontionlocalprovider组件所提供的值，该组件直接或间接地调用使用此属性的可组合函数。
 */
