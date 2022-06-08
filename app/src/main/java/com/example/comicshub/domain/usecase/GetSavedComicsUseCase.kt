package com.example.comicshub.domain.usecase

import com.example.comicshub.data.model.APIResponse
import com.example.comicshub.domain.repository.ComicsRepository
import kotlinx.coroutines.flow.Flow

class GetSavedComicsUseCase(private val comicsRepository: ComicsRepository) {
     fun execute() : Flow<List<APIResponse>>{
        return comicsRepository.getSavedComics()
    }
}