package com.yunho.king.domain.di


import android.content.Context
import androidx.room.Room
import com.yunho.king.data.AudioDataBase
import com.yunho.king.data.CameraDataBase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
annotation class AudioDao

@Qualifier
annotation class CameraDao

@InstallIn(SingletonComponent::class)
@Module
class RoomCreateModule {
    @AudioDao
    @Singleton
    @Provides
    fun providerAudioDataBase(@ApplicationContext context: Context): AudioDataBase {
        val db = Room.databaseBuilder(
            context,
            AudioDataBase::class.java,
            "flora_audio_db").build()

        db.openHelper.writableDatabase
        return db
    }

    @CameraDao
    @Singleton
    @Provides
    fun providerCameraDataBase(@ApplicationContext context: Context): CameraDataBase {
        val db = Room.databaseBuilder(
            context,
            CameraDataBase::class.java,
            "flora_camera_db").build()

        db.openHelper.writableDatabase
        return db
    }
}