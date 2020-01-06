package com.hackathon.watertestinginstant.appl

import android.app.Application
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.polidea.rxandroidble2.LogConstants
import com.polidea.rxandroidble2.LogOptions
import com.polidea.rxandroidble2.RxBleClient

class WaterTestingApplication : Application() {
    companion object {
        lateinit var rxBleClient: RxBleClient
            private set

        lateinit var application: WaterTestingApplication
        lateinit var mAuth: FirebaseAuth
    }
    // Reference to the application graph that is used across the whole app
//    val appComponent = DaggerAp.create()

    override fun onCreate() {
        super.onCreate()
        rxBleClient = RxBleClient.create(this)
        RxBleClient.updateLogOptions(
            LogOptions.Builder()
                .setLogLevel(LogConstants.INFO)
                .setMacAddressLogSetting(LogConstants.MAC_ADDRESS_FULL)
                .setUuidsLogSetting(LogConstants.UUIDS_FULL)
                .setShouldLogAttributeValues(true)
                .build()
        )
        FirebaseApp.initializeApp(this)
        mAuth = FirebaseAuth.getInstance()
        application = this
    }
}