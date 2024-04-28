package com.yunho.king.presentation.service

import android.content.Context
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraDevice
import android.hardware.camera2.CameraManager
import android.os.Handler
import android.os.Looper
import android.provider.Settings.Global
import android.util.Log
import com.yunho.king.GlobalApplication

class CameraService(context: Context): Thread() {

    var mContext: Context = context

    lateinit var cameraManager: CameraManager
    lateinit var cameraIds: Array<String>

    var canOpenCamra: Boolean = false

    override fun run() {
        super.run()
        Log.d(GlobalApplication.TagName, "Checking Service Start")
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

        while (true){
            Log.d(GlobalApplication.TagName, "Checking 10sec")
            Log.d(GlobalApplication.TagName, "Checking CanOpenCamera : ${canOpenCamra}")

            if(canOpenCamra){
                for(i in cameraIds){
                    Log.d(GlobalApplication.TagName, "Checking ids ${i} ")
                    try{
                        cameraManager.openCamera(i, object: CameraDevice.StateCallback(){
                            override fun onOpened(camera: CameraDevice) {
                                Log.d(GlobalApplication.TagName, "Checking onpend : ${canOpenCamra}")
                            }

                            override fun onDisconnected(camera: CameraDevice) {
                                Log.d(GlobalApplication.TagName, "Checking disconnect : ${canOpenCamra}")
                            }

                            override fun onError(camera: CameraDevice, error: Int) {
                                Log.d(GlobalApplication.TagName, "Checking Error : ${canOpenCamra}")
                            }
                        }, Handler(Looper.getMainLooper()))
                    } catch (e: CameraAccessException){
                        Log.d(GlobalApplication.TagName, "Checking exception : \n ${e.reason}" +
                                "\n ${e.cause}" +
                                "\n ${e.message}")
                    }
                }
            }
            sleep(10000)
        }
    }

    fun getInfo(e: CameraAccessException){

    }
}