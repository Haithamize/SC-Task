package com.example.comicshub.presentation.viewmodel

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.lifecycle.*
import com.example.comicshub.data.model.APIResponse
import com.example.comicshub.data.util.Resource
import com.example.comicshub.domain.usecase.GetComicDataUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

class ComicsViewModel(
    private val getComicDataUseCase: GetComicDataUseCase,
    private val app : Application
    ) : AndroidViewModel(app) {
    private val newestComicPrivate : MutableLiveData<Resource<APIResponse>> = MutableLiveData()
    val newestComicPublic : LiveData<Resource<APIResponse>>
    get() = newestComicPrivate

    fun getNewestComic (comicNumber : Int){
        viewModelScope.launch(Dispatchers.IO) {
            newestComicPrivate.postValue(Resource.Loading()) //using this to make a progress bar for loading

            try {
                if (isNetworkAvailable(app)) {
                    val apiResult = getComicDataUseCase.execute(comicNumber)
                    newestComicPrivate.postValue(apiResult)
                } else {
                    newestComicPrivate.postValue(Resource.Error("Internet is unavailable"))
                }
            }catch (error: Exception){
                newestComicPrivate.postValue(Resource.Error(error.message.toString()))
            }
        }
    }

    //function to check the internet Availability
    private fun isNetworkAvailable(context: Context?): Boolean {
        if (context == null) return false
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                when {
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                        return true
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                        return true
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                        return true
                    }
                }
            }
        } else {
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            if (activeNetworkInfo != null && activeNetworkInfo.isConnected) {
                return true
            }
        }
        return false

    }
}