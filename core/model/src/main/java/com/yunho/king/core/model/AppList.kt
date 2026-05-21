package com.yunho.king.core.model

import android.graphics.drawable.Drawable

data class AppList(
    val appPackageName: String = "",
    val appIcon: Drawable,
    var appName: String = "",
    var permUseCount: Int = 0,
    var lastUseDateTime: Long = 0
)
