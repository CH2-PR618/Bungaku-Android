package com.nccdms.bloomiqpro

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.nccdms.bloomiqpro.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding :ActivityMainBinding
    private lateinit var splashScreen: SplashScreen
    private lateinit var navController: NavController
    private val viewmodel:MainViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        splashScreen = installSplashScreen()
        splashScreen.setKeepOnScreenCondition{true}
        setContentView(binding.root)

        navController =
            (supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment).navController

        setStartDestination()
    }

    private fun setStartDestination() {
        viewmodel.token.observe(this){
            navController.navInflater.inflate(R.navigation.main_nav).apply {
                setStartDestination(if (it.isNotEmpty()) R.id.homeFragment else R.id.loginFragment)
                navController.graph = this
                splashScreen.setKeepOnScreenCondition{false}
            }
        }
    }

    override fun onPause() {
        super.onPause()
        viewmodel.token.removeObservers(this)
    }

    override fun onSupportNavigateUp(): Boolean {
        navController.navigateUp()
        return true
    }
}