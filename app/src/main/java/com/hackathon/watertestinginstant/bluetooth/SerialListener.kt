package com.hackathon.watertestinginstant.bluetooth

import android.bluetooth.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.google.android.things.bluetooth.*
import com.google.android.things.bluetooth.BluetoothConfigManager
import com.hackathon.watertestinginstant.appl.WaterTestingApplication


class BluetoothConfigManager {
    val manager = BluetoothConfigManager.getInstance()
    val managerProfile = BluetoothProfileManager.getInstance()

    fun configAttribute() {
        manager.bluetoothClass = BluetoothClassFactory.build(
            BluetoothClass.Service.INFORMATION,
            BluetoothClass.Device.COMPUTER_UNCATEGORIZED
        )

        val enabledProfiles = managerProfile.enabledProfiles
        Log.d("Bluetoothe config", "Enabling A2DP sink mode.")
//        val toDisable = listOf(BluetoothProfile.GATT)
//        val toEnable = listOf(BluetoothProfile.A2DP_SINK, BluetoothProfile.AVRCP_CONTROLLER)

//        managerProfile.enableAndDisableProfiles(toEnable, toDisable)
    }

    private lateinit var bluetoothConnectionManager: BluetoothConnectionManager


    fun reportCapability() {
        // Report full input/output capability for this device
        manager.ioCapability = BluetoothConfigManager.IO_CAPABILITY_IO
    }

    val bluetoothPairingCallback = object : BluetoothPairingCallback {
        override fun onPairingInitiated(
            bluetoothDevice: BluetoothDevice?,
            pairingParams: PairingParams?
        ) {
            // Handle incoming pairing request or confirmation of outgoing pairing request
//            handlePairingRequest(bluetoothDevice, pairingParams)
        }

        override fun onPaired(bluetoothDevice: BluetoothDevice?) {
            super.onPaired(bluetoothDevice)
        }
    }

    fun getInstance() {
        bluetoothConnectionManager = BluetoothConnectionManager.getInstance().apply {
            registerPairingCallback(bluetoothPairingCallback)
        }
    }


    private fun startPairing(remoteDevice: BluetoothDevice) {
        bluetoothConnectionManager.initiatePairing(remoteDevice)
    }

    private fun handlePairingRequest(
        bluetoothDevice: BluetoothDevice,
        pairingParams: PairingParams
    ) {
        when (pairingParams.pairingType) {
            PairingParams.PAIRING_VARIANT_DISPLAY_PIN,
            PairingParams.PAIRING_VARIANT_DISPLAY_PASSKEY -> {
                // Display the required PIN to the user
                Log.d("HandlePairing", "Display Passkey - ${pairingParams.pairingPin}")
            }
            PairingParams.PAIRING_VARIANT_PIN, PairingParams.PAIRING_VARIANT_PIN_16_DIGITS -> {
                // Obtain PIN from the user
                val pin = ""
                // Pass the result to complete pairing
                bluetoothConnectionManager.finishPairing(bluetoothDevice, pin)
            }
            PairingParams.PAIRING_VARIANT_CONSENT,
            PairingParams.PAIRING_VARIANT_PASSKEY_CONFIRMATION -> {
                // Show confirmation of pairing to the user
                mPairingRequestReceiver
                // Complete the pairing process
                bluetoothConnectionManager.finishPairing(bluetoothDevice)
            }
        }
    }

    private val mPairingRequestReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            val action = intent.action
            if (action == BluetoothDevice.ACTION_PAIRING_REQUEST) {
                try {
                    val device = intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
                    val pin = intent.getIntExtra("android.bluetooth.device.extra.PAIRING_KEY", 1234)
                    //the pin in case you need to accept for an specific pin
                    Log.d("PIN ", "Start Auto Pairing. PIN = " + intent.getIntExtra("android.bluetooth.device.extra.PAIRING_KEY", 1234))
                    val pinBytes: ByteArray
                    pinBytes = ("" + pin).toByteArray(charset("UTF-8"))
                    device.setPin(pinBytes)
                    //setPairing confirmation if neeeded
                    device.setPairingConfirmation(true)
                } catch (e: Exception) {
                    Log.e("PIN", "Error occurs when trying to auto pair")
                    e.printStackTrace()
                }
            }
        }
    }

    fun handleFileIncome(){
//        bluetoothConnectionManager.get
        val blu = BluetoothAdapter.getDefaultAdapter()
        val device = blu.bondedDevices.first()
        device.connectGatt(WaterTestingApplication.application,true,object :
            BluetoothGattCallback() {
            override fun onCharacteristicRead(
                gatt: BluetoothGatt?,
                characteristic: BluetoothGattCharacteristic?,
                status: Int
            ) {
                characteristic?.value
            }
        })
    }
}