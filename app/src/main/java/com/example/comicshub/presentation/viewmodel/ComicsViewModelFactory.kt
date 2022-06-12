package com.example.comicshub.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.comicshub.domain.usecase.*
import java.lang.IllegalArgumentException

class ComicsViewModelFactory (
    private val getComicDataUseCase: GetComicDataUseCase,
    private val getNewestComicDataUseCase: GetNewestComicDataUseCase,
    private val saveComicUseCase: SaveComicUseCase,
    private val getSavedComicsUseCase: GetSavedComicsUseCase,
    private val notifyUserUseCase: NotifyUserUseCase,
    private val app : Application
        ): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(ComicsViewModel::class.java)){
            return ComicsViewModel(getComicDataUseCase, getNewestComicDataUseCase,saveComicUseCase,getSavedComicsUseCase,notifyUserUseCase,app) as T
        }
        throw IllegalArgumentException("wrong view model class")
    }
}