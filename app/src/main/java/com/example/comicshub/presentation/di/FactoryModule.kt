package com.example.comicshub.presentation.di

import android.app.Application
import com.example.comicshub.domain.usecase.*
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
        saveComicUseCase: SaveComicUseCase,
        getSavedComicsUseCase: GetSavedComicsUseCase,
        getNewestComicDataUseCase: GetNewestComicDataUseCase,
        notifyUserUseCase: NotifyUserUseCase,
    ): ComicsViewModelFactory {
        return ComicsViewModelFactory(
            getComicsDataUseCase,
            getNewestComicDataUseCase,
            saveComicUseCase,
            getSavedComicsUseCase,
            notifyUserUseCase,
            application
        )
    }
}