package com.yunho.king.presentation.service

import android.app.Application
import android.app.Service
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.Camera
import android.graphics.PixelFormat
import android.graphics.drawable.Drawable
import android.hardware.camera2.CameraManager
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.provider.Settings.Global
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.WindowManager
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import com.google.common.util.concurrent.ListenableFuture
import com.yunho.king.Const
import com.yunho.king.GlobalApplication
import com.yunho.king.Utils.singleClickListener
import com.yunho.king.Utils.toGone
import com.yunho.king.Utils.toVisible
import com.yunho.king.databinding.PopupSuspicionBinding
import com.yunho.king.domain.di.RepositorySource
import com.yunho.king.domain.dto.CameraAppData
import com.yunho.king.presentation.ui.cameraintercept.CameraInterceptActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.util.Calendar
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CameraTrackingManager @Inject constructor(
    context: Context,
    private val repo: RepositorySource) {

    var mContext: Context = context

    lateinit var cameraManager: CameraManager
    lateinit var cameraIds: Array<String>
    lateinit var cameraProviderFuture: ListenableFuture<ProcessCameraProvider>
    lateinit var stateManager: UsageStatsManager
    lateinit var packageManager: PackageManager
    lateinit var packageName: String
    lateinit var appName: String
    lateinit var cameraId: String
    var exceptCameraAppList: List<CameraAppData>? = null

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

                this@CameraTrackingManager.cameraId = cameraId
                try {
                    if (GlobalApplication.prefs!!.appAlim && !CameraInterceptActivity.isRunning) {
                        Handler(Looper.getMainLooper()).postDelayed({
                            getRecentlyCameraUserPackage()
                        }, 500)
                    }
                } catch (e: Exception) {
                    Log.i(GlobalApplication.TagName, "${e.message}")
                }
            }
        }, Handler(Looper.getMainLooper()))
    }

    fun getRecentlyCameraUserPackage() {
        var lastTime = 0L
        val cal = Calendar.getInstance()
        Calendar.getInstance().add(Calendar.SECOND, -1)

        val lastUsagePackageList = getLastUsagesPackages(cal)

        for (pkg in lastUsagePackageList) {
            if (checkPackage(pkg.packageName) && pkg.lastTimeUsed > lastTime) {
                this.packageName = pkg.packageName
                lastTime = pkg.lastTimeUsed
            }
        }

        if (packageName != mContext.packageName && ::packageName.isInitialized) {
            runBlocking(Dispatchers.IO) {
                exceptCameraAppList = repo.getExceptionCameraAppData()
            }

            exceptCameraAppList?.forEach {
                if (packageName == it.appPackageName) return
            }

            startCameraInterceptActivity()
        }
    }

    private fun getLastUsagesPackages(cal: Calendar) =
        stateManager.queryUsageStats(
        UsageStatsManager.INTERVAL_BEST,
        cal.timeInMillis,
        System.currentTimeMillis())

    private fun checkPackage(packageName: String) =
        !GlobalApplication.prefs!!.removeList.contains(packageName)
            && packageName != mContext.packageName
            && packageName.contains("com.android")
            && packageName.contains("com.samsung")
            && packageName.contains("com.google")

    private fun startCameraInterceptActivity() {
        CameraInterceptActivity.isRunning = true
        mContext.startActivity(
            Intent(mContext, CameraInterceptActivity::class.java).apply {
                putExtra(Const.PKG_NAME, packageName)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
        )
    }
}