package com.example.comicshub.presentation.di

import com.example.comicshub.data.remote.ComicsAPIService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetModule {
    @Singleton
    @Provides
    fun provideRetrofitInstance() : Retrofit{
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://xkcd.com/")
            .build()
    }

    @Singleton
    @Provides
    fun provideComicsAPIService(retrofit: Retrofit) : ComicsAPIService{
        return retrofit.create(ComicsAPIService::class.java)
    }
}