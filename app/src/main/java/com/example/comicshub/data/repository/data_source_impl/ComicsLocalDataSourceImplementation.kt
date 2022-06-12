package com.example.comicshub.data.repository.data_source_impl

import com.example.comicshub.data.local.ComicDAO
import com.example.comicshub.data.model.APIResponse
import com.example.comicshub.data.repository.data_source.ComicsLocalDataSource
import kotlinx.coroutines.flow.Flow

class ComicsLocalDataSourceImplementation (
    private val comicsDAO: ComicDAO
        ): ComicsLocalDataSource {
    override suspend fun saveComicDataToDatabase(apiResponse: APIResponse) {
        comicsDAO.insert(apiResponse)
    }

    override fun getAllSavedComics(): Flow<List<APIResponse>> {
        return comicsDAO.getAllSavedComics()
    }
}