package com.nccdms.bloomiqpro.ui.auth.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nccdms.bloomiqpro.data.UserRepository
import com.nccdms.bloomiqpro.data.remote.ApiResult
import com.nccdms.bloomiqpro.data.remote.response.RegisterResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val repository: UserRepository
): ViewModel(){
    private val _registerResult = MutableStateFlow<ApiResult<RegisterResponse>>(ApiResult.Loading)
    val registerResult: StateFlow<ApiResult<RegisterResponse>> get() = _registerResult

    fun register(nama: String,email: String, password: String,confirmPass : String) {
        viewModelScope.launch {
            try {
                _registerResult.value = ApiResult.Loading

                val result = repository.register(email = email, password = password, confpassword = confirmPass, name = nama)
                _registerResult.value = result
            } catch (e: Exception) {
                _registerResult.value = ApiResult.Error("An error occurred: ${e.message}")
            }
        }

    }

}