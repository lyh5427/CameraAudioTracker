package com.yunho.king.presentation

import android.content.Context
import android.content.Intent
import com.yunho.king.core.common.Const
import com.yunho.king.feature.navigator.MainActivity

/**
 * CameraTrackingManager / AudioTrackingManager에서 인터셉트 화면을 띄울 때 사용.
 * 기존: startActivity(Intent(context, CameraInterceptActivity::class.java))
 * 변경: InterceptNavigation.startCameraIntercept(context, pkgName)
 */
object InterceptNavigation {

    fun startCameraIntercept(context: Context, pkgName: String) {
        context.startActivity(createInterceptIntent(context, "camera_intercept", pkgName))
    }

    fun startAudioIntercept(context: Context, pkgName: String) {
        context.startActivity(createInterceptIntent(context, "audio_intercept", pkgName))
    }

    private fun createInterceptIntent(context: Context, screen: String, pkgName: String): Intent {
        return Intent(context, MainActivity::class.java).apply {
            putExtra("SCREEN", screen)
            putExtra(Const.PKG_NAME, pkgName)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP)
        }
    }
}
