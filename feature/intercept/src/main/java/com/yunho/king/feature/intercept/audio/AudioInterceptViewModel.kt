package com.yunho.king.feature.intercept.audio

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
class AudioInterceptViewModel @Inject constructor(
    private val repo: RepositorySource
) : ViewModel() {

    private val store: MviIntentStore<AudioInterceptContract.State, AudioInterceptContract.Intent, AudioInterceptContract.Effect> =
        mviIntentStore(AudioInterceptContract.State()) { intent, state, reduce, _ ->
            when (intent) {
                is AudioInterceptContract.Intent.SetPackageName -> {
                    reduce { copy(packageName = intent.pkg) }
                    loadAppData(intent.pkg, reduce)
                }
                is AudioInterceptContract.Intent.SetAppInfo -> {
                    reduce { copy(appName = intent.appName, appIcon = intent.appIcon) }
                }
                is AudioInterceptContract.Intent.SetAlim -> {
                    withContext(Dispatchers.IO) {
                        if (intent.appAlim) runCatching { repo.setAppAlim(false) }
                        if (intent.audioAlim) updateNotiFlag(state.packageName)
                    }
                }
                else -> Unit
            }
        }

    val state = store.uiState
    val sideEffects = store.sideEffects

    fun onIntent(intent: AudioInterceptContract.Intent) = store.onIntent(intent)

    fun loadAppInfo(pm: PackageManager) {
        val pkg = state.value.packageName
        if (pkg.isEmpty()) return
        store.onIntent(
            AudioInterceptContract.Intent.SetAppInfo(
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
        reduce: (AudioInterceptContract.State.() -> AudioInterceptContract.State) -> Unit
    ) = withContext(Dispatchers.IO) {
        runCatching {
            val data = repo.getAudioAppData(pkgName)
            repo.updateAudioAppPermUseCount(pkgName, data.permUseCount + 1)
            repo.updateAudioLastUseDate(pkgName, System.currentTimeMillis())
            reduce { copy(appName = data.appName) }
        }
    }

    private suspend fun updateNotiFlag(pkgName: String) {
        runCatching {
            val data = repo.getAudioAppData(pkgName)
            repo.updateAudioNotiFlag(data.appPackageName, false, System.currentTimeMillis())
        }
    }
}
