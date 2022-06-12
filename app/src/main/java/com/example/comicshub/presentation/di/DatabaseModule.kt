package com.example.comicshub.presentation.di

import android.app.Application
import androidx.room.Room
import com.example.comicshub.data.local.ComicDAO
import com.example.comicshub.data.local.ComicDatabase
import com.example.comicshub.data.local.DATABASE_NAME
import com.example.comicshub.data.model.APIResponse
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Singleton
    @Provides
    fun provideComicDatabase(application: Application) : ComicDatabase{
        return  Room.databaseBuilder(application, ComicDatabase::class.java, DATABASE_NAME)
            .fallbackToDestructiveMigration() //this will allow room to destructively replace db tables if migrations that would migrate old database schemas to the latest schema version are not found
            .build()
    }

    @Singleton
    @Provides
    fun provideComicsDao(comicDatabase: ComicDatabase) : ComicDAO{
        return comicDatabase.getComicsDao()
    }
}