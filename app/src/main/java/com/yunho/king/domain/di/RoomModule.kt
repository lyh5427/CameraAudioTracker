package com.yunho.king.domain.di


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
import javax.inject.Qualifier
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class RoomCreateModule {
    @Singleton
    @Provides
    fun providesAudioDataBase(@ApplicationContext context: Context): AudioDataBase {
        val db = Room.databaseBuilder(
            context,
            AudioDataBase::class.java,
            "audio.db").build()

        return db
    }

    @Singleton
    @Provides
    fun providesAudioDao(audioDB: AudioDataBase): AudioDao = audioDB.db()

    @Singleton
    @Provides
    fun providesCameraDataBase(@ApplicationContext context: Context): CameraDataBase {
        val db = Room.databaseBuilder(
            context,
            CameraDataBase::class.java,
            "camera.db").build()

        return db
    }

    @Singleton
    @Provides
    fun provides(cameraDB: CameraDataBase): CameraDao = cameraDB.db()

}