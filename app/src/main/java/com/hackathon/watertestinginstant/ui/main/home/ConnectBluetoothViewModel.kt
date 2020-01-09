package com.hackathon.watertestinginstant.ui.main.home

import android.bluetooth.*
import androidx.lifecycle.*
import com.hackathon.watertestinginstant.appl.WaterTestingApplication
import com.hackathon.watertestinginstant.appl.WaterTestingApplication.Companion.application
import com.hackathon.watertestinginstant.bluetooth.ACTION_DATA_AVAILABLE
import com.hackathon.watertestinginstant.bluetooth.SerialSocket
import com.hackathon.watertestinginstant.data.model.WaterData
import com.hackathon.watertestinginstant.database.WaterDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.random.Random


class ConnectBluetoothViewModel(val waterDao: WaterDao, application: WaterTestingApplication) :
    AndroidViewModel(application) /*, SerialListener*/ {

    var bluetoothGatt: BluetoothGatt? = null

    private val _status = MutableLiveData<String>()
    val status: LiveData<String> = _status

    private val _data = MutableLiveData<Result<ByteArray>>()
    val data: LiveData<Result<ByteArray>> = _data

    val waterData = waterDao.getAll()

    var serialSocket: SerialSocket? = null

    init {

    }


    fun latest(): LiveData<Double> {
        return Transformations.map(waterDao.getLatest()) {
            Random.nextDouble(5.0, 100.0)
        }
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

    fun getLatest() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {

            }
        }
    }

    fun saveData(data: Result<ByteArray>) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val item = WaterData(TDS = Random(15).nextDouble(), PH = Random(7).nextDouble())
                waterDao.insert(item)
            }
        }
    }


    fun connectAndReceive(macAddress: String) {
        val blu = BluetoothAdapter.getDefaultAdapter()
        val device = blu.bondedDevices.first { it.address == macAddress }

        bluetoothGatt = device.connectGatt(application, false, object :
            BluetoothGattCallback() {
            override fun onCharacteristicRead(
                gatt: BluetoothGatt?,
                characteristic: BluetoothGattCharacteristic?,
                status: Int
            ) {
//                data.postValue(characteristic?.value?.toHex())
            }

            override fun onConnectionStateChange(
                gatt: BluetoothGatt?,
                statusble: Int,
                newState: Int
            ) {
                when (newState) {
//                    BluetoothGatt.STATE_CONNECTING -> status.postValue("Connecting...")
//                    BluetoothGatt.STATE_CONNECTED -> status.postValue("Connected")
//                    BluetoothGatt.STATE_DISCONNECTED -> status.postValue("Disconnected")
//                    BluetoothGatt.STATE_DISCONNECTING -> status.postValue("Disconnecting...")

                }
                val s = ACTION_DATA_AVAILABLE
            }

        })

    }


}