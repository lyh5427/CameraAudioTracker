package com.yunho.king.presentation.service

import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraDevice
import android.hardware.camera2.CameraManager
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
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

class CameraTrackingManager(context: Context) {

    var mContext: Context = context

    lateinit var cameraManager: CameraManager
    lateinit var cameraIds: Array<String>
    lateinit var popupSuspicionBinding: PopupSuspicionBinding
    lateinit var cameraProviderFuture: ListenableFuture<ProcessCameraProvider!>
    lateinit var cameraProvider: ProcessCameraProvider

    var canOpenCamra: Boolean = false

    fun setCameraTracker() {
        cameraProviderFuture = ProcessCameraProvider.getInstance(mContext)
        cameraManager = mContext.getSystemService(Context.CAMERA_SERVICE) as CameraManager
        cameraIds = cameraManager.cameraIdList

        cameraManager.registerAvailabilityCallback(object: CameraManager.AvailabilityCallback() {
            override fun onCameraAccessPrioritiesChanged() {
                super.onCameraAccessPrioritiesChanged()
            }

            override fun onCameraAvailable(cameraId: String) {
                super.onCameraAvailable(cameraId)
                Log.d(GlobalApplication.TagName, "Checking onCameraAvailable : ${canOpenCamra}")
                canOpenCamra = true
            }

            override fun onCameraUnavailable(cameraId: String) {
                super.onCameraUnavailable(cameraId)
                Log.d(GlobalApplication.TagName, "Checking onCameraUnavailable : ${canOpenCamra}")
                canOpenCamra = false
            }
        }, Handler(Looper.getMainLooper()))
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