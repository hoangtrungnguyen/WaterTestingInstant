package com.hackathon.watertestinginstant.ui.main.home

import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.maps.model.LatLng
import com.hackathon.watertestinginstant.appl.WaterTestingApplication

class MapViewModel(val application: WaterTestingApplication) : AndroidViewModel(application) {
    private val TAG: String = "MapViewModel"

    //    private val _listUser = MutableLiveData<>()
    val store = WaterTestingApplication.fireBaseFireStore

    val dirtyLocations = MutableLiveData<List<LatLng>>()

    init {
        getDirtyLocation()

    }

    fun getDirtyLocation() {
        store.collection("DirtyLocation")
            .get()
            .addOnSuccessListener { docs ->
                val docSnap = docs
                val ls = mutableListOf<LatLng>()
                for (doc in docSnap) {
//                    val obj = doc.toObject(LatLng::class.java)
                    doc?.let {
                        Log.d(TAG, doc.data.toString())
                        ls.add(
                            LatLng(
                                it.data["latitude"] as? Double ?: 0.0,
                                it.data["longtitude"] as? Double ?: 0.0
                            )
                        )
                    }
                }
                dirtyLocations.postValue(ls)

            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents: ", exception)
            }
    }
}