package com.hackathon.watertestinginstant.appl

import android.app.Application
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.*


class WaterTestingApplication : Application() {
    companion object {

        lateinit var application: WaterTestingApplication
        lateinit var mAuth: FirebaseAuth
        lateinit var fireBaseDB: FirebaseDatabase
        lateinit var fireBaseStorage: FirebaseStorage
        lateinit var fireBaseFireStore: FirebaseFirestore
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
            fireBaseFireStore =FirebaseFirestore.getInstance()
        }
    }
}
