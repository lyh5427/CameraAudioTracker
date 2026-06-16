package com.yunho.king.domain.repository

import com.yunho.king.core.model.AudioAppData
import com.yunho.king.core.model.CameraAppData

interface RepositorySource {
    fun clearDb()

    // Preference (DataStore)
    suspend fun getAppAlim(): Boolean
    suspend fun setAppAlim(enabled: Boolean)
    suspend fun getFirstOpenApp(): Boolean
    suspend fun setFirstOpenApp(isFirst: Boolean)
    suspend fun getRemoveList(): Set<String>
    suspend fun setRemoveList(pkgs: Set<String>)

    // Camera DB
    fun getAllCameraAppList(): List<CameraAppData>
    fun getCameraAppData(pkgName: String): CameraAppData
    fun getCameraAppPermUseCount(pkgName: String): Int
    fun deleteCameraApp(pkgName: String)
    fun updateCameraAppPermUseCount(pkgName: String, count: Int)
    suspend fun existCameraApp(pkgName: String): Boolean
    fun updateLastUseDate(pkgName: String, lastUse: Long)
    fun updateCameraNotiFlag(pkgName: String, notiFlag: Boolean, exceptionDate: Long)
    fun getExceptionCameraAppData(): List<CameraAppData>?
    suspend fun insertCameraApp(data: CameraAppData)
    fun deleteAllCamera()

    // Audio DB
    fun getAllAudioAppList(): List<AudioAppData>
    fun getAudioAppData(pkgName: String): AudioAppData
    fun getAudioAppPermUseCount(pkgName: String): Int
    fun deleteAudioApp(pkgName: String)
    fun updateAudioAppPermUseCount(pkgName: String, count: Int)
    suspend fun existAudioApp(pkgName: String): Boolean
    suspend fun insertAudioApp(data: AudioAppData)
    fun updateAudioNotiFlag(pkgName: String, notiFlag: Boolean, exceptionDate: Long)
    fun getExceptionAudioAppData(): List<AudioAppData>?
    fun updateAudioLastUseDate(pkgName: String, lastUse: Long)
    fun deleteAllAudio()
}
