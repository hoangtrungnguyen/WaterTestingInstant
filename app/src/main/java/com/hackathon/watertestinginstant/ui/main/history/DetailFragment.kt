package com.hackathon.watertestinginstant.ui.main.history


import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.fragment.navArgs
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomnavigation.BottomNavigationView

import com.hackathon.watertestinginstant.R
import com.hackathon.watertestinginstant.data.model.WaterData
import com.hackathon.watertestinginstant.ui.customview.animBottom
import com.hackathon.watertestinginstant.util.isInternetConnection
import com.hackathon.watertestinginstant.util.showSnackbarShort
import kotlinx.android.synthetic.main.item_history.*
import kotlinx.android.synthetic.main.item_water_test_history.*
import org.joda.time.DateTime
import org.joda.time.LocalDate
import org.joda.time.format.DateTimeFormatter

/**
 * A simple [Fragment] subclass.
 */
class DetailFragment : Fragment(),OnMapReadyCallback {
    private lateinit var mapView: MapView
    private lateinit var map: GoogleMap
    private lateinit var waterData: WaterData


    val args: DetailFragmentArgs by navArgs()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bt = activity!!.findViewById<BottomNavigationView>(R.id.bottom_nav)
        bt.animBottom(true)
        waterData = args.waterData
        if(!activity!!.isInternetConnection()) activity?.showSnackbarShort("No internet permissiton")

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_detail, container, false)
        mapView = view.findViewById(R.id.map)
        with(mapView) {
            // Initialise the MapView
            onCreate(null)
            // Set the map ready callback to receive the GoogleMap object
            getMapAsync(this@DetailFragment)
        }
        return view
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        MapsInitializer.initialize(context)
        // If map is not initialised properly
        map = googleMap ?: return
        setMapLocation()
    }
    private fun setMapLocation() {
        if (!::map.isInitialized) return
        with(map) {
            waterData.latLng
            moveCamera(CameraUpdateFactory.newLatLngZoom(waterData.latLng, 13f))
            addMarker(MarkerOptions().position(waterData.latLng))
            mapType = GoogleMap.MAP_TYPE_NORMAL
            setOnMapClickListener {
                activity?.showSnackbarShort(it.toString())
            }
        }
        date.text =  waterData.date
        turbidity.text = String.format("%.2f", waterData.Turbidity)
        ph.text = String.format("%.2f", waterData.PH)
        tds.text = String.format("%.2f", waterData.TDS)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }


    override fun onStop() {
        super.onStop()
        val bt = activity!!.findViewById<BottomNavigationView>(R.id.bottom_nav)
        bt.animBottom(false)
    }
}
