package com.example.comicshub

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import androidx.work.*
import com.example.comicshub.databinding.ActivityMainBinding
import com.example.comicshub.presentation.viewmodel.ComicsViewModel
import com.example.comicshub.presentation.viewmodel.ComicsViewModelFactory
import com.example.comicshub.workmanager.NotificationWorker
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit
import javax.inject.Inject

const val FIRST_TIME_IN = "FIRST_TIME_IN"
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
     lateinit var viewModelFactory: ComicsViewModelFactory

    private lateinit var binding: ActivityMainBinding
    lateinit var viewModel : ComicsViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       binding = DataBindingUtil.setContentView(this,R.layout.activity_main)

        //setting up the nav architecture component with the bottom navigation
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController


        binding.apply {
            bottomNavView.setupWithNavController(navController)
            viewModel = ViewModelProvider(this@MainActivity,viewModelFactory).get(ComicsViewModel::class.java)
        }

        val sharedPref = this.getPreferences(Context.MODE_PRIVATE) ?: return
        with (sharedPref.edit()) {
            putBoolean(FIRST_TIME_IN, true)
            apply()
        }
    }

}