package com.yunho.king.Utils

import android.app.AppOpsManager
import android.app.AppOpsManager.MODE_ALLOWED
import android.app.AppOpsManager.OPSTR_GET_USAGE_STATS
import android.content.Context
import android.content.Context.APP_OPS_SERVICE
import android.content.pm.PackageManager
import android.os.Build
import android.provider.Settings
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService

class PermManager(context: Context) {
    private val mContext: Context = context
    private val permArr = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        arrayOf(
            android.Manifest.permission.POST_NOTIFICATIONS,
            android.Manifest.permission.CAMERA,
            android.Manifest.permission.RECORD_AUDIO
        )
    } else {
        arrayOf(
            android.Manifest.permission.CAMERA,
            android.Manifest.permission.RECORD_AUDIO
        )
    }

    fun isAllPermAllow(): Boolean {
        if (!(isOverlayAllow() || isUsagesPermAllow())) return false

        for (i in permArr) {
            if (ContextCompat.checkSelfPermission(mContext, i)
                != PackageManager.PERMISSION_GRANTED) {
                return false
            }
        }
        return true
    }

    fun isRuntimePermAllow(): Boolean {
        for (i in permArr) {
            if (ContextCompat.checkSelfPermission(mContext, i)
                != PackageManager.PERMISSION_GRANTED) {
                return false
            }
        }
        return true
    }

    fun isPermAllow(permName: String): Boolean =
        ContextCompat.checkSelfPermission(mContext, permName) == PackageManager.PERMISSION_GRANTED

    fun isNowPermAllow(permIndex: Int): Boolean {
        val permName = permArr[permIndex]

        return ContextCompat.checkSelfPermission(
            mContext,
            permName
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun isNowPerm(permIndex: Int): String = permArr[permIndex]

    fun permSize(): Int = permArr.size-1

    fun isUsagesPermAllow(): Boolean {
        val appOps = mContext.getSystemService(APP_OPS_SERVICE) as AppOpsManager
        val mode = try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                appOps.unsafeCheckOpNoThrow(
                    OPSTR_GET_USAGE_STATS,
                    android.os.Process.myUid(),
                    mContext.packageName
                )
            } else {
                appOps.checkOpNoThrow(
                    OPSTR_GET_USAGE_STATS, android.os.Process.myUid(), mContext.packageName)
            }
        } catch (exception: SecurityException) {
            9999
        }

        return if(mode == AppOpsManager.MODE_DEFAULT) {
            ContextCompat.checkSelfPermission(
                mContext,
                android.Manifest.permission.PACKAGE_USAGE_STATS) == PackageManager.PERMISSION_GRANTED
        } else {
            mode == MODE_ALLOWED
        }
    }

    fun isOverlayAllow(): Boolean = Settings.canDrawOverlays(mContext)
/*
    private fun hasUsageStatsPermission(context: Context): Boolean {
        val appOps = context.getSystemService(APP_OPS_SERVICE) as AppOpsManager
        val mode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            appOps.unsafeCheckOpNoThrow(
                OPSTR_GET_USAGE_STATS,
                android.os.Process.myUid(), context.packageName
            )
        } else {
            appOps.checkOpNoThrow(
                OPSTR_GET_USAGE_STATS,
                android.os.Process.myUid(), context.packageName
            )
        }
        return mode == MODE_ALLOWED
    }*/
}