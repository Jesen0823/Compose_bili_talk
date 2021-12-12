package com.jesen.bilibanner

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.jesen.bilibanner.bean.BannerData
import com.jesen.bilibanner.ui.theme.Compose_bili_talkTheme

class MainActivity : ComponentActivity() {

    private val items = arrayListOf(
        BannerData(
            id = 0, url = "https://www.nbaidu.com",
            imgUrl = "https://wanandroid.com/blogimgs/8a0131ac-05b7-4b6c-a8d0-f438678834ba.png"
        ),
        BannerData(
            id = 0, url = "https://www.nbaidu.com",
            imgUrl = "https://www.wanandroid.com/blogimgs/62c1bd68-b5f3-4a3c-a649-7ca8c7dfabe6.png"
        ),
        BannerData(
            id = 0, url = "https://www.nbaidu.com",
            imgUrl = "https://www.wanandroid.com/blogimgs/50c115c2-cf6c-4802-aa7b-a4334de444cd.png"
        ),
        BannerData(
            id = 0, url = "https://www.nbaidu.com",
            imgUrl = "https://www.wanandroid.com/blogimgs/90c6cc12-742e-4c9f-b318-b912f163b8d0.png"
        ),
    )

    @ExperimentalPagerApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Compose_bili_talkTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    TestBanner(items = items)
                }
            }
        }
    }
}

@ExperimentalPagerApi
@Composable
fun TestBanner(items: ArrayList<BannerData>) {
    BannerPager(
        modifier = Modifier.padding(top = 10.dp),
        items = items,
    ) { data ->
        Log.d("TestBanner", "click it，id:${data.id}, url:${data.url}")
    }
}