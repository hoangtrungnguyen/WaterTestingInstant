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
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class WaterTestingApplication : Application() {
    companion object {

        lateinit var application: WaterTestingApplication
        lateinit var mAuth: FirebaseAuth
        lateinit var fireBaseDB: FirebaseDatabase
    }
    // Reference to the application graph that is used across the whole app
//    val appComponent = DaggerAp.create()

    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
        mAuth = FirebaseAuth.getInstance()
        application = this

        FirebaseMessaging.getInstance().isAutoInitEnabled = true

        fireBaseDB = FirebaseDatabase.getInstance()
    }
}
