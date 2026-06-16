package com.yunho.king.feature.appdetail

import androidx.lifecycle.ViewModel
import com.yunho.king.core.common.mvi.MviIntentStore
import com.yunho.king.core.common.mvi.mviIntentStore
import com.yunho.king.domain.repository.RepositorySource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class AppDetailViewModel @Inject constructor(
    private val repo: RepositorySource
) : ViewModel() {

    private val store: MviIntentStore<AppDetailContract.State, AppDetailContract.Intent, AppDetailContract.Effect> =
        mviIntentStore(AppDetailContract.State()) { intent, _, reduce, _ ->
            when (intent) {
                is AppDetailContract.Intent.Load -> {
                    reduce { copy(isLoading = true) }
                    val (appName, cameraData, audioData) = withContext(Dispatchers.IO) {
                        val camera = runCatching { repo.getCameraAppData(intent.pkgName) }.getOrNull()
                        val audio = runCatching { repo.getAudioAppData(intent.pkgName) }.getOrNull()
                        val name = camera?.appName ?: audio?.appName ?: intent.pkgName
                        Triple(name, camera, audio)
                    }
                    reduce {
                        copy(
                            appName = appName,
                            packageName = intent.pkgName,
                            cameraData = cameraData,
                            audioData = audioData,
                            isLoading = false
                        )
                    }
                }
            }
        }

    val state = store.uiState
    val sideEffects = store.sideEffects

    fun onIntent(intent: AppDetailContract.Intent) = store.onIntent(intent)
}
