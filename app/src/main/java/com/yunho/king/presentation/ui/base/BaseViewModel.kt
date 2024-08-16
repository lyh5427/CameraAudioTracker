package com.yunho.king.presentation.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yunho.king.domain.di.RepositorySource
import com.yunho.king.domain.dto.AudioAppData
import com.yunho.king.domain.dto.CameraAppData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
open class BaseViewModel @Inject constructor(
    private val repo: RepositorySource
): ViewModel() {
    fun getCameraUseCount(pkgName: String): Int = repo.getCameraAppPermUseCount(pkgName)

    suspend fun insertCameraApp(app: CameraAppData) = repo.insertCameraApp(app)

    fun deleteCameraApp(pkgName: String) = repo.deleteCameraApp(pkgName)

    suspend fun isExistCameraApp(pkgName: String): Boolean = repo.existCameraApp(pkgName)

    fun getAudioUseCount(pkgName: String): Int = repo.getAudioAppPermUseCount(pkgName)

    suspend fun insertAudioApp(app: AudioAppData) = repo.insertAudioApp(app)

    fun deleteAudioApp(pkgName: String) = repo.deleteAudioApp(pkgName)

    suspend fun isExistAudioApp(pkgName: String): Boolean = repo.existAudioApp(pkgName)
}