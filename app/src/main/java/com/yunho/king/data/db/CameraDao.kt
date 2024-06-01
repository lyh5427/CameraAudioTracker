package com.yunho.king.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.yunho.king.domain.dto.CameraAppData

@Dao
interface CameraDao {
    @Query("SELECT * FROM CameraAppData")
    fun getAll(): List<CameraAppData>

    @Query("SELECT * FROM CameraAppData WHERE appPackageName = :appPackageName")
    fun getCameraAppData(appPackageName: String): CameraAppData

    @Query("SELECT permUseCount FROM CameraAppData WHERE appName = :appName")
    fun getPermUseCount(appName: String): Int

    @Query("DELETE FROM CameraAppData WHERE appPackageName = :appPackageName")
    fun delete(appPackageName: String)

    @Query("UPDATE CameraAppData Set permUseCount = :updateCount WHERE appPackageName = :appPackageName")
    fun updatePermUseCount(appPackageName: String, updateCount: Int)

    @Query("SELECT * FROM CameraAppData WHERE appPackageName = :packageName")
    fun isExistAppData(packageName: String): CameraAppData?

    @Insert
    fun insert(data: CameraAppData)

    @Query("DELETE FROM CameraAppData")
    fun deleteAll()

}