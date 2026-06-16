package com.yunho.king.feature.main.settings

import androidx.lifecycle.ViewModel
import com.yunho.king.core.common.mvi.mviIntentStore
import com.yunho.king.domain.repository.RepositorySource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val repo: RepositorySource
) : ViewModel() {

    private val store = mviIntentStore(SettingsContract.State()) { intent, _, reduce, _ ->
        when (intent) {
            is SettingsContract.Intent.Load -> load(reduce)
            is SettingsContract.Intent.SetAppAlim -> setAppAlim(intent.enabled, reduce)
        }
    }

    val state = store.uiState

    fun onIntent(intent: SettingsContract.Intent) = store.onIntent(intent)

    private suspend fun load(reduce: (SettingsContract.State.() -> SettingsContract.State) -> Unit) =
        withContext(Dispatchers.IO) {
            val enabled = repo.getAppAlim()
            reduce { copy(appAlimEnabled = enabled, isLoading = false) }
        }

    private suspend fun setAppAlim(
        enabled: Boolean,
        reduce: (SettingsContract.State.() -> SettingsContract.State) -> Unit
    ) = withContext(Dispatchers.IO) {
        repo.setAppAlim(enabled)
        reduce { copy(appAlimEnabled = enabled) }
    }
}
