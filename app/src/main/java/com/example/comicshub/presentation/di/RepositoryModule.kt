package com.example.comicshub.presentation.di

import com.example.comicshub.data.repository.ComicsRepositoryImplementation
import com.example.comicshub.data.repository.data_source.ComicsLocalDataSource
import com.example.comicshub.data.repository.data_source.ComicsRemoteDataSource
import com.example.comicshub.domain.repository.ComicsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Singleton
    @Provides
    fun provideComicsRepository(
        comicsRemoteDataSource: ComicsRemoteDataSource,
        comicsLocalDataSource: ComicsLocalDataSource
    ) : ComicsRepository{
        return ComicsRepositoryImplementation(comicsRemoteDataSource,comicsLocalDataSource)
    }
}