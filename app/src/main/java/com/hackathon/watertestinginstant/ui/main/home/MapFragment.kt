package com.hackathon.watertestinginstant.ui.main.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.hackathon.watertestinginstant.R
import com.hackathon.watertestinginstant.appl.ViewModelFactory
import com.hackathon.watertestinginstant.appl.WaterTestingApplication
import com.hackathon.watertestinginstant.service.database.AppDataBase
import com.hackathon.watertestinginstant.ui.main.MainViewModel
import com.hackathon.watertestinginstant.util.showSnackbarShort
import kotlinx.android.synthetic.main.fragment_detail.*

class MapFragment : SupportMapFragment(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var mapViewModel: MapViewModel

    override fun onCreate(p0: Bundle?) {
        super.onCreate(p0)
        mapViewModel = ViewModelProviders.of(
            this, ViewModelFactory(
                AppDataBase.getInstance(
                    WaterTestingApplication.application
                ).waterDao()
            )
        ).get(MapViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, p2: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(
            R.layout.fragment_map,
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
        mapViewModel.dirtyLocations.observe(viewLifecycleOwner, Observer {
            if (!::mMap.isInitialized) return@Observer
            with(mMap) {
                if (it.size < 1) return@Observer
                it.forEach { addMarker(MarkerOptions().position(it)) }
                moveCamera(CameraUpdateFactory.newLatLngZoom(it.first(), 12f))
                mapType = GoogleMap.MAP_TYPE_NORMAL
                setOnMapClickListener {
                    activity?.showSnackbarShort(it.toString())
                }
            }
        })
    }

}