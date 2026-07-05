package com.yunho.king.feature.navigator

import android.content.Intent
import androidx.lifecycle.ViewModel
import com.yunho.king.core.common.Const
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class NavigatorViewModel @Inject constructor() : ViewModel() {

    private val _pendingRoute = MutableStateFlow<String?>(null)
    val pendingRoute: StateFlow<String?> = _pendingRoute.asStateFlow()

    fun setIntent(intent: Intent?) {
        if (intent == null) return
        _pendingRoute.value = when (intent.getStringExtra("SCREEN")) {
            "camera_intercept" -> {
                val pkg = intent.getStringExtra(Const.PKG_NAME) ?: return
                "camera_intercept/$pkg"
            }
            "audio_intercept" -> {
                val pkg = intent.getStringExtra(Const.PKG_NAME) ?: return
                "audio_intercept/$pkg"
            }
            else -> return
        }
    }

    fun consumePendingRoute() {
        _pendingRoute.value = null
    }
}
