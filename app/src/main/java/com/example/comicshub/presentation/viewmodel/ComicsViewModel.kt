package com.example.comicshub.presentation.viewmodel

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import androidx.lifecycle.*
import com.example.comicshub.data.model.APIResponse
import com.example.comicshub.data.util.Resource
import com.example.comicshub.domain.usecase.GetComicDataUseCase
import com.example.comicshub.domain.usecase.GetNewestComicDataUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception
import java.util.ArrayList

class ComicsViewModel(
    private val getComicDataUseCase: GetComicDataUseCase,
    private val getNewestComicDataUseCase: GetNewestComicDataUseCase,
    private val app : Application
    ) : AndroidViewModel(app) {
    private val comicDataPrivate : MutableLiveData<Resource<APIResponse>> = MutableLiveData()
    val comicDataPublic : LiveData<Resource<APIResponse>>
    get() = comicDataPrivate

    //list of random comics data
     val incomingComicsList = ArrayList<APIResponse>()

    fun getSelectedComic (comicNumber : Int?){
        viewModelScope.launch(Dispatchers.IO) {
            comicDataPrivate.postValue(Resource.Loading()) //using this to make a progress bar for loading

            try {
                if (isNetworkAvailable(app)) {
                    val apiResult = getComicDataUseCase.execute(comicNumber)
                    comicDataPrivate.postValue(apiResult)
                } else {
                    comicDataPrivate.postValue(Resource.Error("Internet is unavailable"))
                }
            }catch (error: Exception){
                comicDataPrivate.postValue(Resource.Error(error.message.toString()))
            }
        }
    }

    fun getNewestComic (){
        viewModelScope.launch(Dispatchers.IO) {
            comicDataPrivate.postValue(Resource.Loading()) //using this to make a progress bar for loading

            try {
                if (isNetworkAvailable(app)) {
                    val apiResult = getNewestComicDataUseCase.execute()
                    comicDataPrivate.postValue(apiResult)
                } else {
                    comicDataPrivate.postValue(Resource.Error("Internet is unavailable"))
                }
            }catch (error: Exception){
                comicDataPrivate.postValue(Resource.Error(error.message.toString()))
            }
        }
    }

    fun getRandomListOfComics (){
        for(i in 1..40){
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    if (isNetworkAvailable(app)) {
                        val apiResult = getComicDataUseCase.execute(i)
                        apiResult.data?.let { incomingComicsList.add(it) }
                    }
                }catch (error: Exception){
                    Log.d("ComicsViewModel", error.toString())
                }
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