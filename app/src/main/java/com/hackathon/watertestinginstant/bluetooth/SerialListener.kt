package com.hackathon.watertestinginstant.bluetooth

import android.app.Activity
import android.bluetooth.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.ActivityCompat.startActivityForResult
import com.hackathon.watertestinginstant.appl.WaterTestingApplication


val REQUEST_ENABLE_BT = 9999

class BluetootheManager {

    private val BluetoothAdapter.isDisabled: Boolean
        get() = !isEnabled
    
    private val bluetoothAdapter: BluetoothAdapter? by lazy(LazyThreadSafetyMode.NONE) {
        val bluetoothManager =
            WaterTestingApplication.application.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothManager.adapter
    }

}


// Ensures Bluetooth is available on the device and it is enabled. If not,
// displays a dialog requesting user permission to enable Bluetooth.
fun Activity.checkBluetoothe(bluetoothAdapter: BluetoothAdapter) {
    bluetoothAdapter.takeIf { !it.isEnabled }?.apply {
        val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
        startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT)
    }
}
