package com.hackathon.watertestinginstant.ui.main

import android.bluetooth.BluetoothDevice
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.*
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.iid.FirebaseInstanceId
import com.hackathon.watertestinginstant.appl.WaterTestingApplication
import com.hackathon.watertestinginstant.bluetooth.ConnectStatus
import com.hackathon.watertestinginstant.bluetooth.SerialSocket
import com.hackathon.watertestinginstant.data.model.WaterData
import com.hackathon.watertestinginstant.database.WaterDao
import com.hackathon.watertestinginstant.ui.util.isInternetConnection
import com.hackathon.watertestinginstant.ui.util.useCancellably
import kotlinx.coroutines.*
import java.io.Closeable
import java.net.ServerSocket
import java.time.LocalDate
import kotlin.coroutines.resume

class MainViewModel(val application: WaterTestingApplication, val waterDao: WaterDao) :
    AndroidViewModel(application) {
    private val TAG = "MainViewModel"

    private val _user = MutableLiveData<FirebaseUser>()
    val user: LiveData<FirebaseUser> = _user


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
//                Toast.makeText(application, msg, Toast.LENGTH_SHORT).show()
            })

        _user.postValue(WaterTestingApplication.mAuth.currentUser)

    }

}