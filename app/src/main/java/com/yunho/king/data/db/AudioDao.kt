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

    @Insert
    fun insert(data: AudioAppData)

    @Query("DELETE FROM AudioAppData")
    fun deleteAll()

}