package com.example.comicshub.data.repository

import com.example.comicshub.data.model.APIResponse
import com.example.comicshub.data.repository.data_source.ComicsLocalDataSource
import com.example.comicshub.data.repository.data_source.ComicsRemoteDataSource
import com.example.comicshub.data.util.Resource
import com.example.comicshub.domain.repository.ComicsRepository
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

class ComicsRepositoryImplementation (
    private val comicsRemoteDataSource: ComicsRemoteDataSource,
    private val comicsLocalDataSource: ComicsLocalDataSource
    ): ComicsRepository {
    override suspend fun getComicData(comicNumber : Int?): Resource<APIResponse> {
        return responseToResult(comicsRemoteDataSource.getComicData(comicNumber))
    }

    override suspend fun getNewestComicData(): Resource<APIResponse> {
        return responseToResult(comicsRemoteDataSource.getNewestComicData())
    }


    override suspend fun saveComicData(apiResponse: APIResponse) {
        return comicsLocalDataSource.saveComicDataToDatabase(apiResponse)
    }

    override fun getSavedComics(): Flow<List<APIResponse>> {
        return comicsLocalDataSource.getAllSavedComics()
    }

    override fun getNewestComicDataForNotification(): Resource<APIResponse> {
        return responseToResult(comicsRemoteDataSource.getNewestComicDataForNotification())
    }


    private fun responseToResult(response: Response<APIResponse>) : Resource<APIResponse>{
        if(response.isSuccessful){
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())
    }
}