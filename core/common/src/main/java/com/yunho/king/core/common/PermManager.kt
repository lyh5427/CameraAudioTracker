package com.yunho.king.core.common

import android.app.AppOpsManager
import android.app.AppOpsManager.MODE_ALLOWED
import android.app.AppOpsManager.OPSTR_GET_USAGE_STATS
import android.content.Context
import android.content.Context.APP_OPS_SERVICE
import android.content.pm.PackageManager
import android.os.Build
import android.provider.Settings
import androidx.core.content.ContextCompat

class PermManager(private val context: Context) {

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
        if (!(isOverlayAllow() && isUsagesPermAllow())) return false
        for (perm in permArr) {
            if (ContextCompat.checkSelfPermission(context, perm) != PackageManager.PERMISSION_GRANTED) {
                return false
            }
        }
        return true
    }

    fun isRuntimePermAllow(): Boolean {
        for (perm in permArr) {
            if (ContextCompat.checkSelfPermission(context, perm) != PackageManager.PERMISSION_GRANTED) {
                return false
            }
        }
        return true
    }

    fun isPermAllow(permName: String): Boolean =
        ContextCompat.checkSelfPermission(context, permName) == PackageManager.PERMISSION_GRANTED

    fun isNowPermAllow(permIndex: Int): Boolean {
        val permName = permArr.getOrNull(permIndex) ?: return false
        return ContextCompat.checkSelfPermission(context, permName) == PackageManager.PERMISSION_GRANTED
    }

    fun getRuntimePermissionAt(index: Int): String? = permArr.getOrNull(index)

    fun runtimePermissionCount(): Int = permArr.size

    fun isUsagesPermAllow(): Boolean {
        val appOps = context.getSystemService(APP_OPS_SERVICE) as? AppOpsManager ?: return false
        val mode = try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                appOps.unsafeCheckOpNoThrow(
                    OPSTR_GET_USAGE_STATS,
                    android.os.Process.myUid(),
                    context.packageName
                )
            } else {
                @Suppress("DEPRECATION")
                appOps.checkOpNoThrow(
                    OPSTR_GET_USAGE_STATS,
                    android.os.Process.myUid(),
                    context.packageName
                )
            }
        } catch (e: SecurityException) {
            return false
        }
        return if (mode == AppOpsManager.MODE_DEFAULT) {
            ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.PACKAGE_USAGE_STATS
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            mode == MODE_ALLOWED
        }
    }

    fun isOverlayAllow(): Boolean = Settings.canDrawOverlays(context)

    fun getRequiredRuntimePermissions(): Array<String> = permArr
}
