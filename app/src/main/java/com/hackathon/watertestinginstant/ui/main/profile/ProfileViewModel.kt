package com.hackathon.watertestinginstant.ui.main.profile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hackathon.watertestinginstant.api.Client
import com.hackathon.watertestinginstant.api.MarsProperty
import com.hackathon.watertestinginstant.api.WaterApi
import com.hackathon.watertestinginstant.appl.WaterTestingApplication
import com.hackathon.watertestinginstant.data.Result
import com.hackathon.watertestinginstant.data.model.WaterData
import com.hackathon.watertestinginstant.database.WaterDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout

class ProfileViewModel(val waterDao: WaterDao) : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is notifications Fragment"
    }
    val text: LiveData<String> = _text

    val result = MutableLiveData<Result<List<MarsProperty>>>()


    fun syncApi() {
        viewModelScope.launch {
            launch {
                try {
                    val value = WaterApi.retrofitService.getPropertiesAsync().await()

                    result.postValue(Result.Success(value))
                } catch (e: Exception) {
                    result.postValue(Result.Error(e))
                }
            }
        }
    }

    fun saveData(/*data: WaterData*/) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val item = WaterData()
                waterDao.insert(item)
            }
        }
    }

}