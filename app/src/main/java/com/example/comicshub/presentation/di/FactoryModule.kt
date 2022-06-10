package com.example.comicshub.presentation.di

import android.app.Application
import com.example.comicshub.domain.usecase.GetComicDataUseCase
import com.example.comicshub.domain.usecase.GetNewestComicDataUseCase
import com.example.comicshub.presentation.viewmodel.ComicsViewModelFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class FactoryModule {

    @Singleton
    @Provides
    fun provideComicsViewModelFactory(
        application: Application,
        getComicsDataUseCase: GetComicDataUseCase,
        getNewestComicDataUseCase: GetNewestComicDataUseCase
    ): ComicsViewModelFactory{
        return ComicsViewModelFactory(getComicsDataUseCase, getNewestComicDataUseCase,application)
    }
}