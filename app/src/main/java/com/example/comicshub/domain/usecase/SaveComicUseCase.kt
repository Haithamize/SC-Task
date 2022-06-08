package com.example.comicshub.domain.usecase

import com.example.comicshub.data.model.APIResponse
import com.example.comicshub.domain.repository.ComicsRepository

class SaveComicUseCase(private val comicsRepository: ComicsRepository) {
    suspend fun execute(apiResponse: APIResponse){
        comicsRepository.saveComicData(apiResponse)
    }
}