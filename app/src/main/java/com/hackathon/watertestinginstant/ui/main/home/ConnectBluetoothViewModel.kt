package com.hackathon.watertestinginstant.ui.main.home

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothGattCharacteristic
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.ivbaranov.rxbluetooth.BluetoothConnection
import com.hackathon.watertestinginstant.appl.BLUETOOTH_SPP
import com.hackathon.watertestinginstant.appl.WaterTestingApplication
import com.hackathon.watertestinginstant.appl.WaterTestingApplication.Companion.rxBleClient
import com.hackathon.watertestinginstant.bluetooth.ACTION_DATA_AVAILABLE
import com.hackathon.watertestinginstant.bluetooth.ConnectStatus
import com.hackathon.watertestinginstant.ui.util.toHex
import com.polidea.rxandroidble2.RxBleConnection
import com.polidea.rxandroidble2.RxBleDevice
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.launch


class ConnectBluetoothViewModel(val application: WaterTestingApplication) :
    AndroidViewModel(application) /*, SerialListener*/ {
    val data = MutableLiveData<String>()
    val status = MutableLiveData<String>()

    var bluetoothGatt: BluetoothGatt? = null

    init {

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
                data.postValue(characteristic?.value?.toHex())
            }

            override fun onConnectionStateChange(
                gatt: BluetoothGatt?,
                statusble: Int,
                newState: Int
            ) {
                when (newState) {
                    BluetoothGatt.STATE_CONNECTING -> status.postValue("Connecting...")
                    BluetoothGatt.STATE_CONNECTED -> status.postValue("Connected")
                    BluetoothGatt.STATE_DISCONNECTED -> status.postValue("Disconnected")
                    BluetoothGatt.STATE_DISCONNECTING -> status.postValue("Disconnecting...")

                }
                val s = ACTION_DATA_AVAILABLE
            }

        })

    }


}