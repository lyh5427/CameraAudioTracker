package com.yunho.king.feature.intercept.camera

import android.graphics.drawable.Drawable

object CameraInterceptContract {
    data class State(
        val packageName: String = "",
        val appName: String = "",
        val appIcon: Drawable? = null
    )

    sealed interface Intent {
        data class SetPackageName(val pkg: String) : Intent
        data object Dismiss : Intent
        data object OpenAppSettings : Intent
        data class SetAlim(val cameraAlim: Boolean, val appAlim: Boolean) : Intent
    }
}
