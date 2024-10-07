package com.example.gowngrad.data.remote.firebase.service.mudule

import com.example.gowngrad.data.remote.firebase.service.AccountService
import com.example.gowngrad.data.remote.firebase.service.LogService
import com.example.gowngrad.data.remote.firebase.service.StorageService
import com.example.gowngrad.data.remote.firebase.service.impl.AccountServiceImpl
import com.example.gowngrad.data.remote.firebase.service.impl.LogServiceImpl
import com.example.gowngrad.data.remote.firebase.service.impl.StorageServiceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class ServiceModule {
    @Binds abstract fun provideAccountService(impl: AccountServiceImpl): AccountService

    @Binds abstract fun provideStorageService(impl: StorageServiceImpl): StorageService

    @Binds abstract fun provideLogService(impl: LogServiceImpl): LogService

}