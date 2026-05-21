package com.yunho.king.feature.intercept.audio

import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
class AudioInterceptViewModel @Inject constructor(
    private val repo: RepositorySource
) : ViewModel() {

    private val _state = MutableStateFlow(AudioInterceptContract.State())
    val state: StateFlow<AudioInterceptContract.State> = _state.asStateFlow()

    fun onIntent(intent: AudioInterceptContract.Intent) {
        when (intent) {
            is AudioInterceptContract.Intent.SetPackageName -> {
                _state.update { it.copy(packageName = intent.pkg) }
                loadAppData(intent.pkg)
            }
            is AudioInterceptContract.Intent.SetAlim -> {
                if (intent.appAlim) {
                    viewModelScope.launch(Dispatchers.IO) { repo.setAppAlim(false) }
                }
                if (intent.audioAlim) updateNotiFlag()
            }
            else -> { }
        }
    }

    private fun loadAppData(pkgName: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    val data = repo.getAudioAppData(pkgName)
                    repo.updateAudioAppPermUseCount(pkgName, data.permUseCount + 1)
                    repo.updateAudioLastUseDate(pkgName, System.currentTimeMillis())
                    _state.update { it.copy(appName = data.appName) }
                } catch (_: Exception) { }
            }
        }
    }

    fun setAppInfo(pm: PackageManager) {
        val pkg = _state.value.packageName
        if (pkg.isEmpty()) return
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    val appInfo = pm.getApplicationInfo(pkg, PackageManager.GET_META_DATA)
                    val name = pm.getApplicationLabel(appInfo).toString()
                    val icon: Drawable = pm.getApplicationIcon(appInfo)
                    _state.update { it.copy(appName = name, appIcon = icon) }
                } catch (_: Exception) { }
            }
        }
    }

    private fun updateNotiFlag() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val pkg = _state.value.packageName
                try {
                    val data = repo.getAudioAppData(pkg)
                    repo.updateAudioNotiFlag(data.appPackageName, false, System.currentTimeMillis())
                } catch (_: Exception) { }
            }
        }
    }
}
