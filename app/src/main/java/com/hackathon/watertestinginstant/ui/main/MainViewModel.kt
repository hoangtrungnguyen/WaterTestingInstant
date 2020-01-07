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
import com.google.firebase.iid.FirebaseInstanceId
import com.hackathon.watertestinginstant.appl.WaterTestingApplication
import com.hackathon.watertestinginstant.bluetooth.ConnectStatus
import com.hackathon.watertestinginstant.bluetooth.SerialSocket
import com.hackathon.watertestinginstant.data.model.WaterData
import com.hackathon.watertestinginstant.database.WaterDao
import com.hackathon.watertestinginstant.ui.util.useCancellably
import kotlinx.coroutines.*
import java.io.Closeable
import java.net.ServerSocket
import java.time.LocalDate
import kotlin.coroutines.resume

class MainViewModel(val application: WaterTestingApplication, val waterDao: WaterDao) :
    AndroidViewModel(application) {
    private val TAG = this.javaClass.simpleName

    private val _status = MutableLiveData<String>()
    val status: LiveData<String> = _status

    private val _data = MutableLiveData<Result<ByteArray>>()
    val data: LiveData<Result<ByteArray>> = _data

    val waterData = waterDao.getAll()


    val waterDataString = Transformations.map(waterData) {
        it.map {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                "The quality of the water:\nPH: ${it.PH}\nTurbidity: ${it.Turbidity}\nTDS${it.TDS}\n" +
                        "at ${LocalDate.ofEpochDay(it.time)}"
            } else {
                "The quality of the water:\nPH: ${it.PH}\nTurbidity: ${it.Turbidity}\nTDS${it.TDS}\n" +
                        "at ${it.time}"
            }
        }
    }

    var serialSocket: SerialSocket? = null

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
                Toast.makeText(application, msg, Toast.LENGTH_SHORT).show()
            })
    }

    fun postResult(data: Result<ByteArray>) {
        _data.postValue(data)
        saveData(data)
    }

    fun postStatus(status: String) {
        _status.postValue(status)
    }

    fun connect(device: BluetoothDevice) {
        try {
            serialSocket = SerialSocket()
            _status.postValue("Connecting")
            serialSocket?.connect(application, this, device)
        } catch (e: Exception) {
            _status.postValue(e.message)
        }
    }

    fun disconnect() {
        serialSocket?.disconnect()
    }

    fun saveData(data: Result<ByteArray>) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val item = WaterData()
                waterDao.insert(item)
            }
        }
    }


}