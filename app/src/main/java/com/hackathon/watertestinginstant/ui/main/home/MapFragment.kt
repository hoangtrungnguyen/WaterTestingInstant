package com.hackathon.watertestinginstant.ui.main.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.hackathon.watertestinginstant.R

class MapFragment : SupportMapFragment(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, p2: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(
            R.layout.fragment_landing,
            container,
            false
        )
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        this.getMapAsync(this)

    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        // Add a marker in Sydney and move the camera
        val loc = LatLng(-34.0, 151.0)


    }

}