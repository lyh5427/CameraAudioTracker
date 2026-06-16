package com.yunho.king.feature.intercept.camera

import android.content.pm.PackageManager
import androidx.lifecycle.ViewModel
import com.yunho.king.core.common.mvi.MviIntentStore
import com.yunho.king.core.common.mvi.mviIntentStore
import com.yunho.king.domain.repository.RepositorySource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class CameraInterceptViewModel @Inject constructor(
    private val repo: RepositorySource
) : ViewModel() {

    private val store: MviIntentStore<CameraInterceptContract.State, CameraInterceptContract.Intent, CameraInterceptContract.Effect> =
        mviIntentStore(CameraInterceptContract.State()) { intent, state, reduce, _ ->
            when (intent) {
                is CameraInterceptContract.Intent.SetPackageName -> {
                    reduce { copy(packageName = intent.pkg) }
                    loadAppData(intent.pkg, reduce)
                }
                is CameraInterceptContract.Intent.SetAppInfo -> {
                    reduce { copy(appName = intent.appName, appIcon = intent.appIcon) }
                }
                is CameraInterceptContract.Intent.SetAlim -> {
                    withContext(Dispatchers.IO) {
                        if (intent.appAlim) runCatching { repo.setAppAlim(false) }
                        if (intent.cameraAlim) updateNotiFlag(state.packageName)
                    }
                }
                else -> Unit
            }
        }

    val state = store.uiState
    val sideEffects = store.sideEffects

    fun onIntent(intent: CameraInterceptContract.Intent) = store.onIntent(intent)

    fun loadAppInfo(pm: PackageManager) {
        val pkg = state.value.packageName
        if (pkg.isEmpty()) return
        store.onIntent(
            CameraInterceptContract.Intent.SetAppInfo(
                appName = runCatching {
                    val info = pm.getApplicationInfo(pkg, PackageManager.GET_META_DATA)
                    pm.getApplicationLabel(info).toString()
                }.getOrDefault(""),
                appIcon = runCatching { pm.getApplicationIcon(pkg) }.getOrNull()
            )
        )
    }

    private suspend fun loadAppData(
        pkgName: String,
        reduce: (CameraInterceptContract.State.() -> CameraInterceptContract.State) -> Unit
    ) = withContext(Dispatchers.IO) {
        runCatching {
            val data = repo.getCameraAppData(pkgName)
            repo.updateCameraAppPermUseCount(pkgName, data.permUseCount + 1)
            repo.updateLastUseDate(pkgName, System.currentTimeMillis())
            reduce { copy(appName = data.appName) }
        }
    }

    private suspend fun updateNotiFlag(pkgName: String) {
        runCatching {
            val data = repo.getCameraAppData(pkgName)
            repo.updateCameraNotiFlag(data.appPackageName, false, System.currentTimeMillis())
        }
    }
}
