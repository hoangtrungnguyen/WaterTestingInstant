package com.hackathon.watertestinginstant.ui.main.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hackathon.watertestinginstant.api.Client
import com.hackathon.watertestinginstant.data.Result
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class ProfileViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is notifications Fragment"
    }
    val text: LiveData<String> = _text

    val data = MutableLiveData<Result<String>>()

    fun syncApi() {
        viewModelScope.launch {
            try {
                Client.getProperties().collect { value ->
                    data.postValue(Result.Success(value))
                }
            } catch (e: Exception) {
                data.postValue(Result.Error(e))
            }
        }
    }

}