package com.yunho.king.data.local

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import com.yunho.king.core.model.ScannedApp
import com.yunho.king.domain.source.InstalledAppScannerSource
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InstalledAppScannerImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : InstalledAppScannerSource {

    override fun scanCameraApps(): List<ScannedApp> =
        getLauncherApps()
            .filter { hasPermission(it, Manifest.permission.CAMERA) }
            .map { toScannedApp(it) }
            .distinctBy { it.packageName }

    override fun scanAudioApps(): List<ScannedApp> =
        getLauncherApps()
            .filter { hasPermission(it, Manifest.permission.RECORD_AUDIO) }
            .map { toScannedApp(it) }
            .distinctBy { it.packageName }

    private fun getLauncherApps(): List<ResolveInfo> {
        val mainIntent = Intent(Intent.ACTION_MAIN).apply {
            addCategory(Intent.CATEGORY_LAUNCHER)
        }
        return context.packageManager.queryIntentActivities(mainIntent, PackageManager.MATCH_ALL)
    }

    private fun hasPermission(resolveInfo: ResolveInfo, permission: String): Boolean {
        val packageName = resolveInfo.activityInfo.packageName
        val packageInfo = context.packageManager.getPackageInfo(
            packageName,
            PackageManager.GET_PERMISSIONS
        )
        return packageInfo.requestedPermissions?.contains(permission) == true
    }

    private fun toScannedApp(resolveInfo: ResolveInfo): ScannedApp {
        val packageName = resolveInfo.activityInfo.packageName
        val appName = try {
            val appInfo = context.packageManager.getApplicationInfo(packageName, 0)
            context.packageManager.getApplicationLabel(appInfo).toString()
        } catch (_: Exception) {
            packageName
        }
        return ScannedApp(packageName = packageName, appName = appName)
    }
}
