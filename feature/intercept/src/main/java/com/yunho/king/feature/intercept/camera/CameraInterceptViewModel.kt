package com.yunho.king.feature.intercept.camera

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
class CameraInterceptViewModel @Inject constructor(
    private val repo: RepositorySource
) : ViewModel() {

    private val _state = MutableStateFlow(CameraInterceptContract.State())
    val state: StateFlow<CameraInterceptContract.State> = _state.asStateFlow()

    fun onIntent(intent: CameraInterceptContract.Intent) {
        when (intent) {
            is CameraInterceptContract.Intent.SetPackageName -> {
                _state.update { it.copy(packageName = intent.pkg) }
                loadAppData(intent.pkg)
            }
            is CameraInterceptContract.Intent.SetAlim -> {
                if (intent.appAlim) {
                    viewModelScope.launch(Dispatchers.IO) { repo.setAppAlim(false) }
                }
                if (intent.cameraAlim) updateNotiFlag()
            }
            else -> { }
        }
    }

    private fun loadAppData(pkgName: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    val data = repo.getCameraAppData(pkgName)
                    repo.updateCameraAppPermUseCount(pkgName, data.permUseCount + 1)
                    repo.updateLastUseDate(pkgName, System.currentTimeMillis())
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
                    val data = repo.getCameraAppData(pkg)
                    repo.updateCameraNotiFlag(data.appPackageName, false, System.currentTimeMillis())
                } catch (_: Exception) { }
            }
        }
    }
}
