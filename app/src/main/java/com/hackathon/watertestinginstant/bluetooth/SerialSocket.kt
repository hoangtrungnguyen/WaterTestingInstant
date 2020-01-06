package com.hackathon.watertestinginstant.bluetooth

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import com.hackathon.watertestinginstant.appl.BLUETOOTH_SPP
import com.hackathon.watertestinginstant.appl.INTENT_ACTION_DISCONNECT
import com.hackathon.watertestinginstant.ui.main.MainViewModel
import java.io.IOException
import java.util.*
import java.util.concurrent.Executors


class SerialSocket : Runnable {
    private val TAG = this.javaClass.simpleName

    private val disconnectBroadcastReceiver: BroadcastReceiver

    private var context: Context? = null
    private var socket: BluetoothSocket? = null

    private var viewModel: MainViewModel? = null

    private var _connected = false

    private var device: BluetoothDevice? = null;

    init {
        disconnectBroadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                if (viewModel != null)
                    viewModel!!.postStatus("Background Disconnect")
                disconnect() // disconnect now, else would be queued until UI re-attached
            }
        }
    }


    /**
     * connect-success and most connect-errors are returned asynchronously to listener
     */
    @Throws(IOException::class)
    fun connect(context: Context, viewModel: MainViewModel, device: BluetoothDevice) {
        if (_connected)
            throw IOException("already connected")
        this.context = context
        this.viewModel = viewModel
        this.device = device
        context.registerReceiver(
            disconnectBroadcastReceiver,
            IntentFilter(INTENT_ACTION_DISCONNECT)
        )
        Executors.newSingleThreadExecutor().submit(this)
    }

    fun disconnect() {
        viewModel = null
        // connected = false; // run loop will reset connected
        if (socket != null) {
            try {
                socket!!.close()
            } catch (ignored: Exception) {
            }

            socket = null
        }
        try {
            context!!.unregisterReceiver(disconnectBroadcastReceiver)
        } catch (ignored: Exception) {
        }

    }

    @Throws(IOException::class)
    fun write(data: ByteArray) {
        if (!_connected)
            throw IOException("not connected")
        socket!!.outputStream.write(data)
    }

    override fun run() { // connect & read

        try {
            socket = device?.createRfcommSocketToServiceRecord(BLUETOOTH_SPP)
            socket?.connect()
            viewModel?.postStatus("Connected")
        } catch (e: Exception) {
            cancel(e)
            return
        }


        _connected = true

        try {

            socket?.use { socket ->
                // Connect to the remote device through the socket. This call blocks
                // until it succeeds or throws an exception.
                socket.connect()
                _connected = (socket.isConnected)
                // The connection attempt succeeded. Perform work associated with
                // the connection in a separate thread.
                val buffer = ByteArray(1024)
                var len: Int
                while (true) {
                    len = socket.inputStream.read(buffer)
                    val data = buffer.copyOf(len)
                    viewModel?.postResult(Result.success(data))
                }
            }
        } catch (e: Exception) {
            cancel(e)
        }


    }

    // Closes the client socket and causes the thread to finish.
    fun cancel(e: Exception) {
        try {
            viewModel?.postResult(Result.failure(e))
            socket?.close()
        } catch (ignored: Exception) {
        }

        socket = null
    }

    companion object {

        private val BLUETOOTH_SPP = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
    }

}
