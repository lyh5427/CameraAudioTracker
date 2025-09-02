package com.yunho.king.presentation.ui.main

import com.yunho.king.domain.di.RepositorySource
import com.yunho.king.domain.dto.AudioAppData
import com.yunho.king.domain.dto.CameraAppData
import com.yunho.king.presentation.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repo: RepositorySource
): BaseViewModel(repo) {

    //AppListFragment
    private var _cameraList: MutableSharedFlow<List<CameraAppData>> =
        MutableSharedFlow(1, 1, BufferOverflow.DROP_OLDEST)
    val cameraList = _cameraList.asSharedFlow()

    private var _audioList: MutableSharedFlow<List<AudioAppData>> =
        MutableSharedFlow(1, 1, BufferOverflow.DROP_OLDEST)
    val audioList = _audioList.asSharedFlow()

    //ExAppListFragment
    private var _exCameraList: MutableSharedFlow<List<CameraAppData>?> =
        MutableSharedFlow(1, 1, BufferOverflow.DROP_OLDEST)
    val exCameraList = _exCameraList.asSharedFlow()

    private var _exAudioList: MutableSharedFlow<List<AudioAppData>?> =
        MutableSharedFlow(1, 1, BufferOverflow.DROP_OLDEST)
    val exAudioList = _exAudioList.asSharedFlow()

    /** 카메라 앱 페이지 어댑터 적용 */
    private var _cameraPageAdapter: MutableSharedFlow<Int> =
        MutableSharedFlow(1, 1, BufferOverflow.SUSPEND)
    val cameraPageAdapter = _cameraPageAdapter.asSharedFlow()

    private var _audioPageAdapter: MutableSharedFlow<Int> =
        MutableSharedFlow(1, 1, BufferOverflow.SUSPEND)
    val audioPageAdapter = _audioPageAdapter.asSharedFlow()

    var cameraAppList: List<CameraAppData>? = null
    var audioAppList: List<AudioAppData>? = null

    //AppListFragment
    suspend fun getCameraData(page: Int) {
        if (cameraAppList == null) {
            runBlocking {
                cameraAppList = repo.getAllCameraAppList()
            }
            getCameraPageCount()
        }

        val startIndex = ((page - 1) * 10)
        val endIndex = (page * 10)

        _cameraList.emit(cameraAppList!!.subList(startIndex, endIndex))
    }

    /**
     * 카메라 앱 전체 페이지 인텍스 반환
     * */
    suspend fun getCameraPageCount() {
        _cameraPageAdapter.emit((cameraAppList?.count()?: 0)/10)
    }

    suspend fun getAudioData(page: Int) {
        if (audioAppList == null) {
            runBlocking {
                audioAppList = repo.getAllAudioAppList()
            }
            getAudioPageCount()
        }

        val startIndex = ((page - 1) * 10)
        val endIndex = (page * 10)

        _audioList.emit(audioAppList!!.subList(startIndex, endIndex))
    }

    /**
     * 오디오 앱 전체 페이지 인텍스 반환
     * */
    suspend fun getAudioPageCount() {
        _audioPageAdapter.emit((audioAppList?.count()?: 0)/10)
    }

    //ExAppListFragment
    suspend fun getExceptionCameraApp() {
        _exCameraList.emit(repo.getExceptionCameraAppData())
    }

    suspend fun getExceptionAudioApp() {
        _exAudioList.emit(repo.getExceptionAudioAppData())
    }

    suspend fun updateCameraAppFlag(pkgName: String, flag: Boolean) {
        repo.updateCameraNotiFlag(pkgName, flag, System.currentTimeMillis())
    }

    suspend fun updateAudioAppFlag(pkgName: String, flag: Boolean) {
        repo.updateAudioNotiFlag(pkgName, flag, System.currentTimeMillis())
    }
}