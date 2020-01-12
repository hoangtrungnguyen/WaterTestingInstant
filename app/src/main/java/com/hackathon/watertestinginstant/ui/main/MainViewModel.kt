package com.hackathon.watertestinginstant.ui.main

import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.iid.FirebaseInstanceId
import com.hackathon.watertestinginstant.appl.FirebaseMessageService
import com.hackathon.watertestinginstant.appl.WaterTestingApplication
import com.hackathon.watertestinginstant.data.model.WaterData
import com.hackathon.watertestinginstant.service.database.WaterDao
import com.hackathon.watertestinginstant.service.database.userId
import com.hackathon.watertestinginstant.service.network.WaterApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainViewModel(val application: WaterTestingApplication, val waterDao: WaterDao) :
    AndroidViewModel(application) {
    private val TAG = "MainViewModel"

    private val _user = MutableLiveData<FirebaseUser>()
    val user: LiveData<FirebaseUser> = _user

    val waterData = waterDao.getAll()

    val store = WaterTestingApplication.fireBaseFireStore

    val dirtyLocation = MutableLiveData<List<LatLng>>()

    init {
        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w(TAG, "getInstanceId failed", task.exception)
                    return@OnCompleteListener
                }

                // Get new Instance ID token
                val token = task.result?.token

                // Log and toast
                val msg = "Token $token"
                Log.d(TAG, msg)
            })

        _user.postValue(WaterTestingApplication.mAuth.currentUser)

        getDeviceID(application)

    }

    fun getDeviceID(context: Context) {


        WaterTestingApplication.mAuth.currentUser?.getIdToken(true)?.addOnCompleteListener {task ->
            if (task.isSuccessful) {
                val idToken = task.result?.token
                // Send token to your backend via HTTPS
                Log.d(TAG,"$idToken")
            } else {
                // Handle error -> task.getException();
                Log.d(TAG,task.exception?.message ?: "NULL")
            }
        }

        val deviceAppUID = FirebaseInstanceId.getInstance().id
        deviceAppUID.let {
            val deviceID = FirebaseMessageService.getToken(context)
            Log.d(TAG, "DeviceId $deviceAppUID")
        }

        WaterApi.service.sendDeviceId(deviceAppUID).enqueue(object : Callback<String> {
            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.d(TAG, "Send Sucess ${t.message}")
            }

            override fun onResponse(call: Call<String>, response: Response<String>) {
                Log.d(TAG, "Send Sucess ${response.body()}")
            }

        })
    }



}