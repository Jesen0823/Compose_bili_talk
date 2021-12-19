package com.jesen.bilisplash_module.splash

import android.view.animation.OvershootInterpolator
import androidx.compose.animation.*
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.google.accompanist.insets.navigationBarsPadding
import com.jesen.bilisplash_module.R
import com.jesen.bilisplash_module.ui.theme.bili_20

enum class SplashType { SPLASH_ONE, SPLASH_TWO }

@ExperimentalAnimationApi
@OptIn(ExperimentalCoilApi::class)
@Composable
fun SplashScreen(
    splashViewModel: SplashViewModel,
    splashType: SplashType = SplashType.SPLASH_ONE,
    navNexEvent: (Boolean) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding()
            .background(MaterialTheme.colors.background),
    ) {
        splashViewModel.checkLoginState()

        // 大背景图片
        when (splashType) {
            SplashType.SPLASH_ONE -> SplashBgOne()
            SplashType.SPLASH_TWO -> SplashBgTwo(splashViewModel = splashViewModel)
        }

        Column(
            modifier = Modifier
                .animateContentSize()
                .wrapContentSize()
                .align(Alignment.BottomCenter),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .wrapContentHeight(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    modifier = Modifier
                        .width(50.dp)
                        .height(115.dp),
                    painter = rememberImagePainter(R.drawable.bili_man),
                    contentDescription = null,
                )
                Column(
                    modifier = Modifier.background(
                        brush = Brush.verticalGradient(
                            colorStops = arrayOf(
                                0.0f to Color.White,
                                0.2f to bili_20,
                                0.5f to Color.Transparent,
                                0.7f to Color.White,
                                0.9f to Color.Transparent
                            )
                        )
                    )
                ) {
                    Text(
                        modifier = Modifier.padding(start = 5.dp),
                        text = "Bili...",
                        fontSize = 35.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colors.onBackground
                    )
                    Text(
                        modifier = Modifier.padding(start = 5.dp, bottom = 6.dp),
                        text = "https://www.bilibili.com",
                        fontSize = 20.sp,
                        color = Color.Gray.copy(alpha = 0.7f),
                        fontStyle = FontStyle.Italic
                    )
                }
            }
            Spacer(modifier = Modifier.height(28.dp))

            AnimatedVisibility(
                visible = splashViewModel.checkingLoginState.value,
                modifier = Modifier
                    .padding(top = 20.dp)

            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    LinearProgressIndicator(
                        modifier = Modifier.width(150.dp)
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(text = "检查登录状态...")
                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
        }
    }
    LaunchedEffect(
        splashViewModel.loginState,
        splashViewModel.checkingLoginState
    ) {
        if (splashViewModel.loginState != -1 && !splashViewModel.checkingLoginState.value) {
            navNexEvent(splashViewModel.loginState == 1)
            // 前往主页
            /*if (splashViewModel.loginState == 0) {
                navController.navigate("main/推荐") {
                    popUpTo("splash") {
                        inclusive = true
                    }
                }
            } else {
                // 登录
                navController.navigate("login") {
                    popUpTo("splash") {
                        inclusive = true
                    }
                }
            }*/
        }
    }
}

@ExperimentalAnimationApi
@Composable
fun SplashBgTwo(splashViewModel: SplashViewModel) {
    AnimatedVisibility(
        visible = splashViewModel.checkingLoginState.value,
        enter = scaleIn(
            initialScale = 0.3f,
            animationSpec = tween(durationMillis = 700, easing = LinearOutSlowInEasing)
        ).plus(
            fadeIn(initialAlpha = 0.3f)
        ),
        exit = scaleOut(
            targetScale = 1.0f,
            animationSpec = tween(durationMillis = 300, easing = FastOutLinearInEasing)
        ).plus(
            fadeOut(targetAlpha = 1.0f)
        )
    ) {
        Image(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxSize(),
            //.align(Alignment.Center),
            painter = painterResource(id = R.drawable.splash_pic),
            contentScale = ContentScale.FillBounds,
            contentDescription = null
        )
    }
}

@ExperimentalAnimationApi
@Composable
fun SplashBgOne() {
    val alphaAnim = remember {
        Animatable(0f)
    }
    val cornerAnim = remember {
        Animatable(50f)
    }
    val scaleAnim = remember {
        Animatable(0.2f)
    }

    LaunchedEffect(key1 = true) {
        alphaAnim.animateTo(targetValue = 1.0f,
            animationSpec = tween(
                durationMillis = 500,
                easing = {
                    OvershootInterpolator(0.8f).getInterpolation(it)
                }
            )
        )
        cornerAnim.animateTo(targetValue = 1.0f,
            animationSpec = tween(
                durationMillis = 500,
                easing = {
                    OvershootInterpolator(0.8f).getInterpolation(it)
                }
            )
        )
        scaleAnim.animateTo(targetValue = 1.0f,
            animationSpec = tween(
                durationMillis = 300,
                easing = {
                    OvershootInterpolator(0.6f).getInterpolation(it)
                }
            )
        )
    }
    Image(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxSize()
            .scale(scaleAnim.value)
            .alpha(alpha = alphaAnim.value)
            .clip(shape = RoundedCornerShape(1.dp.times(cornerAnim.value.toInt()))),
        //.align(Alignment.Center),
        painter = painterResource(id = R.drawable.splash_pic),
        contentScale = ContentScale.FillBounds,
        contentDescription = null
    )
}
