package com.jesen.common_util_lib.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DownloadManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.*
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ContextThemeWrapper
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import java.io.File

/**
 * 快速创建通知渠道
 *
 * @param channelId 渠道ID
 * @param name 渠道名称
 * @param importance 渠道通知的重要程度
 * @param description 渠道描述
 */
@SuppressLint("WrongConstant")
fun Context.createNotificationChannel(
    channelId: String,
    name: String,
    importance: Int = NotificationManager.IMPORTANCE_LOW, // 2 = Low
    description: String? = null,
) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        (this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager).apply {
            createNotificationChannel(
                NotificationChannel(
                    channelId,
                    name,
                    importance
                ).apply {
                    description?.let {
                        this.description = description
                    }
                    setSound(null, null)
                }
            )
        }
    }
}

val Context.autoRotation: Boolean
    get() = try {
        Settings.System.getInt(contentResolver, Settings.System.ACCELEROMETER_ROTATION, 0) == 1
    } catch (e: Exception) {
        e.printStackTrace()
        false
    }

fun Context.stringResource(id: Int) = this.resources.getString(id)

fun Context.stringResource(id: Int, vararg formatArgs: Any) =
    this.resources.getString(id, *formatArgs)

fun Context.openUrl(url: String) {
    Toast.makeText(this, "打开链接: $url", Toast.LENGTH_SHORT).show()
    try {
        val intent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse(url.let {
                if (!it.startsWith("https://")) "https://$it" else it
            })
        )
        startActivity(intent)
    } catch (e: Exception) {
        e.printStackTrace()
        Toast.makeText(
            this,
            "打开链接失败: ${e.javaClass.simpleName}(${e.localizedMessage})",
            Toast.LENGTH_SHORT
        ).show()
    }
}


fun Context.setClipboard(text: String) {
    val clipboardManager = this.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    clipboardManager.setPrimaryClip(ClipData.newPlainText(null, text))
    Toast.makeText(this, "已复制到剪贴板", Toast.LENGTH_SHORT).show()
}

fun Context.getVersionName(): String {
    var versionName = ""
    try {
        //获取软件版本号，对应AndroidManifest.xml下android:versionName
        versionName = this.packageManager.getPackageInfo(this.packageName, 0).versionName;
    } catch (e: Exception) {
        e.printStackTrace();
    }
    return versionName;
}

// 判断网络是否加密
// 总之反正理解一下
fun Context.isFreeNetwork(): Boolean {
    val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
    val activeNetwork = connectivityManager?.activeNetwork
    val networkCapabilities = connectivityManager?.getNetworkCapabilities(activeNetwork)
    return networkCapabilities?.let {
        when {
            // WIFI
            it.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            // 以太网
            it.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            // 蜂窝
            it.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> false
            // 未知
            else -> true
        }
    } ?: true
}

fun Context.downloadImageNew(filename: String, downloadUrlOfImage: String) {
    try {
        val dm: DownloadManager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        val downloadUri = Uri.parse(downloadUrlOfImage)
        val request: DownloadManager.Request = DownloadManager.Request(downloadUri)
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
            .setAllowedOverRoaming(false)
            .setTitle(filename)
            .setMimeType("image/jpeg") // Your file type. You can use this code to download other file types also.
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setDestinationInExternalPublicDir(
                Environment.DIRECTORY_PICTURES,
                File.separator.toString() + filename + ".jpg"
            )
        dm.enqueue(request)
        Toast.makeText(this, "开始保存图片", Toast.LENGTH_SHORT).show()
    } catch (e: java.lang.Exception) {
        Toast.makeText(this, "保存图片失败: ${e.javaClass.simpleName}", Toast.LENGTH_SHORT).show()
    }
}

val Context.activity: Activity?
    get() {
        return when (this) {
            is Activity -> {
                this
            }
            is ContextWrapper -> {
                this.baseContext.activity
            }
            else -> {
                null
            }
        }
    }

val Context.appCompActivity: AppCompatActivity?
    get() {
        return when (this) {
            is AppCompatActivity -> {
                this
            }
            is ContextThemeWrapper -> {
                this.baseContext.appCompActivity
            }
            else -> {
                null
            }
        }
    }

val Context.componentActivity: ComponentActivity?
    get() {
        return when (this) {
            is ComponentActivity -> {
                this
            }
            is ContextThemeWrapper -> {
                this.baseContext.componentActivity
            }
            else -> {
                null
            }
        }
    }

/**
 * 状态栏显示/隐藏
 * */
fun Activity.statusBarIsHide(view: View, enable: Boolean) {
    val controller = ViewCompat.getWindowInsetsController(view)
    if (enable) {
        controller?.hide(WindowInsetsCompat.Type.statusBars())
        controller?.hide(WindowInsetsCompat.Type.navigationBars())
        controller?.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE

        //controller?.hide(WindowInsetsCompat.Type.systemBars())
    } else {
        controller?.show(WindowInsetsCompat.Type.statusBars())
        controller?.show(WindowInsetsCompat.Type.navigationBars())

        //controller?.show(WindowInsetsCompat.Type.systemBars())
    }
}