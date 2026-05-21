package com.yunho.king.feature.appdetail

import com.yunho.king.core.model.AudioAppData
import com.yunho.king.core.model.CameraAppData

object AppDetailContract {
    data class State(
        val appName: String = "",
        val packageName: String = "",
        val cameraData: CameraAppData? = null,
        val audioData: AudioAppData? = null,
        val isLoading: Boolean = false
    )

    sealed interface Intent {
        data class Load(val pkgName: String) : Intent
    }
}
