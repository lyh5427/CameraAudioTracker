package com.yunho.king.feature.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yunho.king.core.model.AudioAppData
import com.yunho.king.core.model.CameraAppData
import com.yunho.king.domain.repository.RepositorySource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repo: RepositorySource
) : ViewModel() {

    private val _state = MutableStateFlow(MainContract.State())
    val state: StateFlow<MainContract.State> = _state.asStateFlow()

    private val _sideEffects = Channel<MainContract.Effect>(Channel.BUFFERED)
    val sideEffects = _sideEffects.receiveAsFlow()

    fun onIntent(intent: MainContract.Intent) {
        viewModelScope.launch {
            when (intent) {
                is MainContract.Intent.LoadUsageData -> loadUsageData()
                is MainContract.Intent.LoadExceptionData -> loadExceptionData()
                is MainContract.Intent.SelectMainTab -> _state.update {
                    it.copy(selectedMainTab = intent.tab)
                }
                is MainContract.Intent.SelectUsageTab -> _state.update {
                    it.copy(selectedUsageTab = intent.tab)
                }
                is MainContract.Intent.LoadCameraPage -> loadCameraPage(intent.page)
                is MainContract.Intent.LoadAudioPage -> loadAudioPage(intent.page)
                is MainContract.Intent.NavigateToDetail -> {
                    _sideEffects.send(MainContract.Effect.NavigateToAppDetail(intent.pkgName))
                }
                is MainContract.Intent.RemoveFromException -> removeFromException(intent.pkgName, intent.isCamera)
            }
        }
    }

    private suspend fun loadUsageData() = withContext(Dispatchers.IO) {
        _state.update { it.copy(isLoading = true) }
        val cameraAll = repo.getAllCameraAppList()
        val audioAll = repo.getAllAudioAppList()
        val cameraPageCount = (cameraAll.size + MainContract.PAGE_SIZE - 1) / MainContract.PAGE_SIZE
        val audioPageCount = (audioAll.size + MainContract.PAGE_SIZE - 1) / MainContract.PAGE_SIZE
        val cameraPage1 = cameraAll.take(MainContract.PAGE_SIZE)
        val audioPage1 = audioAll.take(MainContract.PAGE_SIZE)
        _state.update {
            it.copy(
                cameraUsageList = cameraPage1,
                audioUsageList = audioPage1,
                cameraUsagePageCount = cameraPageCount.coerceAtLeast(1),
                audioUsagePageCount = audioPageCount.coerceAtLeast(1),
                currentCameraUsagePage = 1,
                currentAudioUsagePage = 1,
                isLoading = false
            )
        }
    }

    private suspend fun loadCameraPage(page: Int) = withContext(Dispatchers.IO) {
        val all = repo.getAllCameraAppList()
        val start = (page - 1) * MainContract.PAGE_SIZE
        val slice = all.drop(start).take(MainContract.PAGE_SIZE)
        val pageCount = (all.size + MainContract.PAGE_SIZE - 1) / MainContract.PAGE_SIZE
        _state.update {
            it.copy(
                cameraUsageList = slice,
                cameraUsagePageCount = pageCount.coerceAtLeast(1),
                currentCameraUsagePage = page
            )
        }
    }

    private suspend fun loadAudioPage(page: Int) = withContext(Dispatchers.IO) {
        val all = repo.getAllAudioAppList()
        val start = (page - 1) * MainContract.PAGE_SIZE
        val slice = all.drop(start).take(MainContract.PAGE_SIZE)
        val pageCount = (all.size + MainContract.PAGE_SIZE - 1) / MainContract.PAGE_SIZE
        _state.update {
            it.copy(
                audioUsageList = slice,
                audioUsagePageCount = pageCount.coerceAtLeast(1),
                currentAudioUsagePage = page
            )
        }
    }

    private suspend fun loadExceptionData() = withContext(Dispatchers.IO) {
        val exCamera = repo.getExceptionCameraAppData() ?: emptyList()
        val exAudio = repo.getExceptionAudioAppData() ?: emptyList()
        _state.update {
            it.copy(exCameraList = exCamera, exAudioList = exAudio)
        }
    }

    private suspend fun removeFromException(pkgName: String, isCamera: Boolean) = withContext(Dispatchers.IO) {
        val now = System.currentTimeMillis()
        if (isCamera) repo.updateCameraNotiFlag(pkgName, true, now)
        else repo.updateAudioNotiFlag(pkgName, true, now)
        loadExceptionData()
    }
}
