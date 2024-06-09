package com.yunho.king.Utils

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.graphics.drawable.Drawable
import com.yunho.king.R
import java.text.SimpleDateFormat

object Util {

    fun getAppName(packageName: String, context: Context): String {
        return try {
            val appInfo =
                context.packageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA)
            context.packageManager.getApplicationLabel(appInfo).toString()
        } catch (e: Exception) {
            context.resources.getString(R.string.not_fount_app_name)
        }
    }

    fun getAppIcon(packageName: String, context: Context): Drawable? {
        return try {
            val appInfo =
                context.packageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA)

            context.packageManager.getApplicationIcon(appInfo)
        } catch (e: Exception) {
            null
        }
    }

    fun getAllPackage(context: Context): List<ResolveInfo> {
        val mainIntent = Intent(Intent.ACTION_MAIN, null).apply {
            addCategory(Intent.CATEGORY_LAUNCHER)
        }
        return context.packageManager.queryIntentActivities(mainIntent, 0)
    }

    fun getCameraAppList(packageList: List<ResolveInfo>, context: Context): ArrayList<String> {
        val cameraAppList = ArrayList<String>()

        packageList.map {
            val packageName = it.activityInfo.packageName
            val packageInfo = context.packageManager.getPackageInfo(
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

    fun getAudioAppList(packageList: List<ResolveInfo>, context: Context): ArrayList<String> {
        val audioAppList = ArrayList<String>()

        packageList.map {
            val packageName = it.activityInfo.packageName
            val packageInfo = context.packageManager.getPackageInfo(
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

    @SuppressLint("SimpleDateFormat")
    fun getDate(time: Long): String {
        val myDate = SimpleDateFormat("MM월 dd일 hh:mm:ss")
        return myDate.format(time)
    }



}