package com.yunho.king.presentation.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import com.yunho.king.Const
import com.yunho.king.GlobalApplication
import com.yunho.king.R
import com.yunho.king.domain.di.RepositorySource
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainService: LifecycleService() {

    @Inject lateinit var repo: RepositorySource
    lateinit var cameraService: CameraTrackingManager
    lateinit var channel: NotificationChannel

    override fun onCreate() {
        super.onCreate()
        Log.d(GlobalApplication.TagName, "Checking Service Create")

        showForegroundService()

        Log.d(GlobalApplication.TagName, "Checking Service StartCommand")

        cameraService = CameraTrackingManager(this, repo)
        cameraService.setCameraTracker()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        return START_STICKY
    }

    fun showForegroundService(){
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
        builder.setContentTitle(StringBuilder(resources.getString(R.string.app_name))
            .append(getString(R.string.service_is_running)).toString())
            .setTicker(StringBuilder(resources.getString(R.string.app_name)).append("service is running").toString())
            .setContentText("") //                    , swipe down for more options.
            .setSmallIcon(R.mipmap.app_icon)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setWhen(0)
            .setOnlyAlertOnce(true)
            .setOngoing(true)

        startForeground(123, builder.build())
    }
}