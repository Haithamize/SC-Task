package com.example.comicshub.domain.usecase

import com.example.comicshub.data.model.APIResponse
import com.example.comicshub.data.util.Resource
import com.example.comicshub.domain.repository.ComicsRepository

class GetNewestComicDataUseCase(private val comicsRepository: ComicsRepository) {
    suspend fun execute() : Resource<APIResponse> {
        return comicsRepository.getNewestComicData()
    }
}