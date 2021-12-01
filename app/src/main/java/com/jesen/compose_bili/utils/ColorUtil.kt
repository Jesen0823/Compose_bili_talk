package com.jesen.compose_bili.utils

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.ui.graphics.Color
import kotlin.math.abs
import kotlin.random.Random

/**
 * 颜色各个分量一般是以每8位为一个单位。
 * 8位，8bit，即一个字节，10进制数的取值范围是0~255，一般用16进制表示，0x开头，取值范围是0x00到0xFF（不区分大小写）。
 *
 * 颜色一般有24位和32位两种表示方法。
 * 24位颜色：每8位表示RGB中的一个分量。RGB颜色分量的值越大的时候最终颜色越偏向于这个颜色。
 * 32位颜色：除了每8位表示RGB中的一个分量，还有一个8位用于表示透明度，表示为ARGB形式。
 *
 * */

object ColorUtil {

    fun rgbFromColor(color: Int) {
        //val color = 0x342388

        //右移16位，把2388移出，取0x34
        val r = color shr (16)   // java: color >> 16
        //右移8位，把88移出，得0x3423，与0xff按位与操作，得0x23
        val g = color shr (8) and 0xff  // java:color >> 8 & 0xff
        //得到0x88
        val b = color and 0xff  // java color & 0xff
        oLog(" colorUtil, rgbFromColor: $r,$g,$b")
    }


    fun argbFromColor(color: Int) {
        //val color = 0xff342388

        //注意这里是>>>，无符号右移位操作,右移24位,把342388移出，得到0xff
        val a = color ushr (24)   //java: color >>> 24
        //右移16位，把2388移出，取0x34
        val r = color shr 16 and 0xff  // java: color >> 16
        //右移8位，把88移出，得0x3423，与0xff按位与操作，得0x23
        val g = color shr 8 and 0xff  // java:color >> 8 & 0xff
        //得到0x88
        val b = color and 0xff  // java color & 0xff
        // 打印得到10进制
        oLog(" colorUtil, argbFromColor: $a,$r,$g,$b")
    }

    fun colorTo(from: Int, to: Int, factor: Float): Int {
        Log.d("xxxx", "colorTo 1: $factor")
        val factor = 1f.coerceAtMost(factor.coerceAtLeast(0f))
        Log.d("xxxx", "colorTo 2: $factor")
        val a1 = (from and (0xFF shl 24)) shr 24
        val r1 = (from and (0xFF shl 16)) shr 16
        val g1 = (from and (0xFF shl 8)) shr 8
        val b1 = (from and 0xFF)
        val a2 = (to and (0xFF shl 24)) shr 24
        val r2 = (to and (0xFF shl 16)) shr 16
        val g2 = (to and (0xFF shl 8)) shr 8
        val b2 = (to and 0xFF)
        val a = (if (a1 > a2) a1 - (a1 - a2) * factor else (a2 - a1) * factor + a1).toInt()
        val r = (if (r1 > r2) r1 - (r1 - r2) * factor else (r2 - r1) * factor + r1).toInt()
        val g = (if (g1 > g2) g1 - (g1 - g2) * factor else (g2 - g1) * factor + g1).toInt()
        val b = (if (b1 > b2) b1 - (b1 - b2) * factor else (b2 - b1) * factor + b1).toInt()

        return (a shl 24) + (r shl 16) + (g shl 8) + b
    }


    /**
     * 随机色与目标色混合
     * */
    @RequiresApi(Build.VERSION_CODES.O)
    fun getRandomColor(mix: Color): Color {
        var red: Int = Random.nextInt(256)
        var green: Int = Random.nextInt(256)
        var blue: Int = Random.nextInt(256)

        // mix the color
        red = (red + mix.red.toInt()) / 2
        green = (green + mix.green.toInt()) / 2
        blue = (blue + mix.blue.toInt()) / 2

        return Color(red, green, blue)
    }


    /**
     * 生成灰度相似的颜色
     * */
    @RequiresApi(Build.VERSION_CODES.O)
    fun getRandomColorB(mix: Color): Color {
        val red: Int = Random.nextInt(256)
        val green: Int = Random.nextInt(256)
        val blue: Int = mix.blue.toInt()

        var b = (blue - (red * 0.3 + green * 0.59)) / 0.11
        if (b < 0) b = abs(b)
        if (b > 255) b = 255.toDouble()
        return Color(red, green, b.toInt())
    }
}

/*@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ColorTest() {

    Column(modifier = Modifier.wrapContentSize()) {
        var colorState by remember { mutableStateOf(bili_20) }
        var backColorState  by remember { mutableStateOf(bili_20) }
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = colorState
                ),
            content = { Text("Click it") },
            colors = ButtonDefaults.buttonColors(backgroundColor = backColorState),
            onClick = {
                oLog("click pre: $colorState")
                colorState = ColorUtil.getRandomColorB(bili_90)
                backColorState = ColorUtil.getRandomColor(bili_90)
                oLog("click after: $colorState")
            }
        )
    }
}*/

