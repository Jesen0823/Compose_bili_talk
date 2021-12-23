package com.jesen.biliwebview_module.debug.utils

import android.content.Context
import android.net.Uri
import android.os.Build
import androidx.core.content.FileProvider
import com.jesen.common_util_lib.utils.oLog
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


object FileUtil {

    fun getPicUri(context: Context): Uri? {
        val path: String = "${context.filesDir}${File.separator}"
        val file = File(path, System.currentTimeMillis().toString() + ".jpg")
        if (!file.parentFile.exists()) {
            file.parentFile.mkdirs()
        }
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //  步骤二：Android 7.0及以上获取文件 Uri
            FileProvider.getUriForFile(
                context,
                context.applicationInfo.packageName.toString() + ".ftWebView",
                file
            )
        } else {
            //  步骤三：获取文件Uri
            Uri.fromFile(file)
        }
    }

    fun getImgUri(context: Context): Uri {
        val photoFile = createFile(context)
        val authority = "${context.applicationInfo.packageName}.provider"
        oLog("getImgUri, ")
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            FileProvider.getUriForFile(context, authority, photoFile)
        } else {
            Uri.fromFile(photoFile)
        }
    }

    private fun getOutputDirectory(context: Context): File {
        /* val mediaDir = context.externalMediaDirs.firstOrNull()?.let {
             File(it, context.resources.getString(R.string.app_name)).apply { mkdirs() }
         }
         return if (mediaDir != null && mediaDir.exists())
             mediaDir else context.filesDir*/
        return context.filesDir
    }

    private fun createFile(context: Context): File =
        File(
            "${getOutputDirectory(context).absolutePath}/images/",
            SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS", Locale.CHINA)
                .format(System.currentTimeMillis()) + ".jpg"
        )

}