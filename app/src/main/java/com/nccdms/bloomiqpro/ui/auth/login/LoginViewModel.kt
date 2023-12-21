package com.nccdms.bloomiqpro.ui.auth.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nccdms.bloomiqpro.data.UserRepository
import com.nccdms.bloomiqpro.data.remote.ApiResult
import com.nccdms.bloomiqpro.data.remote.response.LoginResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: UserRepository
):ViewModel(){
    private val _loginResult = MutableStateFlow<ApiResult<LoginResponse>>(ApiResult.Loading)
    val loginResult: StateFlow<ApiResult<LoginResponse>> get() = _loginResult

    fun login(email: String, password: String) {
        viewModelScope.launch {
            try {
                _loginResult.value = ApiResult.Loading

                val result = repository.login(email, password)
                _loginResult.value = result
            } catch (e: Exception) {
                _loginResult.value = ApiResult.Error("An error occurred: ${e.message}")
            }
        }

    }

}