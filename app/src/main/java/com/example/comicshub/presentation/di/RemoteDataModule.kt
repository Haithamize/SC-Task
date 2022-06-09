package com.example.comicshub.presentation.di

import com.example.comicshub.data.remote.ComicsAPIService
import com.example.comicshub.data.repository.ComicsRepositoryImplementation
import com.example.comicshub.data.repository.data_source.ComicsRemoteDataSource
import com.example.comicshub.data.repository.data_source_impl.ComicsRemoteDataSourceImplementation
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RemoteDataModule {

    @Singleton
    @Provides
    fun provideComicsRemoteDataSource(comicsAPIService: ComicsAPIService) : ComicsRemoteDataSource{
        return ComicsRemoteDataSourceImplementation(comicsAPIService)
    }
}