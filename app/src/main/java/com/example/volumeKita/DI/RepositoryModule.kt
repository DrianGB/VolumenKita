package com.example.volumeKita.DI

import com.example.volumeKita.Domain.Repository.MyRepository
import com.example.volumeKita.Repository.MyRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun Repository(
        repoImpl: MyRepositoryImpl
    ): MyRepository
}