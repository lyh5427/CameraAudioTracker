package com.yunho.king.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.yunho.king.core.model.AudioAppData

@Database(entities = [AudioAppData::class], version = 1, exportSchema = false)
abstract class AudioDataBase : RoomDatabase() {
    abstract fun db(): AudioDao
}
