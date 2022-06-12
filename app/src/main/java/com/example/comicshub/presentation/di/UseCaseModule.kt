package com.example.comicshub.presentation.di

import com.example.comicshub.domain.repository.ComicsRepository
import com.example.comicshub.domain.usecase.*
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

    @Singleton
    @Provides
    fun provideNewestComicDataUseCase(comicsRepository: ComicsRepository) : GetNewestComicDataUseCase{
        return GetNewestComicDataUseCase(comicsRepository)
    }


    @Singleton
    @Provides
    fun provideSaveComicDataUseCase(comicsRepository: ComicsRepository) : SaveComicUseCase{
        return SaveComicUseCase(comicsRepository)
    }



    @Singleton
    @Provides
    fun provideGetSavedComicsUseCase(comicsRepository: ComicsRepository) : GetSavedComicsUseCase{
        return GetSavedComicsUseCase(comicsRepository)
    }

    @Singleton
    @Provides
    fun provideNotifyUsersUseCase(comicsRepository: ComicsRepository) : NotifyUserUseCase{
        return NotifyUserUseCase(comicsRepository)
    }
}