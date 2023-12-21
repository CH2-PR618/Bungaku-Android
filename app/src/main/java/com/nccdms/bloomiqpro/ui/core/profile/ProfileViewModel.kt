package com.nccdms.bloomiqpro.ui.core.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nccdms.bloomiqpro.data.UserRepository
import com.nccdms.bloomiqpro.data.remote.ApiResult
import com.nccdms.bloomiqpro.data.remote.response.LoginResponse
import com.nccdms.bloomiqpro.data.remote.response.LogoutResponse
import com.nccdms.bloomiqpro.data.remote.response.ProfileResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: UserRepository
) : ViewModel() {

    private val _userProfileResult = MutableLiveData<ApiResult<ProfileResponse>>()
    val userProfileResult: LiveData<ApiResult<ProfileResponse>> get() = _userProfileResult

    private val _logoutResult = MutableLiveData<ApiResult<LogoutResponse>>()
    val logoutResult: LiveData<ApiResult<LogoutResponse>> get() = _logoutResult

    fun getUserProfile() {
        viewModelScope.launch {
            val result = repository.getUserProfile()
            _userProfileResult.value = result
        }
    }

    fun logout() {
        viewModelScope.launch {
            try {
                _logoutResult.value = ApiResult.Loading
                val result = repository.logout()
                _logoutResult.value = result
            } catch (e: Exception) {
                _logoutResult.value = ApiResult.Error("An error occurred: ${e.message}")
            }
        }
    }
}