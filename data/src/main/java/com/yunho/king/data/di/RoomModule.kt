package com.yunho.king.data.di

import android.content.Context
import androidx.room.Room
import com.yunho.king.data.db.AudioDao
import com.yunho.king.data.db.AudioDataBase
import com.yunho.king.data.db.CameraDao
import com.yunho.king.data.db.CameraDataBase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object RoomModule {

    @Singleton
    @Provides
    fun provideAudioDataBase(@ApplicationContext context: Context): AudioDataBase =
        Room.databaseBuilder(context, AudioDataBase::class.java, "ad.db").build()

    @Provides
    fun provideAudioDao(db: AudioDataBase): AudioDao = db.db()

    @Singleton
    @Provides
    fun provideCameraDataBase(@ApplicationContext context: Context): CameraDataBase =
        Room.databaseBuilder(context, CameraDataBase::class.java, "ca.db").build()

    @Provides
    fun provideCameraDao(db: CameraDataBase): CameraDao = db.db()
}
