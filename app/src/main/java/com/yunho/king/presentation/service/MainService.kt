package com.yunho.king.presentation.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import com.yunho.king.core.common.Const
import com.yunho.king.GlobalApplication
import com.yunho.king.R
import com.yunho.king.core.designsystem.R as DesignR
import com.yunho.king.domain.repository.RepositorySource
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainService: LifecycleService() {

    @Inject lateinit var repo: RepositorySource

    private lateinit var cameraService: CameraTrackingManager
    private lateinit var audioService: AudioTrackingManager
    private lateinit var channel: NotificationChannel

    override fun onCreate() {
        super.onCreate()
        Log.d(GlobalApplication.TagName, "Checking Service Create")

        showForegroundService()

        Log.d(GlobalApplication.TagName, "Checking Service StartCommand")

        cameraService = CameraTrackingManager(this, repo)
        cameraService.setCameraTracker()

        audioService = AudioTrackingManager(this, repo)
        audioService.setAudioTracker()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        return START_STICKY
    }

    override fun onDestroy() {
        if (::cameraService.isInitialized) {
            cameraService.clearCameraTracker()
        }
        if (::audioService.isInitialized) {
            audioService.clearAudioTracker()
        }
        super.onDestroy()
    }

    fun showForegroundService() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val manager = getSystemService(NotificationManager::class.java)
            channel = NotificationChannel(
                Const.CAMERA_CHANNEL_ID,
                Const.CAMERA_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_NONE
            )
            channel.setShowBadge(false)
            channel.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
            manager.createNotificationChannel(channel)
        }

        val builder = NotificationCompat.Builder(this, Const.CAMERA_CHANNEL_ID)
        builder.setContentTitle(StringBuilder(resources.getString(DesignR.string.app_name))
            .append(getString(DesignR.string.service_is_running)).toString())
            .setTicker(
                StringBuilder(resources.getString(DesignR.string.app_name))
                    .append("service is running")
                    .toString()
            )
            .setContentText("")
            .setSmallIcon(R.mipmap.app_icon)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setWhen(0)
            .setOnlyAlertOnce(true)
            .setOngoing(true)

        val notification = builder.build()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            startForeground(
                123,
                notification,
                ServiceInfo.FOREGROUND_SERVICE_TYPE_CAMERA or ServiceInfo.FOREGROUND_SERVICE_TYPE_MICROPHONE
            )
        } else {
            startForeground(123, notification)
        }
    }
}