package com.yunho.king.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.yunho.king.domain.dto.AudioAppData
import com.yunho.king.domain.dto.CameraAppData


@Dao
interface AudioDao {

    @Query("SELECT * FROM AudioAppData ORDER BY appName DESC")
    fun getAll(): List<AudioAppData>

    @Query("SELECT * FROM AudioAppData WHERE appPackageName = :appPackageName")
    fun getAudioAppData(appPackageName: String): AudioAppData

    @Query("SELECT permUseCount FROM AudioAppData WHERE appPackageName = :appPackageName")
    fun getPermUseCount(appPackageName: String): Int

    @Query("DELETE FROM AudioAppData WHERE appPackageName = :appPackageName")
    fun delete(appPackageName: String)

    @Query("UPDATE AudioAppData Set permUseCount = :updateCount WHERE appPackageName = :appPackageName")
    fun updatePermUseCount(appPackageName: String, updateCount: Int)

    @Query("SELECT * FROM AudioAppData WHERE appPackageName = :packageName")
    fun isExistAppData(packageName: String): AudioAppData?

    @Query("UPDATE AudioAppData set notiFlag = :notiFlag WHERE appPackageName = :pkgName")
    fun updateNotiFlag(pkgName: String, notiFlag: Boolean)

    @Query("UPDATE AudioAppData set exceptionDate = :exceptionDate WHERE appPackageName = :pkgName")
    fun updateExceptionDate(pkgName: String, exceptionDate: Long)

    @Query("SELECT * FROM AudioAppData WHERE notiFlag = :notiFlag")
    fun getExceptionPackage(notiFlag: Boolean = false): List<AudioAppData>?

    @Insert
    fun insert(data: AudioAppData)

    @Query("DELETE FROM AudioAppData")
    fun deleteAll()

}