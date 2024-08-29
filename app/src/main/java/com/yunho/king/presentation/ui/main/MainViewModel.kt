package com.yunho.king.presentation.ui.main

import android.provider.Settings.Global
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.yunho.king.GlobalApplication
import com.yunho.king.domain.di.RepositorySource
import com.yunho.king.domain.dto.AudioAppData
import com.yunho.king.domain.dto.CameraAppData
import com.yunho.king.presentation.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repo: RepositorySource
): BaseViewModel(repo) {

    private var _cameraList: MutableSharedFlow<List<CameraAppData>?> =
        MutableSharedFlow(0, 1, BufferOverflow.DROP_OLDEST)
    val cameraList = _cameraList.asSharedFlow()

    private var _audioList: MutableSharedFlow<List<AudioAppData>?> =
        MutableSharedFlow(0, 1, BufferOverflow.DROP_OLDEST)
    val audioList = _audioList.asSharedFlow()


    suspend fun getCameraData() {
        Log.d(GlobalApplication.TagName, "Search Camera")
        _cameraList.emit(repo.getAllCameraAppList())
    }

    suspend fun getAudioData() {
        Log.d(GlobalApplication.TagName, "Search Audio")
        _audioList.emit(repo.getAllAudioAppList())
    }

    fun getExceptionCameraApp() {
        viewModelScope.launch {
            _cameraList.emit(repo.getExceptionCameraAppData())
        }
    }

    fun getExceptionAudioApp() {
        viewModelScope.launch {
            _audioList.emit(repo.getExceptionAudioAppData())
        }
    }

}