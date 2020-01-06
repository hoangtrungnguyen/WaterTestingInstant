package com.hackathon.watertestinginstant.ui.main

import android.bluetooth.BluetoothDevice
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.hackathon.watertestinginstant.appl.WaterTestingApplication
import com.hackathon.watertestinginstant.bluetooth.ConnectStatus
import com.hackathon.watertestinginstant.bluetooth.SerialSocket

class MainViewModel(val application: WaterTestingApplication) :
    AndroidViewModel(application) {

    private val _status = MutableLiveData<String>()
    val status: LiveData<String> = _status

    private val _state = MutableLiveData<ConnectStatus>()
    val state: LiveData<ConnectStatus> = _state

    private val _data = MutableLiveData<Result<ByteArray>>()
    val data: LiveData<Result<ByteArray>> = _data
    fun postResult(data: Result<ByteArray>) {
        _data.postValue(data)
    }

    var serialSocket: SerialSocket? = null

    init {

    }

    fun connect(device: BluetoothDevice) {
        try {
            serialSocket = SerialSocket()
            _status.postValue("connecting... ")
            _state.postValue(ConnectStatus.Pending)
            serialSocket?.connect(application, this, device)
        } catch (e: Exception) {
            _status.postValue(e.message)
        }

    }

    fun disconnect() {
        serialSocket?.disconnect()
        _state.postValue(ConnectStatus.False)
    }


    fun postStatus(status: String) {
        _status.postValue(status)
    }
}