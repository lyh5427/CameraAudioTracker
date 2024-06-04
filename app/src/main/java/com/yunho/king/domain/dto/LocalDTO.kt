package com.yunho.king.domain.dto

import android.graphics.drawable.Drawable


data class AppList(
    val appPackageName: String = "",
    val appIcon: Drawable,
    var appName: String = "",
    var permUseCount: Int = 0,
    var lastUseDateTime: Long = 0
)

data class State(
    val status: String,
    val msg: String = ""
)