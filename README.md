# Compose_bili_talk
使用Jetpack Compose 实现低仿哔哩哔哩视频播放

# Jetpack Compose | 低仿哔哩哔哩（一）|尝试实现登录注册页面



效果图：

<img src="C:\Users\X1 Carbon\Desktop\capture\ezgif-6-f53b17f37b15.gif" style="zoom:80%;" /

> 非常简单的一个页面UI，就是一些控件 + Navigation + 状态保持，之前看过一些Flutter实现的视频，所以最近就想到用Compose尝试一下，手感跟Flutter感觉差别不大，实际上是有差别的，毕竟对Android更熟悉。
>
> Compose中看别人的文章都是一个Activity，其他的都是Compose控件，也就是 Activity + N * Screen  ,这个Screen就类似Fragment，只不过更加灵活。

| <img src="C:\Users\X1 Carbon\Desktop\capture\ezgif-6-f53b17f37b15.gif" style="zoom: 67%;" /> | <img src="C:\Users\X1 Carbon\Desktop\capture\导航首页.jpg" style="zoom:80%;" /> |
| ------------------------------------------------------------ | ------------------------------------------------------------ |

#### 一、登录注册页面：

大概用到以下控件：

##### 1. 顶部 TopAppBar ，脚手架的一部分

```kotlin
/**
 * 顶部TopBar
 * Scafflod的 appBar 并不仅仅限于TopAppBar控件，号可以是其他任意或自定义的@Compose组件
 * 源码： topBar: @Composable () -> Unit = {},
 * */
@Composable
fun TopBarView(
    iconEvent: @Composable (() -> Unit)? = null,
    titleText: String,
    actionEvent: @Composable RowScope.() -> Unit = {},
) {
    TopAppBar(
        title = {
            Text(
                text = titleText,
                color = Color.Black
            )
        },
        navigationIcon = iconEvent,
        actions = actionEvent,
        // below line is use to give background color
        backgroundColor = Color.White,
        contentColor = Color.Black,
        elevation = 12.dp
    )
}

// 源码：
@Composable
fun TopAppBar(
    title: @Composable () -> Unit, // 标题，不限于文字，可以自定义
    modifier: Modifier = Modifier, // 修饰符，warpContent,matchParent,size,背景，pading等等
    navigationIcon: @Composable (() -> Unit)? = null, // 导航按键，可以是任意按钮，IconButton,TextButton。。。
    actions: @Composable RowScope.() -> Unit = {}, // 右导航，可以是任意按键
    backgroundColor: Color = MaterialTheme.colors.primarySurface,
    contentColor: Color = contentColorFor(backgroundColor), // 内容颜色
    elevation: Dp = AppBarDefaults.TopAppBarElevation  // 投影高度
)
```



密码的明文密文模式切换，根据密码输入框获取焦点监听是否获取了焦点，并且【眼睛】按键被用户选中了，则输入框为密文样式：

```kotlin
// 源码
@Composable
fun TextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    textStyle: TextStyle = LocalTextStyle.current,
    label: @Composable (() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
    // 输入框获取焦点后对这个属性进行设置
    /**
 	* Interface used for changing visual output of the input field.
    *
    * This interface can be used for changing visual output of the text in the input field.
    * For example, you can mask characters in password filed with asterisk with
    * [PasswordVisualTransformation].
    */
    visualTransformation: VisualTransformation = VisualTransformation.None, // 默认是明文，也就是用户名那种
    /// ...
    )
```

Modifier有一个监听获焦的回调，一旦这里发现获得焦点，即可将输入框的visualTransformation  设置为 PasswordVisualTransformation()：

```kotlin
       modifier = Modifier
            .fillMaxWidth()
            .onFocusChanged {
                // 只有输入框获取焦点了，且输入框类型为密码类型，才算真正获焦，这时候捂脸动画需要顶部捂眼睛动画执行
                viewModel.onFocusHide(it.isFocused && (type == "password" || type == "rePassword"))
            },

      // 密码输入类型设置
    val visualTransformation =
        // 用户点击遮掩密码，并且输入框是密码类型，则给输入框设置密文样式，否则就是明文
        if (!viewModel.showPwd && (type == "password" || type == "rePassword")) PasswordVisualTransformation() else VisualTransformation.None
```

其中，showPwd, onFocusHide 是mutableStateOf(Boolean) 类型自己定义的变量，维护UI的状态。



##### 2. 页简单的路由，Navigation：

```kotlin
// 导航
implementation "androidx.navigation:navigation-compose:2.4.0-alpha09"
```

```kotlin

/**
 * 定义路由
 * */
object PageRoute {
    const val LOGIN_ROUTE = "login_route"
    const val REGISTER_ROUTE = "register_route"
    const val MAIN_ROUTE = "main_route"
}

/**
 * 将页面与路由关联
 * 首先要初始化一个PageNavController实例：
 * pageNavController = rememberNavController()

 * */
@ExperimentalPagerApi
@Composable
fun PageNavHost(mainActivity: MainActivity) {
    val navHostController = MainActivity.pageNavController!!
    val isLogined = false // 是否登录

    // 初始路由destination
    val initRoute = if (isLogined) PageRoute.MAIN_PAGE else PageRoute.LOGIN_ROUTE
    NavHost(navController = navHostController, startDestination = initRoute) {
        // 定义路由，注册页面，登录页面
        composable(route = PageRoute.LOGIN_ROUTE) {
            LoginPage(activity = mainActivity)
        }
        composable(route = PageRoute.REGISTER_ROUTE) {
            RegisterPage(activity = mainActivity)
        }
    }
}

/**
 * 页面跳转
 * */
fun doPageNavigationTo(route: String) {
    val navController = MainActivity.pageNavController!!
    navController.navigate(route) {
        launchSingleTop = false
        popUpTo(navController.graph.findStartDestination().id) {
            // 防止状态丢失
            saveState = true
        }
        // 恢复composeble的状态
        restoreState = true
    }
}

/**
 * 页面回退
 * */
fun doPageNavBack(route: String?) {
    val navController = MainActivity.pageNavController!!
    route?.let {
        navController.popBackStack(route = it, inclusive = false)
    } ?: navController.popBackStack()
}
```



#### 二、首页结构：

![](C:\Users\X1 Carbon\Desktop\capture\141444.jpg)

* 底部导航定义在主页(**mainScreen**)底部，分别对应4个Compose Screen页面，默认选中第一个Screen(**homeScreen**)。

* 首页homeScreen 顶部需要有搜索框， 栏目滑动列表，默认选中第一个栏目。




###### 1. 底部导航栏：

1.   mainScreen 主页代码，可以看到脚手架Scaffold除了提供顶部appBar，还提供了底部导航buttomBar，这里给底部导航提供了BottomNavigationScreen，带参是：导航控制器 + 底部元素dataList.

   代码：

   ```kotlin
   /**
    * 首屏 主页面
    * */
   fun MainPage() {
       // 底部导航对应页面
       val list = listOf(
           Screens.Home,
           Screens.Ranking,
           Screens.Favorite,
           Screens.Profile,
       )
       val navController = rememberNavController()

       Scaffold(bottomBar = {
           // 底部导航，实际上Compose 对bottomBar要求很灵活，跟appBar一样，可以传入任何@Compose
           BottomNavigationScreen(navController = navController, items = list)
       }) {
           // 底部导航路由，定义了导航联动，第4点
           BottomNavHost(navHostController = navController)
       }
   }
   ```

2. 底部导航栏

   ```kotlin
   /**
    * 底部导航栏
    * */
   @Composable
   fun BottomNavigationScreen(navController: NavController, items: List<Screens>) {
       val navBackStackEntry by navController.currentBackStackEntryAsState()
       val destination = navBackStackEntry?.destination

       BottomNavigation(backgroundColor = Color.White,elevation = 12.dp) {
           items.forEach { screen ->
               // 底部的每一个选项
               BottomNavigationItem(
                   selected = destination?.route == screen.route,
                   // 点击响应跳转
                   onClick = {
                       navController.navigate(screen.route) {
                           launchSingleTop = true
                           popUpTo(navController.graph.findStartDestination().id) {
                               // 防止状态丢失
                               saveState = true
                           }
                           // 恢复Composable的状态
                           restoreState = true
                       }
                   },
                   icon = {
                       Icon(
                           painter = painterResource(id = screen.icons),
                           contentDescription = null
                       )
                   },
                   label = { Text(screen.title) },
                   alwaysShowLabel = true,
                   unselectedContentColor = gray400,
                   selectedContentColor = bili_90,
               )

           }
       }
   }
   ```



3. 定义底部导航信息，定义了文字，图标和路由地址：

   ```kotlin
   /**
    * 首页底部页面路由定义
    * */
   sealed class Screens(val title: String, val route: String, @DrawableRes val icons: Int) {

       object Home : Screens(title = "首页", route = "home_route", icons = R.drawable.round_home_24)

       object Ranking :
           Screens(title = "排行", route = "ranking_route", icons = R.drawable.round_filter_24)

       object Favorite :
           Screens(title = "收藏", route = "fav_route", icons = R.drawable.round_favorite_24)

       object Profile :
           Screens(title = "我的", route = "profile_route", icons = R.drawable.round_person_24)
   }
   ```

4. 定义底部导航栏对应的，页面的路由

   这里跟登录跳转的路由一样，只不过这里的路由地址是定义在密封类 Screens里面的，这样做也是推荐做法，为了方便管理底部选项与页面的联动。

   ```kotlin
   /**
    * 将Home设为默认页面
    * */
   @Composable
   fun BottomNavHost(navHostController: NavHostController) {
       NavHost(navController = navHostController, startDestination = Screens.Home.route) {
           composable(route = Screens.Home.route) {
               HomeTabPage()
           }
           composable(route = Screens.Ranking.route) {
               RankingPage()
           }
           composable(route = Screens.Favorite.route) {
               FavoritePage()
           }
           composable(route = Screens.Profile.route) {
               ProfilePage()
           }
       }
   }
   ```




###### 2. 顶部滑动列表：

1.  实现思路：主要是根据滑动列表对ViewPager动态滚动，反过来ViewPage滚动也要联动到滑动列表。
2.  Compose提供了与ViewPager一样的组件，即Pager,分为HorizontalPager和VerticalPager， 这里需要引入Pager:



   关于Pager的了解可以看谷歌的文档，推荐一个比较好的用Pager实现banner轮播案例，来自大佬朱江的[Compose Banner](https://github.com/zhujiang521/Banner)

   ```groovy
    // 类似ViewPager
       implementation "com.google.accompanist:accompanist-pager:$accompanist_version"
   ```

3. 滑动tabView组件, 在 androidx.compose.material 下提供了两中tab样式，可滑动的和不可滑动的：

* TabView: 不可滚动，子元素等宽，排列装不下的时候文字方向会由横向变纵向。适合以下场景：

  <img src="C:\Users\X1 Carbon\Desktop\capture\_155914.jpg" style="zoom:50%;" />

*
  ScrollableTabRow 可以滚动，子项长度取决包裹内容。


```kotlin
// >源码
@Composable
fun TabRow(
    selectedTabIndex: Int,  // 选中的下标
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.colors.primarySurface,
    contentColor: Color = contentColorFor(backgroundColor),
    // 指示器默认是铺满内容的，此处用默认指示器
    indicator: @Composable (tabPositions: List<TabPosition>) -> Unit = @Composable { tabPositions ->
        TabRowDefaults.Indicator(
            Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex])
        )
    },
    divider: @Composable () -> Unit = @Composable {
        TabRowDefaults.Divider()
    },
    // 内容为Tab数组
    tabs: @Composable () -> Unit
)

// ScrollableTabRow 与TabRow参数差不多，只是多了两个参数：
// 首位间距pading
edgePadding: Dp = TabRowDefaults.ScrollableTabRowPadding,
// 底部与相邻内容的分割线
divider: @Composable () -> Unit = @Composable {
        TabRowDefaults.Divider()
    },
```



4. 滑动TabView的使用：

```kotlin
     val items = listOf("推荐", "电影", "电视剧", "综艺", "纪录片", "娱乐", "新闻")

    val tabstate = remember {
        mutableStateOf(items[0])
    }

	val pagerState = rememberPagerState(
        //pageCount = items.size, //总页数
        //initialOffscreenLimit = 3, //预加载的个数
        //infiniteLoop = false, //是否无限循环
        initialPage = 0 //初始页面
    )

   // 可滑动TabView
    ScrollableTabRow(
        selectedTabIndex = items.indexOf(tabstr.value),
        modifier = Modifier.wrapContentWidth(),
        edgePadding = 16.dp,
        // 这里用默认指示器，可自定义
        indicator = { tabIndicator ->
            TabRowDefaults.Indicator(
                Modifier.tabIndicatorOffset(
                    tabIndicator[items.indexOf(
                        tabstate.value
                    )]
                ),
                color = Color.Cyan
            )
        },
        // 背景色，有了一列tab，会挡住此背景，首尾各露出edgePadding的长度
        backgroundColor = colorResource(id = R.color.purple_500),
        // 底部分割线，可缺省，目的是为了与下面正文隔离开来
        divider = {
            TabRowDefaults.Divider(color = Color.Gray)
        }
    ) {
        items.forEachIndexed { index, title ->
            val selected = index == items.indexOf(tabstr.value)
            Tab(
                modifier = Modifier.background(color = colorResource(id = R.color.purple_200)),
                text = { Text(title, color = Color.White) },
                selected = selected,
                selectedContentColor = colorResource(id = R.color.purple_500),
                onClick = {
                    // 这里tabView与pager关联
                    tabstate.value = items[index]
                    scope.launch {
                        // Pager的切换
                        pagerState.scrollToPage(index)
                    }
                }
            )
        }
    }

   // 横向Pager类似PagerView
    HorizontalPager(
        state = pageState,
        count = items.size,
        reverseLayout = false
    ) { indexPage ->
       // 以下是Pager的内容
        Column(
            Modifier
                .fillMaxSize()
                .background(Color.White),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when (indexPage) {
                in 0..(items.size) -> Text(text = items[indexPage])

            }
        }
    }
```

###### 再说说指示器：

> ​     *@param indicator表示当前选择哪个TAB的指示器。在默认情况下是一个[TabRowDefaults.Indicator]，使用[TabRowDefaults.tabIndicatorOffset]
>
> ​     * 修饰符来确定它的位置。注意此指示符将被强制填满整个TabRow，所以你应该使用[TabRowDefaults.tabIndicatorOffset]或类似的来修改偏移量
>
可以看到，tabIndicatorOffset里面处理指示器动画

```kotlin
// >源码
fun Modifier.tabIndicatorOffset(
        currentTabPosition: TabPosition
    ): Modifier = composed(
        inspectorInfo = debugInspectorInfo {
            name = "tabIndicatorOffset"
            value = currentTabPosition
        }
    ) {
        val currentTabWidth by animateDpAsState(
            targetValue = currentTabPosition.width,
            animationSpec = tween(durationMillis = 250, easing = FastOutSlowInEasing)
        )
        // animateDpAsState 是动画包装，随着动画进度，会返回一个变化的值，感觉有点像估值器
        val indicatorOffset by animateDpAsState(
            targetValue = currentTabPosition.left,
            animationSpec = tween(durationMillis = 250, easing = FastOutSlowInEasing)
        )
        fillMaxWidth()
            .wrapContentSize(Alignment.BottomStart)
            .offset(x = indicatorOffset) // 动画移动的偏移量
            .width(currentTabWidth)
}
```



* 自定义指示器：

自定义指示器可以给指示器添加需要的动画，样式和颜色。

```kotlin
// 1.怎么画指示器？ 可以用draw画一个想或者图像，也可以modifier设置一个backaground,这里shape用了圆角矩形
@Composable
fun BiliIndicator(
    height: Dp = TabRowDefaults.IndicatorHeight,
    color: Color = bili_50,
    modifier: Modifier = Modifier
) {
    Box(
        modifier
        	// 每一个指示器的子项的大小位置约束
            .padding(top = 5.dp,bottom = 2.dp, start = 16.dp, end = 16.dp)
            .fillMaxWidth()
            .height(height)
            // 如果不用background，用border那就是一个框框
            .background(color = color,shape = RoundedCornerShape(size = 4.dp))

            // 不用上面的想draw的话也可以自己draw:
            /*.drawWithContent(onDraw = {
                drawLine(
                    color = color,
                    strokeWidth = 8f,
                    start = Offset(0f, size.height),
                    end = Offset(size.width, size.height),
                    pathEffect = PathEffect.cornerPathEffect(radius = 5f)
                )
            })*/
    )
}

// 2. 指示器的动画定义，定义完了才去绘制上面的指示器样式
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun BiliAnimatedIndicator(tabPositions: List<TabPosition>, selectedTabIndex: Int) {
    // 可以指定一些颜色，每个滑动子项取一个颜色
    val colors = listOf(Color.Yellow, Color.Red, Color.Green)
    // transition 过渡的意思，用来处理动画，作用对象target是选中的tab即selectedTabIndex
    val transition = updateTransition(selectedTabIndex, label = "Transition")
    // 定义起始点
    val indicatorStart by transition.animateDp(
        label = "Indicator Start",
        transitionSpec = {
            // 如果向右移动，则右边移动速度快
            // 如果向左移动，则左边速度快
            // spring提供了一个弹性空间,AnimatorSpace的一种，与补间动画tween,关键帧动画keyframes是类似的概念。
            // 阻尼比dampingRatio默认1f， 刚度：stiffness
            if (initialState < targetState) {
                spring(dampingRatio = 1f, stiffness = 50f)
            } else {
                spring(dampingRatio = 1f, stiffness = 1000f)
            }
        }
    ) {
        // TabPosition有left,right,width三个属性，描述的是你一个tab在整个tabView里面的位置和大小
        // 所以有 left + width = right
        tabPositions[it].left
    }

    // 定义终点
    val indicatorEnd by transition.animateDp(
        label = "Indicator End",
        transitionSpec = {
            if (initialState < targetState) {
                spring(dampingRatio = 1f, stiffness = 1000f)
            } else {
                spring(dampingRatio = 1f, stiffness = 50f)
            }
        }
    ) {
        tabPositions[it].right
    }

    // 可选颜色 列表colors指定的其中一个
    val indicatorColor1 by transition.animateColor(label = "Indicator Color") {
        colors[it % colors.size]
    }

    //val indicatorColor2 = ColorUtil.getRandomColor(bili_50)

    // 绘制指示器子项
    BiliIndicator2(
        // indicator当前颜色，指定颜色有默认值
        //color = indicatorColor1,
        height = 5.dp,
        modifier = Modifier
            // 填满整个TabView,并把指示器放在开始位置
            .fillMaxSize()
            .wrapContentSize(align = Alignment.BottomStart)
            // 设置偏移量定位指示器的开始位置
            .offset(x = indicatorStart)
            // 在选项卡之间移动时，指示器的宽度与动画宽度一致
            .width(indicatorEnd - indicatorStart)
    )
}
```

画完了指示器，替代上面ScrollTabView中的 indicator = xxx, 就可以看到下面的效果：

<img src="C:\Users\X1 Carbon\Desktop\capture\ezgif-3-e49ae2699e2e.gif" style="zoom:80%;" />
