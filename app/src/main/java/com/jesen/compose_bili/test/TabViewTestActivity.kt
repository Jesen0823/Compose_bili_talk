package com.jesen.compose_bili.test

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.material.Surface
import androidx.compose.ui.graphics.Color
import com.jesen.compose_bili.ui.theme.Compose_bili_talkTheme
import com.jesen.compose_bili.ui.widget.TestIndicatorOfTabView

class TabViewTestActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            Compose_bili_talkTheme {
                Surface(color = Color.Yellow.copy(alpha = 0.5f)) {
                    TestIndicatorOfTabView()
                    /*LazyColumn(modifier = Modifier.fillMaxSize()) {
                        item {
                            Text(text = "1. TestFancyIndicatorContainerTabs: ")
                            TestFancyIndicatorContainerTabs()
                        }
                        item {
                            Text(text = "2. TestFancyIndicatorTabs: ")
                            TestFancyIndicatorTabs()
                        }
                        item {
                            Text(text = "3. TestFancyTabs: ")
                            TestFancyTabs()
                        }
                        item {
                            Text(text = "4. TestLeadingIconTabs: ")
                            TestLeadingIconTabs()

                        }
                        item {
                            Text(text = "5. TestScrollingFancyIndicatorContainerTabs: ")
                            TestScrollingFancyIndicatorContainerTabs()

                        }
                        item {
                            Text(text = "6. TestScrollingTextTabs: ")
                            TestScrollingTextTabs()

                        }
                        item {
                            Text(text = "7. TestTextAndIconTabs: ")
                            TestTextAndIconTabs()

                        }
                        item {
                            Text(text = "8.TestIconTabs: ")
                            TestIconTabs()

                        }

                        item {
                            Text(text = "9. TestTextTabs: ")
                            TestTextTabs()
                        }

                    }*/
                }
            }

        }
    }
}