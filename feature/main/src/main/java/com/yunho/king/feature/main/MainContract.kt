package com.yunho.king.feature.main

import com.yunho.king.core.model.AudioAppData
import com.yunho.king.core.model.CameraAppData

object MainContract {
    const val PAGE_SIZE = 10

    data class State(
        val selectedMainTab: MainTab = MainTab.Usage,
        val selectedUsageTab: UsageTab = UsageTab.Camera,
        val cameraUsageList: List<CameraAppData> = emptyList(),
        val audioUsageList: List<AudioAppData> = emptyList(),
        val cameraUsagePageCount: Int = 0,
        val audioUsagePageCount: Int = 0,
        val currentCameraUsagePage: Int = 1,
        val currentAudioUsagePage: Int = 1,
        val exCameraList: List<CameraAppData> = emptyList(),
        val exAudioList: List<AudioAppData> = emptyList(),
        val isLoading: Boolean = false
    ) {
        val cameraUsageFiltered: List<CameraAppData> get() = cameraUsageList.filter { it.notiFlag }
        val audioUsageFiltered: List<AudioAppData> get() = audioUsageList.filter { it.notiFlag }
    }

    enum class MainTab { Usage, Except }

    enum class UsageTab { Camera, Audio }

    sealed interface Intent {
        data object LoadUsageData : Intent
        data object LoadExceptionData : Intent
        data class SelectMainTab(val tab: MainTab) : Intent
        data class SelectUsageTab(val tab: UsageTab) : Intent
        data class LoadCameraPage(val page: Int) : Intent
        data class LoadAudioPage(val page: Int) : Intent
        data class NavigateToDetail(val pkgName: String) : Intent
        data class RemoveFromException(val pkgName: String, val isCamera: Boolean) : Intent
    }

    sealed interface Effect {
        data class NavigateToAppDetail(val pkgName: String) : Effect
    }
}
