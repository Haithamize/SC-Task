package com.example.comicshub.domain.repository

import com.example.comicshub.data.model.APIResponse
import com.example.comicshub.data.util.Resource
import kotlinx.coroutines.flow.Flow

interface ComicsRepository {
    //network communication functions
    suspend fun getComicData(comicNumber : Int?) : Resource<APIResponse>
    suspend fun getNewestComicData() : Resource<APIResponse>

    //local database functions
    suspend fun saveComicData(apiResponse: APIResponse)
    fun getSavedComics() : Flow<List<APIResponse>>

    //notifications process
    fun getNewestComicDataForNotification() : Resource<APIResponse>


}