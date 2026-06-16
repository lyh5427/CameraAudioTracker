package com.yunho.king.data.repository

import com.yunho.king.core.model.AudioAppData
import com.yunho.king.core.model.CameraAppData
import com.yunho.king.data.db.AudioDao
import com.yunho.king.data.db.CameraDao
import com.yunho.king.data.local.LocalDataSource
import com.yunho.king.domain.repository.RepositorySource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RepositoryImpl @Inject constructor(
    private val caDb: CameraDao,
    private val adDb: AudioDao,
    private val local: LocalDataSource
) : RepositorySource {

    override fun clearDb() {
        caDb.deleteAll()
        adDb.deleteAll()
    }

    override suspend fun getAppAlim(): Boolean = local.getAppAlim()
    override suspend fun setAppAlim(enabled: Boolean) = local.setAppAlim(enabled)
    override suspend fun getFirstOpenApp(): Boolean = local.getFirstOpenApp()
    override suspend fun setFirstOpenApp(isFirst: Boolean) = local.setFirstOpenApp(isFirst)
    override suspend fun getRemoveList(): Set<String> = local.getRemoveList()
    override suspend fun setRemoveList(pkgs: Set<String>) = local.setRemoveList(pkgs)

    override fun getAllCameraAppList(): List<CameraAppData> = caDb.getAll()
    override fun getCameraAppData(pkgName: String): CameraAppData = caDb.getCameraAppData(pkgName)
    override fun getCameraAppPermUseCount(pkgName: String): Int = caDb.getPermUseCount(pkgName)
    override fun deleteCameraApp(pkgName: String) { caDb.delete(pkgName) }
    override fun updateCameraAppPermUseCount(pkgName: String, count: Int) { caDb.updatePermUseCount(pkgName, count) }
    override suspend fun existCameraApp(pkgName: String): Boolean = caDb.isExistAppData(pkgName) != null
    override fun updateLastUseDate(pkgName: String, lastUse: Long) { caDb.updateLastUseDate(pkgName, lastUse) }
    override fun updateCameraNotiFlag(pkgName: String, notiFlag: Boolean, exceptionDate: Long) {
        caDb.updateNotiFlag(pkgName, notiFlag)
        caDb.updateExceptionDate(pkgName, exceptionDate)
    }
    override fun getExceptionCameraAppData(): List<CameraAppData>? = caDb.getExceptionPackage(false)
    override suspend fun insertCameraApp(data: CameraAppData) { caDb.insert(data) }
    override fun deleteAllCamera() { caDb.deleteAll() }

    override fun getAllAudioAppList(): List<AudioAppData> = adDb.getAll()
    override fun getAudioAppData(pkgName: String): AudioAppData = adDb.getAudioAppData(pkgName)
    override fun getAudioAppPermUseCount(pkgName: String): Int = adDb.getPermUseCount(pkgName)
    override fun deleteAudioApp(pkgName: String) { adDb.delete(pkgName) }
    override fun updateAudioAppPermUseCount(pkgName: String, count: Int) { adDb.updatePermUseCount(pkgName, count) }
    override suspend fun existAudioApp(pkgName: String): Boolean = adDb.isExistAppData(pkgName) != null
    override suspend fun insertAudioApp(data: AudioAppData) { adDb.insert(data) }
    override fun updateAudioNotiFlag(pkgName: String, notiFlag: Boolean, exceptionDate: Long) {
        adDb.updateNotiFlag(pkgName, notiFlag)
        adDb.updateExceptionDate(pkgName, exceptionDate)
    }
    override fun getExceptionAudioAppData(): List<AudioAppData>? = adDb.getExceptionPackage(false)
    override fun updateAudioLastUseDate(pkgName: String, lastUse: Long) { adDb.updateLastUseDate(pkgName, lastUse) }
    override fun deleteAllAudio() { adDb.deleteAll() }
}
