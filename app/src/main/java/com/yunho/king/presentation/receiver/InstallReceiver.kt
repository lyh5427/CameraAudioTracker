package com.yunho.king.presentation.receiver

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import com.yunho.king.domain.di.RepositorySource
import com.yunho.king.domain.dto.AudioAppData
import com.yunho.king.domain.dto.CameraAppData
import com.yunho.king.presentation.Utils.Util
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class InstallReceiver: BroadcastReceiver() {
    @Inject
    lateinit var repo: RepositorySource

    override fun onReceive(context: Context?, intent: Intent?) {

        when (intent?.action) {
            Intent.ACTION_PACKAGE_ADDED -> {
                addedProcess(context, intent)
            }

            Intent.ACTION_PACKAGE_REMOVED -> {
                removeProcess(context, intent)
            }
        }
    }

    private fun addedProcess(context: Context?, intent: Intent?) {
        val packageName = intent?.data?.schemeSpecificPart

        if (packageName != null) {
            val appName = Util.getAppName(packageName, context!!)
            val packageInfo = context
                .packageManager
                .getPackageInfo(
                    packageName,
                    PackageManager.GET_PERMISSIONS
                )

            if (checkCameraPerm(packageInfo)) {
                val appData = CameraAppData(
                    appPackageName = packageName,
                    appName = appName,
                    permState = false
                )

                CoroutineScope(Dispatchers.IO).launch {
                    repo.insertCameraApp(appData)
                }
            }

            if (checkAudioPerm(packageInfo)) {
                val appData = AudioAppData(
                    appPackageName = packageName,
                    appName = appName,
                    permState = false
                )

                CoroutineScope(Dispatchers.IO).launch {
                    repo.insertAudioApp(appData)
                }
            }
        }
    }

    private fun removeProcess(context: Context?, intent: Intent?) {
        val packageName = intent?.data?.schemeSpecificPart

        CoroutineScope(Dispatchers.IO).launch {
            repo.deleteAudioApp(packageName?: "")
            repo.deleteCameraApp(packageName?: "")
        }
    }

    private fun checkCameraPerm(packageInfo: PackageInfo): Boolean {
        packageInfo.requestedPermissions?.forEach {
            if (it == Manifest.permission.CAMERA) {
                return true
            }
        }

        return false
    }

    private fun checkAudioPerm(packageInfo: PackageInfo): Boolean {
        packageInfo.requestedPermissions?.forEach {
            if (it == Manifest.permission.RECORD_AUDIO) {
                return true
            }
        }

        return false
    }
}