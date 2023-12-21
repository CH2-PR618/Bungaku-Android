package com.nccdms.bloomiqpro.ui.core.result

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ResultViewModel:ViewModel() {
    private val _uri = MutableLiveData<Uri?>()
    val uri: LiveData<Uri?> get() = _uri

    fun setUri(uri: Uri) {
        _uri.value = uri
    }

    fun clearUri() {
        _uri.value = null
    }
}