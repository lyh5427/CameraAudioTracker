package com.yunho.king.presentation.ui.audiointercept

import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yunho.king.domain.di.RepositorySource
import com.yunho.king.domain.dto.AudioAppData
import com.yunho.king.domain.dto.CameraAppData
import com.yunho.king.domain.dto.State
import com.yunho.king.presentation.constant.Status
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class AudioInterceptViewModel @Inject constructor(
    private val repo: RepositorySource
): ViewModel() {
    private var _appName: MutableSharedFlow<State> =
        MutableSharedFlow(0,1, BufferOverflow.DROP_LATEST)
    val appName = _appName.asSharedFlow()

    private var _appIcon: MutableSharedFlow<Drawable> =
        MutableSharedFlow(0, 1, BufferOverflow.DROP_LATEST)
    val appIcon = _appIcon.asSharedFlow()

    private var _action: MutableSharedFlow<State> =
        MutableSharedFlow(0, 1, BufferOverflow.DROP_LATEST)
    val action = _action.asSharedFlow()

    var packageName = ""
    lateinit var appData: AudioAppData
    lateinit var appInfo: ApplicationInfo

    suspend fun updateUseCount() {
        if (::appData.isInitialized) {
            repo.updateAudioAppPermUseCount(packageName, appData.permUseCount + 1)
        }
    }

    suspend fun updateUseDate() {
        repo.updateAudioLastUseDate(packageName, System.currentTimeMillis())
    }

    fun updateNotiFlag() {
        CoroutineScope(Dispatchers.IO).launch {
            repo.updateAudioNotiFlag(appData.appPackageName, false, System.currentTimeMillis())
        }
    }

    fun getAudioAppData() {
        viewModelScope.launch {
            runBlocking(Dispatchers.IO) {
                appData = repo.getAudioAppData(packageName)
            }
        }
    }

    fun setAppInfo(pm: PackageManager) {
        appInfo = pm.getApplicationInfo(packageName, PackageManager.GET_META_DATA)
        CoroutineScope(Dispatchers.IO).launch {
            getAppName(pm)
            getAppIcon(pm)
            updateUseCount()
            updateUseDate()
        }
    }

    suspend fun getAppName(pm: PackageManager) {
        _appName.emit(State(Status.TEXT, pm.getApplicationLabel(appInfo).toString()?: ""))
    }

    suspend fun getAppIcon(pm: PackageManager) {
        _appIcon.emit(pm.getApplicationIcon(appInfo))
    }
}