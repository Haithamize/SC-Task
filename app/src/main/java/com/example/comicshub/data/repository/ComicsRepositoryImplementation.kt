package com.example.comicshub.data.repository

import com.example.comicshub.data.model.APIResponse
import com.example.comicshub.data.repository.data_source.ComicsRemoteDataSource
import com.example.comicshub.data.util.Resource
import com.example.comicshub.domain.repository.ComicsRepository
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

class ComicsRepositoryImplementation (private val comicsRemoteDataSource: ComicsRemoteDataSource): ComicsRepository {
    override suspend fun getComicData(comicNumber : Int?): Resource<APIResponse> {
        return responseToResult(comicsRemoteDataSource.getComicData(comicNumber))
    }

    override suspend fun getNewestComicData(): Resource<APIResponse> {
        return responseToResult(comicsRemoteDataSource.getNewestComicData())
    }

    override suspend fun getSearchedComicData(searchQuery: Int): Resource<APIResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun saveComicData(apiResponse: APIResponse) {
        TODO("Not yet implemented")
    }

    override fun getSavedComics(): Flow<List<APIResponse>> {
        TODO("Not yet implemented")
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