package com.yunho.king.feature.main

import androidx.lifecycle.ViewModel
import com.yunho.king.core.common.mvi.MviIntentStore
import com.yunho.king.core.common.mvi.mviIntentStore
import com.yunho.king.domain.repository.RepositorySource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repo: RepositorySource
) : ViewModel() {

    private val store: MviIntentStore<MainContract.State, MainContract.Intent, MainContract.Effect> =
        mviIntentStore(MainContract.State()) { intent, state, reduce, postEffect ->
            when (intent) {
                is MainContract.Intent.LoadUsageData -> loadUsageData(reduce)
                is MainContract.Intent.LoadExceptionData -> loadExceptionData(reduce)
                is MainContract.Intent.LoadAllApps -> loadAllApps(reduce)
                is MainContract.Intent.SelectMainTab -> {
                    reduce { copy(selectedMainTab = intent.tab) }
                    if (intent.tab == MainContract.MainTab.Hole) {
                        loadAllApps(reduce)
                    }
                }
                is MainContract.Intent.SelectUsageTab -> reduce { copy(selectedUsageTab = intent.tab) }
                is MainContract.Intent.LoadCameraPage -> loadCameraPage(intent.page, reduce)
                is MainContract.Intent.LoadAudioPage -> loadAudioPage(intent.page, reduce)
                is MainContract.Intent.NavigateToDetail -> {
                    postEffect(MainContract.Effect.NavigateToAppDetail(intent.pkgName))
                }
                is MainContract.Intent.OpenSettings -> {
                    postEffect(MainContract.Effect.NavigateToSettings)
                }
                is MainContract.Intent.RemoveFromException -> {
                    removeFromException(intent.pkgName, intent.isCamera, reduce)
                }
            }
        }

    val state = store.uiState
    val sideEffects = store.sideEffects

    fun onIntent(intent: MainContract.Intent) = store.onIntent(intent)

    private suspend fun loadUsageData(reduce: (MainContract.State.() -> MainContract.State) -> Unit) =
        withContext(Dispatchers.IO) {
            reduce { copy(isLoading = true) }
            val cameraAll = repo.getAllCameraAppList()
            val audioAll = repo.getAllAudioAppList()
            val cameraPageCount = (cameraAll.size + MainContract.PAGE_SIZE - 1) / MainContract.PAGE_SIZE
            val audioPageCount = (audioAll.size + MainContract.PAGE_SIZE - 1) / MainContract.PAGE_SIZE
            reduce {
                copy(
                    cameraUsageList = cameraAll.take(MainContract.PAGE_SIZE),
                    audioUsageList = audioAll.take(MainContract.PAGE_SIZE),
                    cameraUsagePageCount = cameraPageCount.coerceAtLeast(1),
                    audioUsagePageCount = audioPageCount.coerceAtLeast(1),
                    currentCameraUsagePage = 1,
                    currentAudioUsagePage = 1,
                    isLoading = false
                )
            }
        }

    private suspend fun loadCameraPage(
        page: Int,
        reduce: (MainContract.State.() -> MainContract.State) -> Unit
    ) = withContext(Dispatchers.IO) {
        val all = repo.getAllCameraAppList()
        val start = (page - 1) * MainContract.PAGE_SIZE
        val pageCount = (all.size + MainContract.PAGE_SIZE - 1) / MainContract.PAGE_SIZE
        reduce {
            copy(
                cameraUsageList = all.drop(start).take(MainContract.PAGE_SIZE),
                cameraUsagePageCount = pageCount.coerceAtLeast(1),
                currentCameraUsagePage = page
            )
        }
    }

    private suspend fun loadAudioPage(
        page: Int,
        reduce: (MainContract.State.() -> MainContract.State) -> Unit
    ) = withContext(Dispatchers.IO) {
        val all = repo.getAllAudioAppList()
        val start = (page - 1) * MainContract.PAGE_SIZE
        val pageCount = (all.size + MainContract.PAGE_SIZE - 1) / MainContract.PAGE_SIZE
        reduce {
            copy(
                audioUsageList = all.drop(start).take(MainContract.PAGE_SIZE),
                audioUsagePageCount = pageCount.coerceAtLeast(1),
                currentAudioUsagePage = page
            )
        }
    }

    private suspend fun loadExceptionData(
        reduce: (MainContract.State.() -> MainContract.State) -> Unit
    ) = withContext(Dispatchers.IO) {
        val exCamera = repo.getExceptionCameraAppData() ?: emptyList()
        val exAudio = repo.getExceptionAudioAppData() ?: emptyList()
        reduce { copy(exCameraList = exCamera, exAudioList = exAudio) }
    }

    private suspend fun loadAllApps(
        reduce: (MainContract.State.() -> MainContract.State) -> Unit
    ) = withContext(Dispatchers.IO) {
        reduce {
            copy(
                allCameraList = repo.getAllCameraAppList(),
                allAudioList = repo.getAllAudioAppList()
            )
        }
    }

    private suspend fun removeFromException(
        pkgName: String,
        isCamera: Boolean,
        reduce: (MainContract.State.() -> MainContract.State) -> Unit
    ) = withContext(Dispatchers.IO) {
        val now = System.currentTimeMillis()
        if (isCamera) repo.updateCameraNotiFlag(pkgName, true, now)
        else repo.updateAudioNotiFlag(pkgName, true, now)
        loadExceptionData(reduce)
    }
}
