package com.yunho.king.presentation.ui.base

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.graphics.Color
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import com.yunho.king.R
import com.yunho.king.domain.dto.AudioAppData
import com.yunho.king.domain.dto.CameraAppData
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
open class BaseActivity : AppCompatActivity() {
    private val baseViewModel: BaseViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setActionBar()
        setFullScreen()
    }

    fun setActionBar() {
        supportActionBar!!.hide()
        supportActionBar!!.setDisplayShowTitleEnabled(false)
    }

    fun setFullScreen() {
        this.window?.apply {
            this.statusBarColor = Color.TRANSPARENT
            decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
    }


    fun moveToIntent(intent: Intent) {
        startActivity(intent)
    }

    fun getAppName(packageName: String): String {
        return try {
            val appInfo =
                packageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA)
            packageManager.getApplicationLabel(appInfo).toString()
        } catch (e: Exception) {
            resources.getString(R.string.not_fount_app_name)
        }
    }

    fun getAppIcon(packageName: String): Drawable? {
        return try {
            val appInfo =
                packageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA)

            packageManager.getApplicationIcon(appInfo)
        } catch (e: Exception) {
            null
        }
    }

    fun getAllPackage(): List<ResolveInfo> {
        val mainIntent = Intent(Intent.ACTION_MAIN, null).apply {
            addCategory(Intent.CATEGORY_LAUNCHER)
        }
        return packageManager.queryIntentActivities(mainIntent, 0)
    }

    fun getCameraAppList(packageList: List<ResolveInfo>): ArrayList<String> {
        val cameraAppList = ArrayList<String>()

        packageList.map {
            val packageName = it.activityInfo.packageName
            val packageInfo = packageManager.getPackageInfo(
                packageName, PackageManager.GET_PERMISSIONS
            )

            if (packageInfo.requestedPermissions != null) {
                for (perm in packageInfo.requestedPermissions) {
                    if (perm == Manifest.permission.CAMERA) {
                        cameraAppList.add(packageName)
                    }
                }
            }
        }

        return cameraAppList
    }

    fun getAudioAppList(packageList: List<ResolveInfo>): ArrayList<String> {
        val audioAppList = ArrayList<String>()

        packageList.map {
            val packageName = it.activityInfo.packageName
            val packageInfo = packageManager.getPackageInfo(
                packageName, PackageManager.GET_PERMISSIONS
            )

            if (packageInfo.requestedPermissions != null) {
                for (perm in packageInfo.requestedPermissions) {
                    if (perm == Manifest.permission.RECORD_AUDIO) {
                        audioAppList.add(packageName)
                    }
                }
            }
        }
        return audioAppList
    }

    fun insertCameraPackage() {
        val list = getCameraAppList(getAllPackage())

        if (list.isNotEmpty()) {
            for (packageName in list) {
                CoroutineScope(Dispatchers.IO).launch {
                    if (baseViewModel.isExistCameraApp(packageName)) {
                        val appData = CameraAppData(
                            appPackageName = packageName,
                            appName = getAppName(packageName),
                        )
                        baseViewModel.insertCameraApp(appData)
                    }
                }
            }
        }
    }

    fun insertAudioPackage() {
        val list = getAudioAppList(getAllPackage())

        if (list.isNotEmpty()) {
            for (packageName in list) {
                CoroutineScope(Dispatchers.IO).launch {
                    if (baseViewModel.isExistAudioApp(packageName)) {
                        val appData = AudioAppData(
                            appPackageName = packageName,
                            appName = getAppName(packageName),
                        )
                        baseViewModel.insertAudioApp(appData)
                    }
                }
            }
        }
    }
}