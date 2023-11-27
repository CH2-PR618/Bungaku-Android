package com.nccdms.bloomiqpro.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nccdms.bloomiqpro.data.local.pref.UserPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
private val preferences: UserPreferences
):ViewModel() {

    fun clearSession() = viewModelScope.launch {
        preferences.clearSession()
    }
}