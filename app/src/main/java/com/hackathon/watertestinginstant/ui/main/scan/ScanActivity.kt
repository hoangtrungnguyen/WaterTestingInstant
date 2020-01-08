package com.hackathon.watertestinginstant.ui.main.scan

import android.app.ListActivity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.companion.BluetoothLeDeviceFilter
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.PersistableBundle
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.hackathon.watertestinginstant.R
import com.hackathon.watertestinginstant.ui.main.profile.LeDeviceListAdapter
import kotlinx.android.synthetic.main.activity_scan.*
import java.util.zip.Inflater

private const val SCAN_PERIOD: Long = 10000

/**
 * Activity for scanning and displaying available BLE devices.
 */
@Suppress("DEPRECATION")
class ScanActivity(
    private val bluetoothAdapter: BluetoothAdapter,
    private val handler: Handler
) : ListActivity() {

    companion object{
        fun newInstance(context:Context){
            val intent = Intent(context, ScanActivity::class.java)
            context.startActivity(intent)
        }
    }

    private var mScanning: Boolean = false
    var bluetoothGatt: BluetoothGatt? = null

    private val leDeviceListAdapter = LeDeviceListAdapter() {
        bluetoothGatt = it.connectGatt(this, false, object : BluetoothGattCallback() {

        })
    }

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        setContentView(R.layout.activity_scan)
        scan_device.setOnClickListener { scanLeDevice(true) }
        stop_scan_device.setOnClickListener { scanLeDevice(false) }
    }


     fun scanLeDevice(enable: Boolean) {
        when (enable) {
            true -> {
                // Stops scanning after a pre-defined scan period.
                handler.postDelayed({
                    mScanning = false
                    bluetoothAdapter.stopLeScan(leScanCallback)
                }, SCAN_PERIOD)
                mScanning = true
                bluetoothAdapter.startLeScan(leScanCallback)
            }
            else -> {
                mScanning = false
                bluetoothAdapter.stopLeScan(leScanCallback)
            }
        }
    }

    private val leScanCallback = BluetoothAdapter.LeScanCallback { device, rssi, scanRecord ->
        runOnUiThread {
            leDeviceListAdapter.addItem(device)
        }
    }
}

