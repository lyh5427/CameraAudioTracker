package com.yunho.king.presentation.ui.appdetail

import com.yunho.king.Status
import com.yunho.king.domain.di.RepositorySource
import com.yunho.king.domain.dto.AudioAppData
import com.yunho.king.domain.dto.CameraAppData
import com.yunho.king.domain.dto.State
import com.yunho.king.presentation.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject

@HiltViewModel
class AppDetailViewModel@Inject constructor(
    private val repo: RepositorySource
): BaseViewModel(repo) {
    private var _cameraState: MutableSharedFlow<State> =
        MutableSharedFlow(0, 1, BufferOverflow.DROP_OLDEST)
    val cameraState = _cameraState.asSharedFlow()

    private var _cameraCount: MutableSharedFlow<State> =
        MutableSharedFlow(0, 1, BufferOverflow.DROP_OLDEST)
    val cameraCount = _cameraCount.asSharedFlow()

    private var _cameraLastUseDate: MutableSharedFlow<State> =
        MutableSharedFlow(0, 1, BufferOverflow.DROP_OLDEST)
    val cameraLastUseDate = _cameraLastUseDate.asSharedFlow()

    private var _audioState: MutableSharedFlow<State> =
        MutableSharedFlow(0, 1, BufferOverflow.DROP_OLDEST)
    val audioState = _audioState.asSharedFlow()

    private var _audioCount: MutableSharedFlow<State> =
        MutableSharedFlow(0, 1, BufferOverflow.DROP_OLDEST)
    val audioCountTitle = _audioCount.asSharedFlow()

    private var _audioLastUseDate: MutableSharedFlow<State> =
        MutableSharedFlow(0, 1, BufferOverflow.DROP_OLDEST)
    val audioLastUseDate = _audioLastUseDate.asSharedFlow()

    var cameraData: CameraAppData? = null
    var audioAppData: AudioAppData? = null

    fun getCameraAppData(pkgName: String) {
        cameraData = repo.getCameraAppData(pkgName)
        audioAppData = repo.getAudioAppData(pkgName)

        if (cameraData != null) setCameraView()
        if (audioAppData != null) setAudioView()
    }

    fun setCameraView() {
        cameraData.



    }

    fun setAudioView() {

    }
}