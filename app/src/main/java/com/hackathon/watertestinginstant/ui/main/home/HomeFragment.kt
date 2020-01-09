package com.hackathon.watertestinginstant.ui.main.home

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.ListFragment
import androidx.navigation.fragment.findNavController
import com.hackathon.watertestinginstant.appl.BLUETOOTH_SPP
import com.hackathon.watertestinginstant.ui.main.MainViewModel
import java.util.*
import kotlin.Comparator
import kotlin.collections.ArrayList

@Suppress("DEPRECATION")
class HomeFragment : ListFragment() {

    private var bluetoothAdapter: BluetoothAdapter? = null
    private var listAdapter: ArrayAdapter<BluetoothDevice>? = null

    private var listDevices = ArrayList<BluetoothDevice>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        if (activity!!.packageManager.hasSystemFeature(PackageManager.FEATURE_BLUETOOTH))
            bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()



        listAdapter = object : ArrayAdapter<BluetoothDevice>(context!!, 0, listDevices) {
            @SuppressLint("ViewHolder")
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val device = listDevices[position]
                val view = activity!!.layoutInflater.inflate(
                    android.R.layout.simple_list_item_2,
                    parent,
                    false
                )
                val text1 = view.findViewById<TextView>(android.R.id.text1)
                val text2 = view.findViewById<TextView>(android.R.id.text2)
                text1.text = device.name
                text2.text = device.address
                return view
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setListAdapter(null)
        val header = activity!!.layoutInflater.inflate(
            android.R.layout.browser_link_context_header,
            null,
            false
        )
        listView.addHeaderView(header, null, false)
        setEmptyText("initializing...")
        (listView.emptyView as TextView).textSize = 20f
        setListAdapter(listAdapter)
    }

    override fun onResume() {
        super.onResume()
        if (bluetoothAdapter == null)
            setEmptyText("<bluetooth not supported>")
        else if (bluetoothAdapter?.isEnabled == false) {
            setEmptyText("<bluetooth is disabled>")
        } else
            setEmptyText("<no bluetooth devices found>")
        refresh()
    }


    override fun onListItemClick(l: ListView, v: View, position: Int, id: Long) {
        super.onListItemClick(l, v, position, id)
        val device = listDevices[position - 1]
//        val args = Bundle()
//        args.putString(TerminalFragment.MAC_ADDRESS, device.address)

//        val action = HomeFragmentDirections.actionHomeFragmentToTerminalFragment(device.address)
//        this.findNavController().navigate(action)

//        startActivity(RxConnectActivity.newInstance(context!!, device.address, BLUETOOTH_SPP ))


    }

    internal fun refresh() {
        listDevices.clear()
        if (bluetoothAdapter != null) {
            for (device in bluetoothAdapter!!.bondedDevices)
                if (device.type != BluetoothDevice.DEVICE_TYPE_LE)
                    listDevices.add(device)
        }


        listDevices.sortWith(Comparator { a, b ->
            val aValid = a.name.isNullOrBlank()
            val bValid = b.name.isNullOrBlank()
            if (aValid && bValid) {
            }
            //TODO

            a.address.compareTo(b.address)
        })
        listAdapter?.notifyDataSetChanged()
    }
}