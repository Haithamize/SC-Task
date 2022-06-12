package com.example.comicshub.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.comicshub.data.model.APIResponse
import kotlinx.coroutines.flow.Flow

@Dao
interface ComicDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(apiResponse: APIResponse)


    @Query("SELECT * FROM TABLE_NAME")
    fun getAllSavedComics() : Flow<List<APIResponse>>
}