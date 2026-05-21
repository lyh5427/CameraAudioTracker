package com.yunho.king.feature.launch.intro

import android.content.Context
import androidx.lifecycle.ViewModel
import com.yunho.king.core.common.PermManager
import com.yunho.king.core.common.mvi.MviIntentStore
import com.yunho.king.core.common.mvi.mviIntentStore
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class IntroViewModel @Inject constructor(
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val permManager = PermManager(context)

    private val store: MviIntentStore<IntroContract.State, IntroContract.Intent, IntroContract.Effect> =
        mviIntentStore(IntroContract.State()) { intent, state, reduce, postEffect ->
            when (intent) {
                is IntroContract.Intent.OnStart -> {
                    reduce { copy(isLoading = false) }
                    val effect = if (permManager.isAllPermAllow()) {
                        IntroContract.Effect.NavigateToMain
                    } else {
                        IntroContract.Effect.NavigateToPerm
                    }
                    postEffect(effect)
                }
            }
        }

    val uiState = store.uiState
    val sideEffects = store.sideEffects

    fun onIntent(intent: IntroContract.Intent) = store.onIntent(intent)
}
