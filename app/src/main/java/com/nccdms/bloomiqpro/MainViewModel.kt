package com.nccdms.bloomiqpro

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.nccdms.bloomiqpro.data.local.pref.UserPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    preference:UserPreferences
):ViewModel() {

    val token:LiveData<String> = preference.getToken().asLiveData()
}