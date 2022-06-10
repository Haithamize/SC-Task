package com.example.comicshub.data.repository.data_source_impl

import com.example.comicshub.data.model.APIResponse
import com.example.comicshub.data.remote.ComicsAPIService
import com.example.comicshub.data.repository.data_source.ComicsRemoteDataSource
import retrofit2.Response

class ComicsRemoteDataSourceImplementation (private val comicsAPIService: ComicsAPIService): ComicsRemoteDataSource {
    override suspend fun getComicData(comicNumber : Int?): Response<APIResponse> {
        return comicsAPIService.getComicData(comicNumber)
    }

    override suspend fun getNewestComicData(): Response<APIResponse> {
        return comicsAPIService.getNewestComicData()
    }
}