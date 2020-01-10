package com.hackathon.watertestinginstant.ui.main

import android.util.Log
import androidx.lifecycle.*
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.iid.FirebaseInstanceId
import com.hackathon.watertestinginstant.appl.WaterTestingApplication
import com.hackathon.watertestinginstant.database.WaterDao

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