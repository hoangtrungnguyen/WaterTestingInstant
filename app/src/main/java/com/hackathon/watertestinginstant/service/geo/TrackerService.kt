package com.hackathon.watertestinginstant.service.geo

import android.Manifest
import android.app.PendingIntent
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.database.FirebaseDatabase
import com.hackathon.watertestinginstant.appl.WaterTestingApplication


class TrackerService : Service() {

    private val TAG = TrackerService::class.java.simpleName

    val store = WaterTestingApplication.fireBaseFireStore

    val dirtyLocations = mutableListOf<LatLng>()

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        locationUpdate()
        getDirtyLocation()
    }


    private fun buildNotification() {
        val stop = "stop"
        registerReceiver(stopReceiver, IntentFilter(stop))
        val broadcastIntent = PendingIntent.getBroadcast(
            this, 0, Intent(stop), PendingIntent.FLAG_UPDATE_CURRENT
        )
        // Create the persistent notification
        val builder = NotificationCompat.Builder(this)
            .setContentTitle("Water testing:Location")
            .setContentText("Near by location have bad water")
            .setOngoing(true)
            .setContentIntent(broadcastIntent)
            .setSmallIcon(com.hackathon.watertestinginstant.R.drawable.ic_icon_water_blue)
        startForeground(1, builder.build())
    }

    protected var stopReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            Log.d(TAG, "received stop broadcast")
            // Stop the service when the notification is tapped
            unregisterReceiver(this)
            stopSelf()
        }
    }

    private fun getDirtyLocation(){
        store.collection("DirtyLocation")
            .get()
            .addOnSuccessListener { docs ->
                val docSnap = docs
                for (doc in docSnap) {
                    val obj = doc.data
                    Log.d(TAG, obj.toString())
                }

            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents: ", exception)
            }
    }

    private fun locationUpdate() {
        // Functionality coming next step
        val request = LocationRequest()
        request.interval = 10000
        request.fastestInterval = 5000
        request.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        val client = LocationServices.getFusedLocationProviderClient(this)
        val permission = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
        if (permission == PackageManager.PERMISSION_GRANTED) {
            // Request location updates and when an update is
            // received, store the location in Firebase
            client.requestLocationUpdates(request, object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult?) {
                    val location = locationResult!!.lastLocation
                    if (location != null) {
                        Log.d(TAG, "location update $location")
//                        location =
                    }
                }
            }, null)
        }
    }
}