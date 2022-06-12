package com.example.comicshub.presentation.di

import com.example.comicshub.data.local.ComicDAO
import com.example.comicshub.data.repository.data_source.ComicsLocalDataSource
import com.example.comicshub.data.repository.data_source_impl.ComicsLocalDataSourceImplementation
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class LocalDataSourceModule {

    @Singleton
    @Provides
    fun provideLocalDataSource(comicsDao : ComicDAO) : ComicsLocalDataSource{
        return ComicsLocalDataSourceImplementation(comicsDao)
    }
}