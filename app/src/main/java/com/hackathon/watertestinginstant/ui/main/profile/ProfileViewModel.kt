package com.hackathon.watertestinginstant.ui.main.profile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hackathon.watertestinginstant.network.MarsProperty
import com.hackathon.watertestinginstant.network.WaterApi
import com.hackathon.watertestinginstant.data.Result
import com.hackathon.watertestinginstant.data.model.WaterData
import com.hackathon.watertestinginstant.database.WaterDao
import kotlinx.coroutines.*
import java.util.*

class ProfileViewModel(val waterDao: WaterDao) : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is notifications Fragment"
    }
    val text: LiveData<String> = _text

    val mars = MutableLiveData<kotlin.Result<List<MarsProperty>>>()

    private var viewModelJob = Job()
    private val dbScope = CoroutineScope(Dispatchers.IO + viewModelJob)

    private val _waterData = MutableLiveData<List<WaterData>>()
    val result = MutableLiveData<kotlin.Result<String>>()


    init {

        getData()
    }

    @ExperimentalCoroutinesApi
    fun callApi() {
        viewModelScope.launch {
            val value = WaterApi.retrofitService.getPropertiesAsync().await()
            mars.postValue(kotlin.Result.success(value))
        }
    }

    fun syncData() {
        getData()
        dbScope.launch {
            _waterData.value?.forEach {
                Log.d("aaaaaa", it.toString())
//                 WaterApi.retrofitService.saveTestingData(it.toString())
//                result.postValue(kotlin.Result.success(res))
            }
        }
    }

    fun getData() {
        try {
            dbScope.launch {
                val l = waterDao.getDataBetween(0, Date().time + 100000L)
                _waterData.postValue(l)
            }
        } catch (e: Exception) {
            result.postValue(kotlin.Result.failure(e))
        }
    }

    fun saveData(/*data: WaterData*/) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val item = WaterData(TDS = Random(15).nextDouble(), PH = Random(7).nextDouble())
                waterDao.insert(item)
            }
        }
    }

    override fun onCleared() {
        dbScope.cancel()
        super.onCleared()
    }
}