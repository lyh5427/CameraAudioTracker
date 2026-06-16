package com.yunho.king.data.di

import com.yunho.king.data.local.InstalledAppScannerImpl
import com.yunho.king.domain.source.InstalledAppScannerSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {

    @Singleton
    @Binds
    abstract fun bindInstalledAppScanner(impl: InstalledAppScannerImpl): InstalledAppScannerSource
}
