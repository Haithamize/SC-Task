package com.example.comicshub.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.comicshub.domain.usecase.GetComicDataUseCase
import java.lang.IllegalArgumentException

class ComicsViewModelFactory (
    private val getComicDataUseCase: GetComicDataUseCase,
    private val app : Application
        ): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(ComicsViewModel::class.java)){
            return ComicsViewModel(getComicDataUseCase,app) as T
        }
        throw IllegalArgumentException("wrong view model class")
    }
}