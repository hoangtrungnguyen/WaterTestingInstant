package com.hackathon.watertestinginstant.service.bluetooth

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP
import android.content.Intent.FLAG_ACTIVITY_SINGLE_TOP
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.navigation.findNavController
import com.hackathon.watertestinginstant.R
import com.hackathon.watertestinginstant.ui.main.MainActivity
import com.hackathon.watertestinginstant.ui.main.home.TerminalFragment
import java.lang.Exception

class BluetoothReceiver(val main: MainActivity) : BroadcastReceiver() {
    private val TAG = this.javaClass.simpleName

    override fun onReceive(context: Context, intent: Intent) {
        // This method is called when the BroadcastReceiver is receiving an Intent broadcast.
        val action = intent.action
        val device: BluetoothDevice? = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)

        val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        Log.d("aaaaaaaaaaaa", device?.address ?: "NULL")
        Log.d("aaaaaaaaaaaa", action.toString())


        when {
            BluetoothDevice.ACTION_FOUND == action -> {

            }
            BluetoothDevice.ACTION_ACL_CONNECTED == action -> {

            }
            BluetoothAdapter.ACTION_DISCOVERY_FINISHED == action -> {

            }
            BluetoothAdapter.ACTION_STATE_CHANGED == action -> {

            }
            BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED == action -> {

            }
            BluetoothDevice.ACTION_ACL_DISCONNECTED == action -> {

            }
        }

        if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
            val state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR)
            when (state) {
                BluetoothAdapter.STATE_OFF -> {
                }

                BluetoothAdapter.STATE_TURNING_OFF -> {
                }


                BluetoothAdapter.STATE_ON -> {

                    val address = getAddressMatchDevice(
                        bluetoothAdapter,
                        "ESP32test"
                    ).also { Log.d("aaaaaddd", it.toString()) }

                    address?.let {
                        val args = Bundle()
                        args.putString(TerminalFragment.MAC_ADDRESS, address)
//                        val pendingIntent = NavDeepLinkBuilder(context)
//                            .setGraph(R.navigation.home)
//                            .setDestination(R.id.action_homeFragment_to_terminalFragment)
//                            .setArguments(args)
//                            .createPendingIntent()

//                        main.findNavController(R.id.nav_host_fragment)
//                            .navigate(R.id.action_homeFragment_to_terminalFragment, args)

                        if (main.isPause) {
                            val i = Intent(main, MainActivity::class.java)
                            i.flags = FLAG_ACTIVITY_CLEAR_TOP
                            i.flags = FLAG_ACTIVITY_SINGLE_TOP
                            main.startActivity(i)
                            main.setNav(MainActivity.TAB_HOME)
                            val deeplink = Uri.parse("com.hackathon.watertestinginstant.ui.main.home.TerminalFragment")
                            main.findNavController(R.id.nav_host_fragment)
                                .navigate(deeplink)

                        }

                        main.setNav(MainActivity.TAB_HOME)
                        val deeplink = Uri.parse("com.hackathon.watertestinginstant.ui.main.home.TerminalFragment")
                        main.findNavController(R.id.nav_host_fragment)
                            .navigate(deeplink)

                    }.also { Log.d(TAG, it.toString()) }
                }


                BluetoothAdapter.STATE_TURNING_ON -> {
                }


            }

        }
    }

    private fun getAddressMatchDevice(bluetoothAdapter: BluetoothAdapter, name: String): String? = try {
        bluetoothAdapter.bondedDevices.first { it.name == name }.address
    } catch (e: Exception) {
        null
    }


}
