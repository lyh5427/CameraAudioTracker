package com.yunho.king.domain.dto

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

@Entity
data class AudioAppData(
    @PrimaryKey
    var appPackageName: String = "",
    var appName: String = "",
    var permUseCount: Int = 0,
    var notiFlag: Boolean = true,
    var lastUseDateTime: Long = 0,
    var exceptionDate: Long = 0,
    var permState: Boolean
)

data class AppData(
    var appPackageName: String = "",
    var appIcon: Int = 0,
    var isCamera: Boolean = false,
    var isAudio: Boolean = false
)