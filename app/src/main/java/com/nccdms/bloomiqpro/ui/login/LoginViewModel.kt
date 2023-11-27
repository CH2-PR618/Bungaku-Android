package com.nccdms.bloomiqpro.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nccdms.bloomiqpro.data.local.pref.UserPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val preferences: UserPreferences
):ViewModel(){
    fun setToken(token:String) = viewModelScope.launch {
        preferences.setToken(token)
    }
}