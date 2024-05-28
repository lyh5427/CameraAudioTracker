package com.yunho.king.presentation.ui.base

import androidx.lifecycle.ViewModel
import com.yunho.king.domain.di.RepositorySource
import com.yunho.king.domain.dto.AudioAppData
import com.yunho.king.domain.dto.CameraAppData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
open class BaseViewModel @Inject constructor(
    private val repo: RepositorySource
): ViewModel() {
    fun getCameraUseCount(pkgName: String): Int = repo.getCameraAppPermUseCount(pkgName)

    fun insertCameraApp(app: CameraAppData) = repo.insertCameraApp(app)

    fun deleteCameraApp(pkgName: String) = repo.deleteCameraApp(pkgName)

    fun isExistCameraApp(pkgName: String): Boolean = repo.existCameraApp(pkgName)

    fun getAudioUseCount(pkgName: String): Int = repo.getAudioAppPermUseCount(pkgName)

    fun insertAudioApp(app: AudioAppData) = repo.insertAudioApp(app)

    fun deleteAudioApp(pkgName: String) = repo.deleteAudioApp(pkgName)

    fun isExistAudioApp(pkgName: String): Boolean = repo.existAudioApp(pkgName)
}