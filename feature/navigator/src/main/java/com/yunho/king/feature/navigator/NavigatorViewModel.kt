package com.yunho.king.feature.navigator

import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yunho.king.core.common.Const
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NavigatorViewModel @Inject constructor() : ViewModel() {

    private val _pendingRoute = MutableSharedFlow<String>(replay = 0)
    val pendingRoute: SharedFlow<String> = _pendingRoute.asSharedFlow()

    fun setIntent(intent: Intent?) {
        if (intent == null) return
        viewModelScope.launch {
            when (intent.getStringExtra("SCREEN")) {
                "camera_intercept" -> {
                    val pkg = intent.getStringExtra(Const.PKG_NAME) ?: return@launch
                    _pendingRoute.emit("camera_intercept/$pkg")
                }
                "audio_intercept" -> {
                    val pkg = intent.getStringExtra(Const.PKG_NAME) ?: return@launch
                    _pendingRoute.emit("audio_intercept/$pkg")
                }
                else -> { }
            }
        }
    }
}
