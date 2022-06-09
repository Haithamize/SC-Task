package com.example.comicshub

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.comicshub.databinding.ActivityMainBinding
import com.example.comicshub.presentation.viewmodel.ComicsViewModel
import com.example.comicshub.presentation.viewmodel.ComicsViewModelFactory
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

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
    }
}