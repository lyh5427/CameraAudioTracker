package com.yunho.king.core.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CameraAppData(
    @PrimaryKey
    val appPackageName: String = "",
    var appName: String = "",
    var permUseCount: Int = 0,
    var notiFlag: Boolean = true,
    var lastUseDateTime: Long = 0,
    var exceptionDate: Long = 0,
    var permState: Boolean
)
