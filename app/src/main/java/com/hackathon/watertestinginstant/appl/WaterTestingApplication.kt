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
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.*


class WaterTestingApplication : Application() {
    companion object {

        lateinit var application: WaterTestingApplication
        lateinit var mAuth: FirebaseAuth
        lateinit var fireBaseDB: FirebaseDatabase
        lateinit var fireBaseStorage: FirebaseStorage
    }


    // Reference to the application graph that is used across the whole app
//    val appComponent = DaggerAp.create()

    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
        application = this

        CoroutineScope(Dispatchers.IO).launch {
            FirebaseMessaging.getInstance().isAutoInitEnabled = true
            mAuth = FirebaseAuth.getInstance()
            fireBaseDB = FirebaseDatabase.getInstance()
            fireBaseStorage = FirebaseStorage.getInstance()
        }
    }
}
