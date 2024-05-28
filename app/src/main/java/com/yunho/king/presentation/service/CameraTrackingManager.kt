package com.yunho.king.presentation.service

import android.app.usage.UsageStats
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.PixelFormat
import android.graphics.drawable.Drawable
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraDevice
import android.hardware.camera2.CameraManager
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.PointerIcon
import android.view.Window
import android.view.WindowManager
import androidx.camera.core.CameraProvider
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.core.content.getSystemService
import com.google.common.util.concurrent.ListenableFuture
import com.yunho.king.GlobalApplication
import com.yunho.king.databinding.PopupSuspicionBinding
import com.yunho.king.domain.di.RepositorySource
import java.util.Calendar
import javax.inject.Inject

class CameraTrackingManager(context: Context) {

    @Inject lateinit var repo: RepositorySource

    var mContext: Context = context

    lateinit var cameraManager: CameraManager
    lateinit var cameraIds: Array<String>
    lateinit var popupSuspicionBinding: PopupSuspicionBinding
    lateinit var cameraProviderFuture: ListenableFuture<ProcessCameraProvider>
    lateinit var cameraProvider: ProcessCameraProvider
    lateinit var stateManager: UsageStatsManager
    lateinit var packageManager: PackageManager
    lateinit var packageName: String
    lateinit var appName: String
    lateinit var appIcon: Drawable
    lateinit var appInfo: ApplicationInfo

    var canOpenCamra: Boolean = false

    fun setCameraTracker() {
        cameraProviderFuture = ProcessCameraProvider.getInstance(mContext)
        cameraManager = mContext.getSystemService(Context.CAMERA_SERVICE) as CameraManager
        stateManager = mContext.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
        packageManager = mContext.packageManager
        cameraIds = cameraManager.cameraIdList

        cameraManager.registerAvailabilityCallback(object: CameraManager.AvailabilityCallback() {
            override fun onCameraAccessPrioritiesChanged() {
                super.onCameraAccessPrioritiesChanged()
            }

            override fun onCameraAvailable(cameraId: String) { // 카메라 사용 가능 할 때
                super.onCameraAvailable(cameraId)

            }

            override fun onCameraUnavailable(cameraId: String) { // 카메라 사용이 불가능 할 때!
                super.onCameraUnavailable(cameraId)
                getRecentlyCameraUserPackage()

            }
        }, Handler(Looper.getMainLooper()))
    }

    fun getRecentlyCameraUserPackage() {

        val cal = Calendar.getInstance()
        cal.add(Calendar.SECOND, -1)

        val lastUsagePackageList = stateManager.queryUsageStats(
            UsageStatsManager.INTERVAL_BEST, cal.timeInMillis, System.currentTimeMillis())

        for (pkg in lastUsagePackageList) {
            Log.d(GlobalApplication.TagName,
                "AppName: ${pkg.packageName}\n" +
                        "LastTimeUse : ${pkg.lastTimeUsed}")
        }
    }

    fun setAppInfo() {
        appInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            packageManager.getApplicationInfo(packageName, PackageManager.ApplicationInfoFlags.of(0))
        } else {
            packageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA)
        }
    }

    fun getAppName() {
        appName = packageManager.getApplicationLabel(appInfo).toString()
    }

    fun getAppIcon() {
        appIcon = packageManager.getApplicationIcon(appInfo)
    }

    fun setSuspicionPopup() {
        popupSuspicionBinding = PopupSuspicionBinding.inflate(
            LayoutInflater.from(mContext),
            null,
            false)

        val flag = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        } else {
            WindowManager.LayoutParams.TYPE_PHONE
        }

        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            flag,
            WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON or
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            PixelFormat.TRANSLUCENT)

        params.x = 0
        params.y = 0
        params.gravity = Gravity.CENTER_VERTICAL

        val windowManager = mContext.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        windowManager.addView(popupSuspicionBinding.root, params)
    }

    fun openCamera2() {
        cameraProviderFuture.addListener(
            {
             cameraProvider = cameraProviderFuture.get()
                val preview = Preview.Builder()
                    .build()
                    .also {
                        it.setSurfaceProvider(popupSuspicionBinding.cameraPreview.surfaceProvider)
                    }
        },
        ContextCompat.getMainExecutor(mContext))
    }

}