package com.jesen.biliwebview_module.debug.localprovidertest

import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.annotation.ExperimentalCoilApi
import com.jesen.common_util_lib.utils.CoilCircleImage


// 页面入口
@Composable
fun TestEnterScreen() {
    val advert =
        "https://img0.baidu.com/it/u=2586663849,2136659347&fm=26&fmt=auto"
    val name = "孙子兵法"
    TestComposable1(user = UserTest(name = name, photoUrl = advert))
}

/***
在树的某个地方，可以使用CompositionLocalProvider组件，它为CompositionLocal提供一个值。
这通常在树的“根”处，但也可以在任何地方，也可以在多个地方使用，以覆盖为子树提供的值。
 */
@Composable
fun TestComposable1(user: UserTest) {
    // 提供参数者
    CompositionLocalProvider(ActiveUser provides user) {
        TestComposable2()
    }

    val userNew = UserTest(
        name = "金瓶梅",
        photoUrl = "https://bkimg.cdn.bcebos.com/pic/0eb30f2442a7d9333a74267fad4bd11372f001da?x-bce-process=image/resize,m_lfit,w_268,limit_1/format,f_auto"
    )
    CompositionLocalProvider(ActiveUser provides userNew) {
        TestComposable2()
    }
    //TestComposable2()
}

/***
中间组件不需要知道CompositionLocal的值，并且对它的依赖关系可以为零。例如，SomeScreen可能是这样的:
公共属性current
https://developer.android.com/reference/kotlin/androidx/compose/runtime/CompositionLocal#current()
返回最近的compontionlocalprovider组件所提供的值，该组件直接或间接地调用使用此属性的可组合函数。
 */
@Composable
fun TestComposable2() {
    // 消费参数者
    val user = ActiveUser.current
    TestComposable3(src = user.photoUrl)
    //TestComposable2_1()
}

@Composable
fun TestComposable2_1() {
    // 消费参数者
    val user = ActiveUser.current
    TestComposable3(src = user.photoUrl)
}


// 最终消费
@ExperimentalCoilApi
@Composable
fun TestComposable3(src: String) {

    CoilCircleImage(url = src, modifier = Modifier.size(200.dp))
}

