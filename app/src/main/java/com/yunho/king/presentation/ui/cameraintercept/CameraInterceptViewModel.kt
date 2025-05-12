package com.yunho.king.presentation.ui.cameraintercept

import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.lifecycle.ViewModel
import com.yunho.king.Status
import com.yunho.king.domain.di.RepositorySource
import com.yunho.king.domain.dto.CameraAppData
import com.yunho.king.domain.dto.State
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
class CameraInterceptViewModel @Inject constructor(
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
    lateinit var appData: CameraAppData
    lateinit var appInfo: ApplicationInfo


    suspend fun updateUseCount() {
        if (::appData.isInitialized) {
            repo.updateCameraAppPermUseCount(packageName, appData.permUseCount + 1)
        }
    }

    suspend fun updateUseDate() {
        repo.updateLastUseDate(packageName, System.currentTimeMillis())
    }

    fun updateNotiFlag() {
        CoroutineScope(Dispatchers.IO).launch {
            repo.updateCameraNotiFlag(appData.appPackageName, false, System.currentTimeMillis())
        }
    }

    fun getCameraAppData() {
        runBlocking(Dispatchers.IO) {
            Log.e("yunho", "${packageName}")
            appData = repo.getCameraAppData(packageName)
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