package com.yunho.king.domain.di

import android.graphics.Camera
import com.yunho.king.domain.dto.AudioAppData
import com.yunho.king.domain.dto.CameraAppData

interface RepositorySource {
    fun clearDb()

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
    fun deleteAllAudio()
}