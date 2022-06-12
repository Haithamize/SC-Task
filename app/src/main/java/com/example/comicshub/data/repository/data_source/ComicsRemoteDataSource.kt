package com.example.comicshub.data.repository.data_source

import com.example.comicshub.data.model.APIResponse
import retrofit2.Response

interface ComicsRemoteDataSource {
    suspend fun getComicData (comicNumber : Int?) : Response<APIResponse>
    suspend fun getNewestComicData () : Response<APIResponse>
    fun getNewestComicDataForNotification () : Response<APIResponse>
}