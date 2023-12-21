package com.nccdms.bloomiqpro.ui.core.profile.change

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nccdms.bloomiqpro.data.UserRepository
import com.nccdms.bloomiqpro.data.remote.ApiResult
import com.nccdms.bloomiqpro.data.remote.response.UpdateResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChangeViewModel @Inject constructor(
    private val repository: UserRepository
) : ViewModel() {

    private val _updateResult = MutableLiveData<ApiResult<UpdateResponse>>()
    val updateResult: LiveData<ApiResult<UpdateResponse>> get() = _updateResult

    fun updateName(name: String) {
        viewModelScope.launch {
            _updateResult.value = repository.updateName(name)
        }
    }

    fun updateEmail(email: String, currentPassword: String) {
        viewModelScope.launch {
            _updateResult.value = repository.updateEmail(email, currentPassword)
        }
    }

    fun updatePassword(newPassword: String, currentPassword: String) {
        viewModelScope.launch {
            _updateResult.value = repository.updatePassword(newPassword, currentPassword)
        }
    }
}