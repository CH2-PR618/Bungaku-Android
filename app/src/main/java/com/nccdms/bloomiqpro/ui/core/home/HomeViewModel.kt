package com.nccdms.bloomiqpro.ui.core.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nccdms.bloomiqpro.data.UserRepository
import com.nccdms.bloomiqpro.data.dummy.Quote
import com.nccdms.bloomiqpro.data.dummy.QuoteData.dummyData
import com.nccdms.bloomiqpro.data.remote.ApiResult
import com.nccdms.bloomiqpro.data.remote.response.FlowerResponseItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: UserRepository
):ViewModel() {
    private val _flowersResult = MutableLiveData<ApiResult<List<FlowerResponseItem>>>()
    val flowersResult: LiveData<ApiResult<List<FlowerResponseItem>>> get() = _flowersResult

    // LiveData for the unfiltered list of flowers
    private val _flowers = MutableLiveData<List<FlowerResponseItem>>()
    val flowers: LiveData<List<FlowerResponseItem>> get() = _flowers

    private val quotes = dummyData

    // MutableLiveData to hold the current quote
    private val _currentQuote = MutableLiveData<Quote>()
    val currentQuote: LiveData<Quote> get() = _currentQuote

    // Function to set a random quote
    fun setRandomQuote() {
        val randomIndex = quotes.indices.random()
        _currentQuote.value = quotes[randomIndex]
    }

    init {
        // Initialize _flowers LiveData with an empty list
        _flowers.value = emptyList()
    }

    fun getFlowers() {
        viewModelScope.launch {
            _flowersResult.value = ApiResult.Loading
            try {
                val result = repository.getFlowers()
                if (result is ApiResult.Success) {
                    // Save the unfiltered list
                    _flowers.value = result.data!!
                }
                _flowersResult.value = result
            } catch (e: Exception) {
                _flowersResult.value = ApiResult.Error("An error occurred: ${e.message}")
            }
        }
    }
}