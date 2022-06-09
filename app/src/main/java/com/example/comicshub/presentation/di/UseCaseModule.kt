package com.example.comicshub.presentation.di

import com.example.comicshub.domain.repository.ComicsRepository
import com.example.comicshub.domain.usecase.GetComicDataUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class UseCaseModule {

    @Singleton
    @Provides
    fun provideComicDataUseCase(comicsRepository: ComicsRepository) : GetComicDataUseCase{
        return GetComicDataUseCase(comicsRepository)
    }
}