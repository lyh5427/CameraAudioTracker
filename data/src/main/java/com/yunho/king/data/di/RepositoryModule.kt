package com.yunho.king.data.di

import com.yunho.king.data.repository.RepositoryImpl
import com.yunho.king.domain.repository.RepositorySource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Singleton
    @Binds
    abstract fun bindRepository(impl: RepositoryImpl): RepositorySource
}
