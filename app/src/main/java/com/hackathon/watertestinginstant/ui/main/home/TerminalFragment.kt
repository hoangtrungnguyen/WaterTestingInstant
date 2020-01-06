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
import com.hackathon.watertestinginstant.ui.main.MainViewModel
import com.hackathon.watertestinginstant.ui.util.toHex
import kotlinx.android.synthetic.main.fragment_terminal.*

/**
 * A simple [Fragment] subclass.
 */
@Suppress("DEPRECATION")
class TerminalFragment : Fragment() {
    companion object{
        val MAC_ADDRESS = "macAddress"
    }

    var macAddress: String? = null

    private lateinit var viewModel: MainViewModel

    private lateinit var device: BluetoothDevice

    val args: TerminalFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        retainInstance = true
        macAddress = args.macAddress
        val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        device = bluetoothAdapter.getRemoteDevice(macAddress)

        viewModel = activity?.run {
            ViewModelProviders.of(this)[MainViewModel::class.java]
        } ?: throw Exception("Invalid Activity")


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
        send_btn.setOnClickListener {}

        viewModel.connect(device)
    }

    private fun initView() {

        viewModel.data.observe(viewLifecycleOwner, Observer {
            if (it.isSuccess)
                it.getOrNull()?.let {
                    receive_text.append(it.toHex())
                }
            else
                receive_text.append(it.exceptionOrNull().toString())

        })

        viewModel.status.observe(viewLifecycleOwner, Observer {
            val spn = SpannableStringBuilder(it + '\n')
            spn.setSpan(
                ForegroundColorSpan(resources.getColor(R.color.colorStatusText)),
                0, spn.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            receive_text.append(spn)
        })

    }
}
