package com.hackathon.watertestinginstant.ui.main.profile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hackathon.watertestinginstant.appl.BASE_URL
import com.hackathon.watertestinginstant.data.model.WaterData
import com.hackathon.watertestinginstant.service.database.WaterDao
import com.hackathon.watertestinginstant.service.network.MarsProperty
import com.hackathon.watertestinginstant.service.network.WaterApi
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.*
import retrofit2.*
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
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

    }

    fun callApi(message: String = "last test") {
        viewModelScope.launch {
            try {
//                To explain a bit calling the suspended function getPosts() pretty much allowed
//                the function to wait for a result before processing
//                the if else condition within the main or UI thread.
                val response = WaterApi.retrofitService.saveTestingData(message)

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        //Do something with response e.g show to the UI.
                        result.postValue(Result.success(response.body() ?: "NULL"))
                    } else {
                        result.postValue(Result.failure(java.lang.Exception("Fail with code ${response.code()}")))
                    }
                }
            } catch (e: HttpException) {
                result.postValue(Result.failure(e))
            } catch (e: Throwable) {
                result.postValue(Result.failure(e))
            }
        }
    }

    fun syncData(message: String = "testMessage") {
        val builder = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(ScalarsConverterFactory.create())

        val retrofit = builder.build()
        val client = retrofit.create(ApiClient::class.java)
        client.sample("Test").enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                Log.d("2222222nor", call.request().toString())
                result.postValue(kotlin.Result.success(response.body() ?: "NULL"))
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                result.postValue(kotlin.Result.failure(t))
            }
        })

    }

    fun maintain() {
        /**
         * Build the Moshi object that Retrofit will be using, making sure to add the Kotlin adapter for
         * full Kotlin compatibility.
         */
        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        val builder = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .addConverterFactory(MoshiConverterFactory.create())

        val retrofit: Retrofit = builder.build()

        val client = retrofit.create(ApiClient::class.java)
        viewModelScope.launch(Dispatchers.IO) {
            val value = client.sampleKt("Test")
            try {
                Log.d("222222222222", value.request().toString())
                val json = value.await()
                result.postValue(Result.success(json))
            } catch (e: java.lang.Exception) {
                result.postValue(Result.failure(e))
            }
        }
    }

    interface ApiClient {
        @POST("save")
        fun sample(@Body message: String): Call<String>

        @POST("save")
        fun sampleKt(@Body message: String): Call<String>

        @GET("realestate")
        fun getMars(): Call<String>

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
                val item =
                    WaterData(TDS = Random(15).nextDouble(), PH = Random(7).nextDouble())
                waterDao.insert(item)
            }
        }
    }

    override fun onCleared() {
        dbScope.cancel()
        super.onCleared()
    }
}