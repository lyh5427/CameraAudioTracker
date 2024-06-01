package com.yunho.king.domain.di

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
    fun existCameraApp(pkgName: String): Boolean
    fun insertCameraApp(data: CameraAppData)
    fun deleteAllCamera()

    // Audio DB
    fun getAllAudioAppList(): List<AudioAppData>
    fun getAudioAppData(pkgName: String): AudioAppData
    fun getAudioAppPermUseCount(pkgName: String): Int
    fun deleteAudioApp(pkgName: String)
    fun updateAudioAppPermUseCount(pkgName: String, count: Int)
    fun existAudioApp(pkgName: String): Boolean
    fun insertAudioApp(data: AudioAppData)
    fun deleteAllAudio()
}