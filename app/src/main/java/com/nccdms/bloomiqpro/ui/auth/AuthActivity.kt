package com.nccdms.bloomiqpro.ui.auth

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.nccdms.bloomiqpro.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)
        supportActionBar?.hide()
    }
}