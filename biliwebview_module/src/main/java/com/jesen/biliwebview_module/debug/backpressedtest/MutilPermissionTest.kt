import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.jesen.common_util_lib.utils.oLog
import com.jesen.common_util_lib.utils.showToast

@RequiresApi(Build.VERSION_CODES.N)
@Composable
fun MutilPermissionTest() {

    var remembers = remember {
        mutableStateOf(
            arrayListOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.CAMERA
            )
        )
    }

    val context = LocalContext.current

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) {

        it.entries.filter { entry ->
            oLog("请求结果：权限${entry.key}${if (entry.value) "通过" else "被拒绝"}")
            entry.value
        }.map { successEntry ->
            if (remembers.value.contains(successEntry.key)) {
                remembers.value.remove(successEntry.key)
            }
        }
        if (remembers.value.isEmpty()) {
            showToast(context, "权限全部通过")
        }
    }

    Button(
        onClick = {
            if (remembers.value.isNullOrEmpty()) {
                showToast(context, "权限全部通过")
                oLog("权限全部通过")
            } else {

                // 检查所有权限
                val allFailed = remembers.value.map { it ->
                    mutableListOf(
                        it to (ContextCompat.checkSelfPermission(
                            context,
                            it
                        ) == PackageManager.PERMISSION_GRANTED)
                    ).filter { entry ->
                        // 过滤未通过权限
                        !entry.second
                    }.map { entry ->
                        if (entry.second) {
                            remembers.value.remove(entry.first)
                        }
                    }
                }

                if (allFailed.isNotEmpty()) {
                    launcher.launch(remembers.value.toTypedArray())
                }
            }
        },
    ) {
        Text(text = "检查并请求多个权限")
    }
}
