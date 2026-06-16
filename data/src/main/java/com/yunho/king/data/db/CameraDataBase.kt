package com.yunho.king.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.yunho.king.core.model.CameraAppData

@Database(entities = [CameraAppData::class], version = 1, exportSchema = false)
abstract class CameraDataBase : RoomDatabase() {
    abstract fun db(): CameraDao
}
