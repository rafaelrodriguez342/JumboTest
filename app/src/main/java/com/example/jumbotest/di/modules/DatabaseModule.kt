package com.example.jumbotest.di.modules

import android.content.Context
import androidx.room.Room
import com.example.jumbotest.data.database.databases.JumboDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {

    companion object {
        private const val JUMBO_DATABASE_NAME = "jumbo_database"
    }

    @Provides
    @Singleton
    fun providesDatabase(@ApplicationContext context: Context) =
        Room.databaseBuilder(context, JumboDatabase::class.java, JUMBO_DATABASE_NAME).build()

    @Provides
    @Singleton
    fun providesOrderDao(database: JumboDatabase) = database.getOrderDao()

    @Provides
    @Singleton
    fun providesProductsDao(database: JumboDatabase) = database.getProductDao()

}
