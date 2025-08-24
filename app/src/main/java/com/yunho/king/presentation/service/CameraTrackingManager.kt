package com.yunho.king.presentation.service

import android.app.usage.UsageStats
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.camera2.CameraManager
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.camera.lifecycle.ProcessCameraProvider
import com.google.common.util.concurrent.ListenableFuture
import com.yunho.king.presentation.constant.Const
import com.yunho.king.GlobalApplication
import com.yunho.king.domain.di.RepositorySource
import com.yunho.king.domain.dto.CameraAppData
import com.yunho.king.presentation.ui.cameraintercept.CameraInterceptActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import java.util.Calendar
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CameraTrackingManager @Inject constructor(
    context: Context,
    private val repo: RepositorySource
) {
    private var mContext: Context = context

    private lateinit var cameraManager: CameraManager
    private lateinit var cameraIds: Array<String>
    private lateinit var stateManager: UsageStatsManager
    private lateinit var packageManager: PackageManager
    private var exceptCameraAppList: List<CameraAppData>? = null
    private var packageName: String = ""

    /**
     * 카메라 상태 콜백 등록
     * */
    fun setCameraTracker() {
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

                try {
                    if (GlobalApplication.prefs!!.appAlim && !CameraInterceptActivity.isRunning) {
                        getRecentlyRunningPackage()
                    }
                } catch (e: Exception) {
                    e.stackTrace
                    Log.i(GlobalApplication.TagName, "${e.message}")
                }
            }
        }, Handler(Looper.getMainLooper()))
    }

    /**
     * 최근 실행된 패키지 추출
     * */
    fun getRecentlyRunningPackage() {
        var lastPackageRunTime: Long = 0L
        var beforeTheLastPackageRunTime: Long = 0L
        var lastForegroundApp: String? = null
        var beforeTheLastApp: String? = null

        runBlocking(Dispatchers.IO) {
            exceptCameraAppList = repo.getExceptionCameraAppData()
        }

        for (pkg in getLastUsagesPackages()) {
            if (checkPackage(pkg.packageName) && pkg.lastTimeUsed > lastPackageRunTime) {
                if (lastForegroundApp == null) {
                    lastForegroundApp = pkg.packageName
                    lastPackageRunTime = pkg.lastTimeUsed
                } else {
                    beforeTheLastApp = lastForegroundApp
                    lastForegroundApp = pkg.packageName

                    beforeTheLastPackageRunTime = lastPackageRunTime
                    lastPackageRunTime = pkg.lastTimeUsed
                }
            }
        }

        val timeDeffer = (lastPackageRunTime-beforeTheLastPackageRunTime)/1000

        packageName = if (timeDeffer < 1) {
            beforeTheLastApp?: ""
        } else {
            lastForegroundApp?: ""
        }

        exceptCameraAppList?.forEach {
            if (packageName == it.appPackageName) {
                packageName = ""
                return
            }
        }

        if (packageName != "") {
            startCameraInterceptActivity()
        }
    }

    /**
     * 최근 앱 실행 목록 반환
     * */
    private fun getLastUsagesPackages(): List<UsageStats>{
        val cal = Calendar.getInstance()
        cal.add(Calendar.MINUTE, -30)

        return stateManager.queryUsageStats(
            UsageStatsManager.INTERVAL_BEST,
            cal.timeInMillis,
            System.currentTimeMillis())
    }

    /**
     * 추출된 패키지에서 제거할 목록 여부 확인
     * */
    private fun checkPackage(packageName: String) =
        !GlobalApplication.prefs!!.removeList.contains(packageName)
                && packageName != mContext.packageName
                && !packageName.contains("com.android")
                && !packageName.contains("com.samsung")
                && !packageName.contains("com.google")

    /**
     * 카메라 탈취 액티비티 실행
     * */
    private fun startCameraInterceptActivity() {
        CameraInterceptActivity.isRunning = true

        mContext.startActivity(
            Intent(mContext, CameraInterceptActivity::class.java).apply {
                putExtra(Const.PKG_NAME, packageName)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            }
        )

        packageName = ""
    }
}