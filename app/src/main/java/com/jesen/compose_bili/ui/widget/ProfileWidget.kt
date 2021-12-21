package com.jesen.compose_bili.ui.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.annotation.ExperimentalCoilApi
import com.jesen.common_util_lib.utils.CoilCircleImage
import com.jesen.common_util_lib.utils.CoilImage
import com.jesen.common_util_lib.utils.CoilImageBlur
import com.jesen.common_util_lib.utils.advancedShadow
import com.jesen.compose_bili.R
import com.jesen.compose_bili.ui.theme.bili_120
import com.jesen.compose_bili.ui.theme.black87
import com.jesen.retrofit_lib.model.Benefit
import com.jesen.retrofit_lib.model.Course
import com.jesen.retrofit_lib.model.DataProfile

/**
 * 个人中心小部件
 */


/**
 * 加载中...
 */
@Composable
fun ProfileLoading() {
    Box(modifier = Modifier.wrapContentSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

/**
 * 顶部图片
 * */
@Composable
fun HeaderTop() {
    CoilImageBlur(
        modifier = Modifier
            .blur(radius = 30.dp)
            .height(200.dp)
            .fillMaxWidth(),
        url = R.drawable.profile_bg,
        blurRadius = 12f,
        context = LocalContext.current,
        blurSampling = 4f,
        contentScale = ContentScale.FillWidth
    )
}

@Composable
fun UserNameUI(profileData: DataProfile) {
    Text(
        modifier = Modifier
            .wrapContentSize()
            .advancedShadow(
                color = Color.White,
                alpha = 0.3f,
                cornersRadius = 6.dp,
                shadowBlurRadius = 2.dp,
                offsetX = 1.dp,
                offsetY = 1.dp
            ),
        text = buildAnnotatedString {
            withStyle(
                style = SpanStyle(fontWeight = FontWeight.Bold)
            ) {
                append(profileData.name.replaceRange(4..9, "*****"))

            }
            withStyle(
                style = SpanStyle(
                    color = bili_120,
                    fontSize = 18.sp,
                    fontStyle = FontStyle.Italic,
                )
            ) {
                append("  VIP 12")
            }
        },
        fontSize = 24.sp,
        color = black87
    )
}

@ExperimentalCoilApi
@Composable
fun UserAdvertImg(advert: String) {
    val testUrl =
        "http://img.wmtp.net/wp-content/uploads/2020/09/99e56d8ab85447e79a92e1ae4d25a051!400x400.jpeg"
    CoilCircleImage(url = testUrl, modifier = Modifier.size(80.dp))
}


/**
 * 信息分类标题
 */
@Composable
fun ColumnStickHeader(title: String, subTitle: String) {
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, top = 8.dp, bottom = 8.dp),
        text = buildAnnotatedString {
            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                append(title)

            }
            withStyle(
                style = SpanStyle(
                    color = Color.Gray,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal
                )
            ) {
                append("  $subTitle")
            }

        },
        fontSize = 18.sp,
        color = Color.Black
    )
}

/**
 *  用户资产信息
 */
@Composable
fun ProfileToolBar(profileData: DataProfile) {
    Row(
        modifier = Modifier
            .wrapContentHeight()
            .padding(start = 30.dp)
            .background(
                brush = Brush.horizontalGradient(
                    colorStops = arrayOf(
                        0f to Color.Transparent,
                        0.9f to Color.White,
                    ),
                ),
                alpha = 0.6f
            ),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        VerticalNumText(num = "${profileData.favorite}", "收藏")
        VerticalNumText(num = "${profileData.like}", "点赞")
        VerticalNumText(num = "${profileData.browsing}", "浏览")
        VerticalNumText(num = "${profileData.coin}", "金币")
        VerticalNumText(num = "${profileData.fans}", "粉丝")
    }
}

@Composable
fun VerticalNumText(num: String, tex: String) {
    Column(
        modifier = Modifier
            .wrapContentSize()
            .width(80.dp)
            .background(color = Color.Transparent)
            .padding(vertical = 4.dp, horizontal = 6.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = num,
            fontSize = 12.sp,
            color = black87,
            modifier = Modifier.padding(bottom = 3.dp),
            fontWeight = FontWeight.Bold
        )
        Text(
            text = tex,
            fontSize = 14.sp,
            color = Color.Gray,
            modifier = Modifier.padding(bottom = 3.dp)
        )
    }
}

@Composable
fun CourseItemView(course: Course, onclick: (String) -> Unit) {
    Box(modifier = Modifier
        .fillMaxWidth()
        .aspectRatio(ratio = 27 / 10f)
        .clickable { onclick(course.url) }) {
        CoilImage(
            url = course.cover,
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp),
            topLeft = 12f,
            topRight = 12f,
            bottomLeft = 8f,
            bottomRight = 8f
        )
    }
}


@Composable
fun BenefitItemView(benefit: Benefit, onclick: (String) -> Unit) {
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .background(bili_120, shape = RoundedCornerShape(6.dp))
            .advancedShadow(
                color = Color.White.copy(alpha = 0.5f),
                alpha = 0.6f,
                cornersRadius = 8.dp,
                shadowBlurRadius = 6.dp,
                offsetX = 1.dp,
                offsetY = 1.dp
            )
            .padding(horizontal = 28.dp, vertical = 18.dp)
            .clickable { onclick(benefit.url) },
        text = benefit.name, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White
    )
}

@Composable
fun CourseListView(courseList: List<Course>) {
    // 需要展示几列
    val columnSize = 2
    // rows 总共几行
    var rowSize = courseList.size.div(columnSize)
    rowSize = if (courseList.size.mod(columnSize) == 0) rowSize else rowSize.plus(1)

    for (rowIndex in 0 until rowSize) {
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)) {
            for (columnIndex in 0 until columnSize) {
                //itemIndex List数据位置
                val itemIndex = rowIndex * columnSize + columnIndex
                if (itemIndex < courseList.size) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                start = if (itemIndex % 2 == 0) 0.dp else 3.dp,
                                end = if (itemIndex % 2 == 0) 3.dp else 0.dp,
                                bottom = 6.dp
                            )
                            .weight(1f, fill = true),
                        propagateMinConstraints = true
                    ) {
                        CourseItemView(course = courseList[itemIndex], onclick = {})
                    }
                } else {
                    Spacer(Modifier.fillMaxSize())
                }
            }
        }

    }
}