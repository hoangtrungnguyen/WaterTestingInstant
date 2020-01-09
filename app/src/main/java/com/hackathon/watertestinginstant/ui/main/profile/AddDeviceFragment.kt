package com.hackathon.watertestinginstant.ui.main.profile

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.hackathon.watertestinginstant.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.add_device_fragment.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout


@Suppress("DEPRECATION")
class AddDeviceFragment : Fragment() {

    companion object {
        fun newInstance() = AddDeviceFragment()
    }

    private var mScanning: Boolean = false
    var bluetoothGatt: BluetoothGatt? = null

    private lateinit var bluetoothAdapter: BluetoothAdapter

    private lateinit var viewModel: AddDeviceViewModel

    private val leDeviceListAdapter = LeDeviceListAdapter() {
//        bluetoothGatt = it.connectGatt(context!!, false, object : BluetoothGattCallback() {
//
//        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(com.hackathon.watertestinginstant.R.layout.add_device_fragment, container, false)
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
    }

    override fun onStop() {
        super.onStop()
        val animation = AnimationUtils.loadAnimation(context, R.anim.bottom_anim_unhide)
        animation.start()
        val bt = activity!!.findViewById<BottomNavigationView>(R.id.bottom_nav)
        bt.startAnimation(animation)
    }

    fun scanLeDevice(enable: Boolean) {
        when (enable) {
            true -> {
                // Stops scanning after a pre-defined scan period.
                CoroutineScope(Dispatchers.IO).launch {
                    withTimeout(5000)
                    {
                        mScanning = false
                        bluetoothAdapter.stopLeScan(leScanCallback)
                    }
                }
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
        CoroutineScope(Dispatchers.IO).launch {
            leDeviceListAdapter.addItem(device)
        }
    }

}


class LeDeviceListAdapter(val listener: (item: BluetoothDevice) -> Unit) :
    RecyclerView.Adapter<LeDeviceListAdapter.ViewHolder>() {
    private val _data = mutableListOf<BluetoothDevice>()

    fun updateData(data: List<BluetoothDevice>) {
        _data.clear()
        _data.addAll(data)
        notifyDataSetChanged()
    }

    fun addItem(item: BluetoothDevice) {
        _data.add(item)
        notifyDataSetChanged()

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater
            .inflate(android.R.layout.simple_list_item_2, parent, false) as TextView
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
