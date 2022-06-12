package com.example.comicshub.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.comicshub.data.model.APIResponse


const val DATABASE_NAME = "comics_db"
@Database(
    entities = [APIResponse::class],
    version = 1,
    exportSchema = false
)
abstract class ComicDatabase : RoomDatabase() {
    abstract fun getComicsDao() : ComicDAO
}