package com.hackathon.watertestinginstant.ui.main.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hackathon.watertestinginstant.api.Client
import com.hackathon.watertestinginstant.api.MarsProperty
import com.hackathon.watertestinginstant.api.WaterApi
import com.hackathon.watertestinginstant.data.Result
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout

class ProfileViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is notifications Fragment"
    }
    val text: LiveData<String> = _text

    val result = MutableLiveData<Result<List<MarsProperty>>>()


    fun syncApi() {
        viewModelScope.launch {
            withTimeout(3000) {
                try {
                    val value = WaterApi.retrofitService.getPropertiesAsync().await()

                    result.postValue(Result.Success(value))
                } catch (e: Exception) {
                    result.postValue(Result.Error(e))
                }
            }
        }
    }

}