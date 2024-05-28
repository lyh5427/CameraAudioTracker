package com.yunho.king.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.yunho.king.domain.dto.CameraAppData

@Database(entities = [CameraAppData::class], version = 1)
abstract class CameraDataBase : RoomDatabase() {
    abstract fun db(): CameraDao
}
