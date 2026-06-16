package com.yunho.king.core.model

import android.graphics.drawable.Drawable

data class ExAppList(
    val appPackageName: String = "",
    val appIcon: Drawable,
    var appName: String = "",
    var permUseCount: Int = 0,
    var lastUseDateTime: Long = 0,
    var exceptionDate: Long = 0
)
