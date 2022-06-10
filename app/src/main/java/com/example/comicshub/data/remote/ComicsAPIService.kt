package com.example.comicshub.data.remote

import com.example.comicshub.data.model.APIResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ComicsAPIService {

    @GET("{comicNumber}/info.0.json")
    suspend fun getComicData (@Path(value = "comicNumber", encoded = true) comicNumber : Int?) : Response<APIResponse>

    @GET("/info.0.json")
    suspend fun getNewestComicData () : Response<APIResponse>
}