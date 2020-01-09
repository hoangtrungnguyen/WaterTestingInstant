package com.hackathon.watertestinginstant.ui.main.home


import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.method.ScrollingMovementMethod
import android.text.style.ForegroundColorSpan
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.navArgs

import com.hackathon.watertestinginstant.R
import com.hackathon.watertestinginstant.appl.ViewModelFactory
import com.hackathon.watertestinginstant.database.AppDataBase
import com.hackathon.watertestinginstant.ui.main.MainViewModel
import com.hackathon.watertestinginstant.ui.util.showSnackbarShort
import com.hackathon.watertestinginstant.ui.util.toHex
import kotlinx.android.synthetic.main.fragment_terminal.*

/**
 * A simple [Fragment] subclass.
 */
@Suppress("DEPRECATION")
class TerminalFragment : Fragment() {
    companion object {
        val MAC_ADDRESS = "macAddress"
    }

    var macAddress: String? = null

    private lateinit var viewModel: MainViewModel
    private lateinit var viewModelConnect: ConnectBluetoothViewModel

    private lateinit var device: BluetoothDevice

    val args: TerminalFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        retainInstance = true
        macAddress = args.macAddress
        val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        device = bluetoothAdapter.getRemoteDevice(macAddress)

        try {
            viewModel = activity?.run {
                ViewModelProviders.of(
                    this,
                    ViewModelFactory(AppDataBase.getInstance(context!!).waterDao())
                )[MainViewModel::class.java]
            } ?: throw Exception("Invalid Activity")
        } catch (e: Exception) {
            activity?.showSnackbarShort(e.toString())
        }

        viewModelConnect = ViewModelProviders.of(
            activity!!,
            ViewModelFactory(AppDataBase.getInstance(context!!).waterDao())
        )[ConnectBluetoothViewModel::class.java]

//        macAddress?.let { viewModelConnect.connectAndReceive(it) }
        viewModelConnect.connect(device)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_terminal, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        receive_text.setTextColor(resources.getColor(R.color.colorRecieveText))
        receive_text.movementMethod = ScrollingMovementMethod.getInstance()
        send_btn.setOnClickListener {

        }

//        viewModel.connect(device)
    }

    private fun initView() {


        viewModelConnect.status.observe(viewLifecycleOwner, Observer {
            val spn = SpannableStringBuilder(it + '\n')
            spn.setSpan(
                ForegroundColorSpan(resources.getColor(R.color.colorStatusText)),
                0, spn.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            receive_text.append(spn)
            receive_text.append("\n")
        })

        viewModelConnect.data.observe(viewLifecycleOwner, Observer {
            receive_text.append("$it\n")
        })
    }
}
