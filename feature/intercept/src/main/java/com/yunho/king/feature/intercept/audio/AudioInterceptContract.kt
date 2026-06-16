package com.yunho.king.feature.intercept.audio

import android.graphics.drawable.Drawable

object AudioInterceptContract {
    data class State(
        val packageName: String = "",
        val appName: String = "",
        val appIcon: Drawable? = null
    )

    sealed interface Intent {
        data class SetPackageName(val pkg: String) : Intent
        data object Dismiss : Intent
        data object OpenAppSettings : Intent
        data class SetAlim(val audioAlim: Boolean, val appAlim: Boolean) : Intent
        data class SetAppInfo(val appName: String, val appIcon: Drawable?) : Intent
    }

    sealed interface Effect
}
