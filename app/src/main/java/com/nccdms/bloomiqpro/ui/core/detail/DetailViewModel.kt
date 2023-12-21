package com.nccdms.bloomiqpro.ui.core.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nccdms.bloomiqpro.data.UserRepository
import com.nccdms.bloomiqpro.data.remote.ApiResult
import com.nccdms.bloomiqpro.data.remote.response.FlowerDetailResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val repository: UserRepository
): ViewModel() {
    private val _flowerDetailResult = MutableLiveData<ApiResult<FlowerDetailResponse>>()
    val flowerDetailResult: LiveData<ApiResult<FlowerDetailResponse>> get() = _flowerDetailResult

    fun getFlowerDetails(flowerName: String) {
        viewModelScope.launch {
            _flowerDetailResult.value = ApiResult.Loading
            try {
                val result = repository.getFlowerDetails(flowerName)
                _flowerDetailResult.value = result
            } catch (e: Exception) {
                _flowerDetailResult.value = ApiResult.Error("An error occurred: ${e.message}")
            }
        }
    }
}