package com.yunho.king.data.di

import com.yunho.king.data.local.LocalDataSource
import com.yunho.king.data.local.LocalDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class LocalDataSourceModule {
    @Singleton
    @Binds
    abstract fun bindLocalDataSource(impl: LocalDataSourceImpl): LocalDataSource
}

