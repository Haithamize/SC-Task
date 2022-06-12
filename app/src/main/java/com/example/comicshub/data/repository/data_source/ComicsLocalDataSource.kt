package com.example.comicshub.data.repository.data_source

import com.example.comicshub.data.model.APIResponse
import kotlinx.coroutines.flow.Flow

interface ComicsLocalDataSource {
    suspend fun saveComicDataToDatabase(apiResponse: APIResponse)

    fun getAllSavedComics() : Flow<List<APIResponse>>
}