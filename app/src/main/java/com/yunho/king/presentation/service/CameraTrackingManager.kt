package com.yunho.king.presentation.service

import android.app.Service
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.PixelFormat
import android.graphics.drawable.Drawable
import android.hardware.camera2.CameraManager
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.WindowManager
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
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
    lateinit var popupSuspicionBinding: PopupSuspicionBinding
    lateinit var cameraProviderFuture: ListenableFuture<ProcessCameraProvider>
    lateinit var cameraProvider: ProcessCameraProvider
    lateinit var stateManager: UsageStatsManager
    lateinit var packageManager: PackageManager
    lateinit var packageName: String
    lateinit var appName: String
    lateinit var appIcon: Drawable
    lateinit var appInfo: ApplicationInfo
    lateinit var cameraId: String

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
                if (GlobalApplication.prefs!!.appAlim) {
                    getRecentlyCameraUserPackage()
                    setAppInfo()
                }
            }
        }, Handler(Looper.getMainLooper()))
    }

    fun getRecentlyCameraUserPackage() {
        val cal = Calendar.getInstance()
        cal.add(Calendar.SECOND, -1)

        val lastUsagePackageList = stateManager.queryUsageStats(
            UsageStatsManager.INTERVAL_BEST, cal.timeInMillis, System.currentTimeMillis())
        var lastTime = 0L
        for (pkg in lastUsagePackageList) {
            if (pkg.lastTimeUsed > lastTime) {
                Log.d(GlobalApplication.TagName, "Camera ${pkg.lastTimeUsed}  ${pkg.packageName}")
                lastTime = pkg.lastTimeUsed
                this.packageName = pkg.packageName
            }
        }

        if (packageName != mContext.packageName) {
            mContext.startActivity(
                Intent(mContext, CameraInterceptActivity::class.java).apply {
                    putExtra(Const.PKG_NAME, packageName)
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
            )
        }
    }

    private fun setAppInfo() {
        CoroutineScope(Dispatchers.IO).launch {
            appInfo = packageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA)

            getAppName()
            getAppIcon()

            repo.getCameraAppData(packageName)

            val appData = repo.getCameraAppData(packageName)

            withContext(Dispatchers.Main) {
                try {
                    if (appData.notiFlag) {
                        makeSuspicionPopup()
                        updateAppData(appData)
                    }
                } catch (e: Exception) {
                    Log.d(GlobalApplication.TagName, e.message?: "")
                }
            }
        }
    }

    fun getAppName() {
        appName = try {
            packageManager.getApplicationLabel(appInfo).toString()
        } catch (e: Exception) {
            appInfo.name
        }
    }

    fun getAppIcon() {
        appIcon = packageManager.getApplicationIcon(appInfo)
    }

    fun makeSuspicionPopup() {
        if (!::popupSuspicionBinding.isInitialized) {
            popupSuspicionBinding = PopupSuspicionBinding.inflate(
                LayoutInflater.from(mContext),
                null,
                false)

            setListener()

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

        setSuspicionPopup()
    }
    fun setSuspicionPopup() = with(popupSuspicionBinding) {
        appImg.setImageDrawable(appIcon)
        appName.text = this@CameraTrackingManager.appName

        root.toVisible()
        openCamera2()
    }

    fun setListener() = with(popupSuspicionBinding) {
        cameraAlimCheckBox.singleClickListener {
            cameraAlimCheckBox.isChecked = !cameraAlimCheckBox.isChecked
        }

        appAlimCheckBox.singleClickListener {
            appAlimCheckBox.isChecked = !appAlimCheckBox.isChecked
        }

        cancel.singleClickListener {
            setAlimOption(cameraAlimCheckBox.isChecked)
            setAppAlim(appAlimCheckBox.isChecked)
            closeCamera()
            root.toGone()

        }

        btnPopupOk.singleClickListener {
            mContext.startActivity(
                Intent(
                    Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                    Uri.parse("package:${packageName}"))
            )
        }
    }

    private fun setAlimOption(isChecked: Boolean) {
        if (isChecked) {
            CoroutineScope(Dispatchers.IO).launch {
                repo.updateNotiFlag(packageName, false)
            }
        }
    }

    private fun setAppAlim(isChecked: Boolean) {
        GlobalApplication.prefs!!.appAlim = !isChecked
    }

    private fun openCamera2() {
        try {
            cameraProviderFuture.addListener(
                {
                    cameraProvider = cameraProviderFuture.get()
                    val preview = Preview.Builder()
                        .build()

                    preview.setSurfaceProvider(popupSuspicionBinding.cameraPreview.surfaceProvider)

                    val cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA

                    try {
                        cameraProvider.unbindAll()
                        cameraProvider.bindToLifecycle((mContext as MainService), cameraSelector, preview)
                    } catch (e: Exception) {

                    }
                },
                ContextCompat.getMainExecutor(mContext))
        } catch (e: Exception) {
            Log.d(GlobalApplication.TagName, "aaa ${e.message}")
        }
    }

    private fun closeCamera() {
        try {
            cameraProvider.unbindAll()
        } catch (e: Exception) {
            Log.d(GlobalApplication.TagName, "${e.message}")
        }
    }

    private fun updateAppData(data: CameraAppData) {
        CoroutineScope(Dispatchers.IO).launch {
            repo.updateCameraAppPermUseCount(packageName, data.permUseCount + 1)
            repo.updateLastUseDate(packageName, System.currentTimeMillis())
        }
    }
}