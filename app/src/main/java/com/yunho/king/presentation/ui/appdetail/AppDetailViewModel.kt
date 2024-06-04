package com.yunho.king.presentation.ui.appdetail

import android.content.pm.PackageManager
import androidx.lifecycle.viewModelScope
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
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AppDetailViewModel@Inject constructor(
    private val repo: RepositorySource
): BaseViewModel(repo) {
    private var _appName: MutableSharedFlow<State> =
        MutableSharedFlow(0, 1, BufferOverflow.DROP_OLDEST)
    val appName = _appName.asSharedFlow()

    private var _downLoad: MutableSharedFlow<State> =
        MutableSharedFlow(0, 1, BufferOverflow.DROP_OLDEST)
    val downLoad = _downLoad.asSharedFlow()

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
    val audioCount = _audioCount.asSharedFlow()

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

    private fun setCameraView() {
        viewModelScope.launch {
            _appName.emit(State(Status.TEXT, cameraData!!.appName))
            _downLoad.emit(State(Status.TEXT, cameraData!!.appPackageName))
            _cameraState.emit(State(Status.TEXT, "${cameraData!!.permState}"))
            _cameraCount.emit(State(Status.TEXT, "${cameraData!!.permUseCount}"))
            _cameraLastUseDate.emit(State(Status.TEXT, "${cameraData!!.lastUseDateTime}"))
        }
    }

    private fun setAudioView() {
        viewModelScope.launch {
            _appName.emit(State(Status.TEXT, audioAppData!!.appName))
            _downLoad.emit(State(Status.TEXT, audioAppData!!.appPackageName))
            _audioState.emit(State(Status.TEXT, "${audioAppData!!.permState}"))
            _audioCount.emit(State(Status.TEXT, "${audioAppData!!.permUseCount}"))
            _audioLastUseDate.emit(State(Status.TEXT, "${audioAppData!!.lastUseDateTime}"))
        }
    }
}