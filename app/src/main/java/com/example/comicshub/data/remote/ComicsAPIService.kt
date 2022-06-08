package com.example.comicshub.data.remote

import com.example.comicshub.data.model.APIResponse
import retrofit2.Response
import retrofit2.http.GET

interface ComicsAPIService {

    @GET("/info.0.json")
    suspend fun getComicData () : Response<APIResponse>
}