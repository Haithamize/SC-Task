package com.example.comicshub.presentation.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.*
import androidx.work.Data
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.example.comicshub.data.model.APIResponse
import com.example.comicshub.data.util.Resource
import com.example.comicshub.domain.usecase.*
import com.example.comicshub.workmanager.NotificationWorker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.lang.Exception
import java.util.ArrayList
import java.util.concurrent.TimeUnit


const val NEWEST_COMIC_NUMBER  = "NEWEST_COMIC_NUMBER"
class ComicsViewModel(
    private val getComicDataUseCase: GetComicDataUseCase,
    private val getNewestComicDataUseCase: GetNewestComicDataUseCase,
    private val saveComicUseCase: SaveComicUseCase,
    private val getSavedComicsUseCase: GetSavedComicsUseCase,
    private val notifyUserUseCase: NotifyUserUseCase,
    private val app: Application
) : AndroidViewModel(app) {
    private val comicDataPrivate: MutableLiveData<Resource<APIResponse>> = MutableLiveData()
    val comicDataPublic: LiveData<Resource<APIResponse>>
        get() = comicDataPrivate




    fun getSelectedComic(comicNumber: Int?) {
        viewModelScope.launch(Dispatchers.IO) {
            comicDataPrivate.postValue(Resource.Loading()) //using this to make a progress bar for loading

            try {
                if (isNetworkAvailable(app)) {
                    val apiResult = getComicDataUseCase.execute(comicNumber)
                    comicDataPrivate.postValue(apiResult)
                } else {
                    comicDataPrivate.postValue(Resource.Error("Internet is unavailable"))
                }
            } catch (error: Exception) {
                comicDataPrivate.postValue(Resource.Error(error.message.toString()))
            }
        }
    }

    fun getNewestComic() {
        viewModelScope.launch(Dispatchers.IO) {
            comicDataPrivate.postValue(Resource.Loading()) //using this to make a progress bar for loading

            try {
                if (isNetworkAvailable(app)) {
                    val apiResult = getNewestComicDataUseCase.execute()
                    comicDataPrivate.postValue(apiResult)
                } else {
                    comicDataPrivate.postValue(Resource.Error("Internet is unavailable"))
                }
            } catch (error: Exception) {
                comicDataPrivate.postValue(Resource.Error(error.message.toString()))
            }
        }
    }


    //get newest comic number and save it
    fun saveNewestComicNumber(fragmentActivity: FragmentActivity) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                if (isNetworkAvailable(app)) {
                    val apiResult = getNewestComicDataUseCase.execute()
                    val sharedPref = fragmentActivity.getPreferences(Context.MODE_PRIVATE)
                    with (sharedPref?.edit()) {
                        apiResult.data?.num?.let { this?.putInt(NEWEST_COMIC_NUMBER, it) }
                        this?.apply()
                        Log.d("ComicsViewModelNumber", sharedPref.getInt(NEWEST_COMIC_NUMBER,0).toString())

                    }
                }
            } catch (error: Exception) {
                Log.d("ComicsViewModel", error.toString())
            }
        }
    }


    //list of random comics data
    val incomingComicsList = ArrayList<APIResponse>()

    fun getRandomListOfComics() {
        for (i in 1..40) {
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    if (isNetworkAvailable(app)) {
                        val apiResult = getComicDataUseCase.execute(i)
                        apiResult.data?.let { incomingComicsList.add(it) }
                        Log.d("ComicsViewModel", incomingComicsList.toString())
                    }
                } catch (error: Exception) {
                    Log.d("ComicsViewModel", error.toString())
                }
            }
        }
    }

    //function to check the internet Availability
    private fun isNetworkAvailable(context: Context?): Boolean {
        if (context == null) return false
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
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


    //saving comics in database
    fun saveComicDataToDatabase(comic: APIResponse) {
        viewModelScope.launch(Dispatchers.IO) {
            saveComicUseCase.execute(comic)
        }
    }


    //receiving saved comics from database
    fun getAllSavedComics() =
        liveData {
            getSavedComicsUseCase.execute().collect {
                emit(it)
            }
        }

}