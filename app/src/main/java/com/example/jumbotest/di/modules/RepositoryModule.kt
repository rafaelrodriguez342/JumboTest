package com.example.jumbotest.di.modules

import com.example.jumbotest.data.database.RoomCartRepository
import com.example.jumbotest.data.database.RoomProductRepository
import com.example.jumbotest.data.network.ApiClientProductRepository
import com.example.jumbotest.data.repositories.*
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
abstract class RepositoryModule {

    @Singleton
    @Binds
    abstract fun providesProductRepository(apiClientProductRepository: ApiClientProductRepository): ProductRepository

    @Singleton
    @Binds
    abstract fun providesDatabaseProductRepository(databaseProductRepository: RoomProductRepository): ProductDatabaseRepository

    @Singleton
    @Binds
    abstract fun providesCartRepository(roomCartRepository: RoomCartRepository): CartRepository

}
