package com.yunho.king.presentation.Utils

import android.content.Context
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import com.yunho.king.core.designsystem.R as DesignR
import java.text.SimpleDateFormat

object Util {

    fun getAppName(packageName: String, context: Context): String {
        return try {
            val appInfo =
                context.packageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA)
            context.packageManager.getApplicationLabel(appInfo).toString()
        } catch (e: Exception) {
            context.resources.getString(DesignR.string.not_fount_app_name)
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

    fun getDate(time: Long): String {
        val myDate = SimpleDateFormat("MM월 dd일 hh:mm:ss")
        return myDate.format(time)
    }
}
