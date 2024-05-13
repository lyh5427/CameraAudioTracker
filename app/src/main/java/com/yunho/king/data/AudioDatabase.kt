package com.yunho.king.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.yunho.king.domain.dto.AudioAppData

@Database(entities = [AudioAppData::class], version = 1)
abstract class AudioDataBase : RoomDatabase() {
    abstract fun db(): AudioDao
}

