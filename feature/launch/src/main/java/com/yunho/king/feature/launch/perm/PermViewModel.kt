package com.yunho.king.feature.launch.perm

import android.content.Context
import androidx.lifecycle.ViewModel
import com.yunho.king.core.common.PermManager
import com.yunho.king.core.common.mvi.MviIntentStore
import com.yunho.king.core.common.mvi.mviIntentStore
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class PermViewModel @Inject constructor(
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val permManager = PermManager(context)

    private val store: MviIntentStore<PermContract.State, PermContract.Intent, PermContract.Effect> =
        mviIntentStore(PermContract.State()) { intent, state, reduce, postEffect ->
            when (intent) {
                is PermContract.Intent.RequestPermissions -> {
                    reduce { copy(isLoading = true) }
                }
                is PermContract.Intent.PermissionsResult -> {
                    reduce { copy(isLoading = false) }
                    when {
                        permManager.isAllPermAllow() -> postEffect(PermContract.Effect.NavigateToMain)
                        !permManager.isUsagesPermAllow() -> postEffect(PermContract.Effect.OpenUsageStatsSettings)
                        !permManager.isOverlayAllow() -> postEffect(PermContract.Effect.OpenOverlaySettings)
                        else -> postEffect(PermContract.Effect.NavigateToMain)
                    }
                }
                is PermContract.Intent.SkipToMain -> postEffect(PermContract.Effect.NavigateToMain)
            }
        }

    val uiState = store.uiState
    val sideEffects = store.sideEffects

    fun onIntent(intent: PermContract.Intent) = store.onIntent(intent)
}
