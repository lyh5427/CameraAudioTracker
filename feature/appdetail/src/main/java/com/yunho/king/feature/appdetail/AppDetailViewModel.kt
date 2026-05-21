package com.yunho.king.feature.appdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yunho.king.core.model.AudioAppData
import com.yunho.king.core.model.CameraAppData
import com.yunho.king.domain.repository.RepositorySource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@HiltViewModel
class AppDetailViewModel @Inject constructor(
    private val repo: RepositorySource
) : ViewModel() {

    private val _state = MutableStateFlow(AppDetailContract.State())
    val state: StateFlow<AppDetailContract.State> = _state.asStateFlow()

    fun onIntent(intent: AppDetailContract.Intent) {
        when (intent) {
            is AppDetailContract.Intent.Load -> loadAppData(intent.pkgName)
        }
    }

    private fun loadAppData(pkgName: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            withContext(Dispatchers.IO) {
                val cameraData = try {
                    repo.getCameraAppData(pkgName)
                } catch (_: Exception) {
                    null
                }
                val audioData = try {
                    repo.getAudioAppData(pkgName)
                } catch (_: Exception) {
                    null
                }
                val appName = when {
                    cameraData != null -> cameraData.appName
                    audioData != null -> audioData.appName
                    else -> pkgName
                }
                _state.update {
                    it.copy(
                        appName = appName,
                        packageName = pkgName,
                        cameraData = cameraData,
                        audioData = audioData,
                        isLoading = false
                    )
                }
            }
        }
    }
}
