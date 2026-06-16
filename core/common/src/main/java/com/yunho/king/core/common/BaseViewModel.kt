package com.yunho.king.core.common

import androidx.lifecycle.ViewModel
import com.yunho.king.core.model.AudioAppData
import com.yunho.king.core.model.CameraAppData
import com.yunho.king.domain.repository.RepositorySource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
open class BaseViewModel @Inject constructor(
    protected val repo: RepositorySource
) : ViewModel() {

    fun getCameraUseCount(pkgName: String): Int = repo.getCameraAppPermUseCount(pkgName)
    suspend fun insertCameraApp(app: CameraAppData) = repo.insertCameraApp(app)
    fun deleteCameraApp(pkgName: String) = repo.deleteCameraApp(pkgName)
    suspend fun isExistCameraApp(pkgName: String): Boolean = repo.existCameraApp(pkgName)

    fun getAudioUseCount(pkgName: String): Int = repo.getAudioAppPermUseCount(pkgName)
    suspend fun insertAudioApp(app: AudioAppData) = repo.insertAudioApp(app)
    fun deleteAudioApp(pkgName: String) = repo.deleteAudioApp(pkgName)
    suspend fun isExistAudioApp(pkgName: String): Boolean = repo.existAudioApp(pkgName)
}
