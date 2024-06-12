package com.yunho.king.presentation.ui.cameraintercept

import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.hardware.camera2.CameraManager
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.common.util.concurrent.ListenableFuture
import com.yunho.king.Const
import com.yunho.king.GlobalApplication
import com.yunho.king.R
import com.yunho.king.Utils.singleClickListener
import com.yunho.king.Utils.toGone
import com.yunho.king.databinding.ActivityCameraInterceptBinding
import com.yunho.king.databinding.PopupSuspicionBinding
import com.yunho.king.domain.dto.CameraAppData
import com.yunho.king.presentation.service.MainService
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class CameraInterceptActivity : AppCompatActivity() {

    lateinit var binding: ActivityCameraInterceptBinding
    val viewModel: CameraInterceptViewModel by viewModels()

    lateinit var cameraManager: CameraManager
    lateinit var cameraIds: Array<String>
    lateinit var cameraProviderFuture: ListenableFuture<ProcessCameraProvider>
    lateinit var cameraProvider: ProcessCameraProvider
    lateinit var stateManager: UsageStatsManager
    lateinit var packageName: String
    lateinit var appName: String
    lateinit var appIcon: Drawable
    lateinit var appInfo: ApplicationInfo


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCameraInterceptBinding.inflate(layoutInflater)

        cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        viewModel.packageName = intent.getStringExtra(Const.PKG_NAME)?: ""
        viewModel.getCameraAppData()
        viewModel.setAppInfo(packageManager)

        cameraIds = cameraManager.cameraIdList
        setListener()
        setContentView(binding.root)

    }

    override fun onResume() {
        super.onResume()
        openCamera2()
    }

    fun setListener() = with(binding) {
        cameraAlimCheckBox.singleClickListener {
            cameraAlimCheckBox.isChecked = !cameraAlimCheckBox.isChecked
        }

        appAlimCheckBox.singleClickListener {
            appAlimCheckBox.isChecked = !appAlimCheckBox.isChecked
        }

        cancel.singleClickListener {
//            setAlimOption(cameraAlimCheckBox.isChecked)
//            setAppAlim(appAlimCheckBox.isChecked)
//            closeCamera()
            root.toGone()
        }

        btnPopupOk.singleClickListener {
            startActivity(
                Intent(
                    android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                    android.net.Uri.parse("package:${packageName}"))
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

    private fun openCamera2() {
        try {
            cameraProviderFuture.addListener(
                {
                    cameraProvider = cameraProviderFuture.get()
                    val preview = Preview.Builder()
                        .build()

                    preview.setSurfaceProvider(binding.cameraPreview.surfaceProvider)

                    val cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA

                    try {
                        cameraProvider.unbindAll()
                        cameraProvider.bindToLifecycle(this, cameraSelector, preview)
                    } catch (e: Exception) {

                    }
                },
                ContextCompat.getMainExecutor(this))
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