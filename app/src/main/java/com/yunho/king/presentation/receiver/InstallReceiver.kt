package com.yunho.king.presentation.receiver

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.util.Log
import com.yunho.king.GlobalApplication
import com.yunho.king.domain.repository.RepositorySource
import com.yunho.king.core.model.AudioAppData
import com.yunho.king.core.model.CameraAppData
import com.yunho.king.presentation.Utils.Util
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class InstallReceiver: BroadcastReceiver() {
    @Inject lateinit var repo: RepositorySource

    private val receiverScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onReceive(context: Context?, intent: Intent?) {
        Log.i(GlobalApplication.TagName, "Receiver Event ${intent?.action}")

        val ctx = context ?: return
        val pendingResult = goAsync()

        when (intent?.action) {
            Intent.ACTION_PACKAGE_ADDED -> {
                if (intent.getBooleanExtra(Intent.EXTRA_REPLACING, false)) {
                    pendingResult.finish()
                    return
                }
                addedProcess(ctx, intent, pendingResult)
            }

            Intent.ACTION_PACKAGE_REMOVED -> {
                removeProcess(intent, pendingResult)
            }

            else -> pendingResult.finish()
        }
    }

    private fun addedProcess(context: Context, intent: Intent?, pendingResult: PendingResult) {
        val packageName = intent?.data?.schemeSpecificPart ?: run {
            pendingResult.finish()
            return
        }

        receiverScope.launch {
            try {
                val appName = Util.getAppName(packageName, context)
                val packageInfo = runCatching {
                    context.packageManager.getPackageInfo(
                        packageName,
                        PackageManager.GET_PERMISSIONS
                    )
                }.getOrNull() ?: return@launch

                if (checkCameraPerm(packageInfo)) {
                    runCatching {
                        repo.insertCameraApp(
                            CameraAppData(
                                appPackageName = packageName,
                                appName = appName,
                                permState = false
                            )
                        )
                    }.onFailure {
                        Log.w(GlobalApplication.TagName, "Failed to insert camera app: $packageName", it)
                    }
                }

                if (checkAudioPerm(packageInfo)) {
                    runCatching {
                        repo.insertAudioApp(
                            AudioAppData(
                                appPackageName = packageName,
                                appName = appName,
                                permState = false
                            )
                        )
                    }.onFailure {
                        Log.w(GlobalApplication.TagName, "Failed to insert audio app: $packageName", it)
                    }
                }
            } finally {
                pendingResult.finish()
            }
        }
    }

    private fun removeProcess(intent: Intent?, pendingResult: PendingResult) {
        val packageName = intent?.data?.schemeSpecificPart ?: run {
            pendingResult.finish()
            return
        }

        receiverScope.launch {
            try {
                repo.deleteAudioApp(packageName)
                repo.deleteCameraApp(packageName)
            } finally {
                pendingResult.finish()
            }
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
