package com.yunho.king.presentation

import android.text.BoringLayout
import com.yunho.king.data.db.AudioDao
import com.yunho.king.data.db.AudioDataBase
import com.yunho.king.data.db.CameraDao
import com.yunho.king.data.db.CameraDataBase
import com.yunho.king.domain.di.RepositorySource
import com.yunho.king.domain.dto.AudioAppData
import com.yunho.king.domain.dto.CameraAppData
import java.lang.Exception
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Repository @Inject constructor(
    private val caDb: CameraDao,
    private val adDb: AudioDao
): RepositorySource {

    override fun clearDb() {
        caDb.deleteAll()
        adDb.deleteAll()
    }

    override fun getAllCameraAppList(): List<CameraAppData> = caDb.getAll()

    override fun getCameraAppData(pkgName: String): CameraAppData {
        return caDb.getCameraAppData(pkgName)
    }

    override fun getCameraAppPermUseCount(pkgName: String): Int {
        return caDb.getPermUseCount(pkgName)
    }

    override fun deleteCameraApp(pkgName: String) {
        caDb.delete(pkgName)
    }

    override fun updateCameraAppPermUseCount(pkgName: String, count: Int) {
        caDb.updatePermUseCount(pkgName, count)
    }

    override suspend fun existCameraApp(pkgName: String): Boolean {
        return caDb.isExistAppData(pkgName) != null
    }

    override fun updateLastUseDate(pkgName: String, lastUse: Long) {
        caDb.updateLastUseDate(pkgName, lastUse)
    }

    override fun updateCameraNotiFlag(pkgName: String, notiFlag: Boolean, exceptionDate: Long) {
        caDb.updateNotiFlag(pkgName, notiFlag)
        caDb.updateExceptionDate(pkgName, exceptionDate)
    }

    override suspend fun insertCameraApp(data: CameraAppData) {
        caDb.insert(data)
    }

    override fun getExceptionCameraAppData(): List<CameraAppData>? {
        return caDb.getExceptionPackage(false)
    }

    override fun deleteAllCamera() {
        caDb.deleteAll()
    }

    override fun getAllAudioAppList(): List<AudioAppData> = adDb.getAll()

    override fun getAudioAppData(pkgName: String): AudioAppData {
        return adDb.getAudioAppData(pkgName)
    }

    override fun getAudioAppPermUseCount(pkgName: String): Int {
        return adDb.getPermUseCount(pkgName)
    }

    override fun deleteAudioApp(pkgName: String) {
        adDb.delete(pkgName)
    }

    override fun updateAudioAppPermUseCount(pkgName: String, count: Int) {
        adDb.updatePermUseCount(pkgName, count)
    }

    override suspend fun existAudioApp(pkgName: String): Boolean {
        return adDb.isExistAppData(pkgName) != null
    }

    override suspend fun insertAudioApp(data: AudioAppData) {
        adDb.insert(data)
    }

    override fun updateAudioNotiFlag(pkgName: String, notiFlag: Boolean, exceptionDate: Long) {
        adDb.updateNotiFlag(pkgName, notiFlag)
        adDb.updateExceptionDate(pkgName, exceptionDate)
    }

    override fun getExceptionAudioAppData(): List<AudioAppData>? {
        return adDb.getExceptionPackage(false)
    }

    override fun deleteAllAudio() {
        adDb.deleteAll()
    }
}