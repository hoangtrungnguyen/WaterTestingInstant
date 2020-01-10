package com.hackathon.watertestinginstant.ui.main.profile

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.hackathon.watertestinginstant.R
import com.hackathon.watertestinginstant.util.isLocationPermissionGranted
import com.hackathon.watertestinginstant.util.requestLocationPermission
import kotlinx.android.synthetic.main.add_device_fragment.*
import kotlinx.coroutines.*


val REQUEST_ENABLE_BT = 9999

@Suppress("DEPRECATION")
class AddDeviceFragment : Fragment() {

    companion object {
        fun newInstance() = AddDeviceFragment()
    }

    private var mScanning = MutableLiveData<Boolean>()
    var bluetoothGatt: BluetoothGatt? = null

    private lateinit var bluetoothAdapter: BluetoothAdapter

    private lateinit var viewModel: AddDeviceViewModel

    private val leDeviceListAdapter = LeDeviceListAdapter() {
        //        bluetoothGatt = it.connectGatt(context!!, false, object : BluetoothGattCallback() {
//
//        })
    }

    // Create a BroadcastReceiver for ACTION_FOUND.
    private val receiver = object : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {
            val action: String = intent.action ?: return
            when (action) {
                BluetoothDevice.ACTION_FOUND -> {
                    // Discovery has found a device. Get the BluetoothDevice
                    // object and its info from the Intent.
                    val device: BluetoothDevice? =
                        intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                    leDeviceListAdapter.addItem(device)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        mScanning.postValue(false)

        val pairedDevices: Set<BluetoothDevice>? = bluetoothAdapter.bondedDevices
        leDeviceListAdapter.updateData(pairedDevices)

        if (!bluetoothAdapter?.isEnabled) {
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT)
        }

// Register for broadcasts when a device is discovered.
        val filter = IntentFilter(BluetoothDevice.ACTION_FOUND)
        activity?.registerReceiver(receiver, filter)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(
            R.layout.add_device_fragment,
            container,
            false
        )
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(AddDeviceViewModel::class.java)
        // TODO: Use the ViewModel
        scan_device.setOnClickListener { scanLeDevice(true) }
        stop_scan_device.setOnClickListener { scanLeDevice(false) }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val animation = AnimationUtils.loadAnimation(context, R.anim.bottom_anim_hide)
        animation.start()
        val bt = activity!!.findViewById<BottomNavigationView>(R.id.bottom_nav)
        bt.startAnimation(animation)
        devices.adapter = leDeviceListAdapter

        mScanning.observe(this, Observer {
            if (it) status.text = "Scanning ..." else status.text = "Done"
        })
    }

    override fun onStop() {
        super.onStop()
        val animation = AnimationUtils.loadAnimation(context, R.anim.bottom_anim_unhide)
        animation.start()
        val bt = activity!!.findViewById<BottomNavigationView>(R.id.bottom_nav)
        bt.startAnimation(animation)
    }

    fun scanLeDevice(enable: Boolean) {
        if (!context!!.isLocationPermissionGranted()) {
            activity!!.requestLocationPermission()
        }
        mScanning.postValue(true)
        CoroutineScope(Dispatchers.Main).launch {

            when (enable) {
                true -> {
                    // Stops scanning after a pre-defined scan period.
                    CoroutineScope(Dispatchers.IO).launch {
                        withTimeout(5000)
                        {
                            mScanning.postValue(false)
                            bluetoothAdapter.stopLeScan(leScanCallback)
                        }
                    }
                    mScanning.postValue(true)
                    bluetoothAdapter.startLeScan(leScanCallback)
                }
                else -> {
                    mScanning.postValue(false)
                    bluetoothAdapter.stopLeScan(leScanCallback)
                }
            }
        }
    }

    private val leScanCallback = BluetoothAdapter.LeScanCallback { device, rssi, scanRecord ->
        CoroutineScope(Dispatchers.Main).launch {
            leDeviceListAdapter.addUnique(device)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        data?.let {
            if (data.action == BluetoothAdapter.ACTION_REQUEST_ENABLE) {
                if (requestCode == REQUEST_ENABLE_BT) {
                    val pairedDevices: Set<BluetoothDevice>? = bluetoothAdapter.bondedDevices
                    leDeviceListAdapter.updateData(pairedDevices)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // Don't forget to unregister the ACTION_FOUND receiver.
        activity?.unregisterReceiver(receiver)
    }
}


class LeDeviceListAdapter(val listener: (item: BluetoothDevice) -> Unit) :
    RecyclerView.Adapter<LeDeviceListAdapter.ViewHolder>() {
    private val _data = mutableListOf<BluetoothDevice>()

    fun updateData(data: Collection<BluetoothDevice>?) {
        _data.clear()
        data?.let { _data.addAll(it) }
        notifyDataSetChanged()
    }

    fun addItem(item: BluetoothDevice?) {
        item?.let { _data.add(item) }
        notifyDataSetChanged()
    }

    fun addUnique(item: BluetoothDevice?) {
        item ?: return
        if (_data.find { it.address == item.address } == null) {
            addItem(item)
        } else {
            val i = _data.indexOfFirst { item.address == it.address }
            _data[i] = item
            notifyItemChanged(i)
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater
            .inflate(android.R.layout.simple_list_item_2, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = _data.size


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(_data[position])
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(item: BluetoothDevice) {
            val tv1 = itemView.findViewById<TextView>(android.R.id.text1)
            val tv2 = itemView.findViewById<TextView>(android.R.id.text2)
            tv1.text = item.name
            tv2.text = item.address
        }
    }

}
