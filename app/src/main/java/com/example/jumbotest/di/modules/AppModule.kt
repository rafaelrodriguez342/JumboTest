package com.example.jumbotest.di.modules

import com.example.jumbotest.ui.image.ImageHandler
import com.example.jumbotest.ui.image.PicassoImageHandler
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
abstract class AppModule {

    companion object {
        @Provides
        @Singleton
        fun providesAsyncDispatcher(): CoroutineDispatcher = Dispatchers.IO
    }

    @Singleton
    @Binds
    abstract fun providesImageHandler(picassoImageHandler: PicassoImageHandler): ImageHandler
}
