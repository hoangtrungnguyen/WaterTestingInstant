package com.hackathon.watertestinginstant.appl

import android.app.Application
import bleshadow.dagger.Module
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.hackathon.watertestinginstant.database.AppDataBase
import com.polidea.rxandroidble2.LogConstants
import com.polidea.rxandroidble2.LogOptions
import com.polidea.rxandroidble2.RxBleClient
import bleshadow.dagger.Provides
import bleshadow.javax.inject.Singleton
import com.google.firebase.messaging.FirebaseMessaging


class WaterTestingApplication : Application() {
    companion object {
        lateinit var rxBleClient: RxBleClient
            private set

        lateinit var application: WaterTestingApplication
        lateinit var mAuth: FirebaseAuth

        lateinit var appDataBase :AppDataBase
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
        appDataBase = AppDataBase.getInstance(this)
        FirebaseMessaging.getInstance().isAutoInitEnabled = true
    }
}
