package com.example.jumbotest.di.modules

import com.example.jumbotest.data.network.RetrofitApiClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {

    companion object {
        const val BASE_URL =
            "https://raw.githubusercontent.com/jumbo-tech-campus/AndroidAssignment/main/"
    }

    @Provides
    @Singleton
    fun provideApiClient(): RetrofitApiClient {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(RetrofitApiClient::class.java)
    }
}
