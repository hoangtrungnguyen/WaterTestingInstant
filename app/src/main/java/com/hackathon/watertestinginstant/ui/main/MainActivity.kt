package com.hackathon.watertestinginstant.ui.main

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.telephony.TelephonyManager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.ui.setupActionBarWithNavController
import com.google.android.gms.location.GeofencingClient
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.hackathon.watertestinginstant.appl.ViewModelFactory
import com.hackathon.watertestinginstant.appl.WaterTestingApplication
import com.hackathon.watertestinginstant.service.database.AppDataBase
import com.hackathon.watertestinginstant.service.geo.TrackerService
import com.hackathon.watertestinginstant.util.isInternetConnection
import com.hackathon.watertestinginstant.util.showSnackbarShort
import kotlinx.android.synthetic.main.activity_main.*

private val PERMISSIONS_REQUEST_GPS = 211

val PACKAGE_MAIN = "com.hackathon.watertestinginstant.ui.main.MainActivity"
@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {
    companion object {
        fun newInstance(context: Context) {
            val intent = Intent(context, MainActivity::class.java)
            context.startActivity(intent)
        }

        const val TAB_HOME = 0
        const val TAB_HISTORY = 1
        const val TAB_PROFILE = 2

        val tabs = mapOf<Int, Int>(
            TAB_HOME to com.hackathon.watertestinginstant.R.id.home,
            TAB_HISTORY to com.hackathon.watertestinginstant.R.id.history,
            TAB_PROFILE to com.hackathon.watertestinginstant.R.id.profile
        )

    }

    private val TAG = "MainActivity"

    private lateinit var viewModel: MainViewModel

    private var currentNavController: LiveData<NavController>? = null

    private lateinit var receiver: BroadcastReceiver

    lateinit var geofencingClient: GeofencingClient


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.hackathon.watertestinginstant.R.layout.activity_main)

        viewModel = ViewModelProviders.of(
            this,
            ViewModelFactory(AppDataBase.getInstance(WaterTestingApplication.application).waterDao())
        ).get(MainViewModel::class.java)


        if (savedInstanceState == null) {
            setupBottomNavigationBar()
        } // Else, need to wait for onRestoreInstanceState

        val filter = IntentFilter()
        filter.apply {
            addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED)
            addAction(BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED)
            addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED)
            addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)
            addAction(BluetoothAdapter.ACTION_STATE_CHANGED)
//            addAction()
        }

        checkGPS()


        setUpNavigationDrawer()
        if(!isInternetConnection()) showSnackbarShort("No internet connection")
    }

    private fun checkGPS() {
        // Check GPS is enabled
        val lm = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Toast.makeText(this, "Please enable location services", Toast.LENGTH_SHORT).show()
            finish()
        }

        // Check location permission is granted - if it is, start
        // the service, otherwise request the permission
        val permission = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
        if (permission == PackageManager.PERMISSION_GRANTED) {
            startTrackerService()
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSIONS_REQUEST_GPS
            )
        }
    }

    private fun startTrackerService(){
        startService(Intent(this,TrackerService::class.java))

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == PERMISSIONS_REQUEST_GPS && grantResults.size == 1
            && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // Start the service when the permission is granted
            startTrackerService();
        }
    }

    override fun onStart() {
        super.onStart()
        isPause = false
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        // Now that BottomNavigationBar has restored its instance state
        // and its selectedItemId, we can proceed with setting up the
        // BottomNavigationBar with Navigation
        setupBottomNavigationBar()
    }


    private fun setUpNavigationDrawer() {
        viewModel.user.observe(this, Observer {
            val tvName = navigation_view.getHeaderView(0).findViewById<TextView>(com.hackathon.watertestinginstant.R.id.nav_view_name)
            val tvEmail =
                navigation_view.getHeaderView(0).findViewById<TextView>(com.hackathon.watertestinginstant.R.id.nav_view_email)
            tvName.text = it.displayName
            tvEmail.text = it.email
        })

        viewModel.waterData.observe(this, Observer {
            Log.d(TAG,it.toString())
            showSnackbarShort(it.toString())
        })
    }

    /**
     * Called on first creation and when restoring state.
     */
    private fun setupBottomNavigationBar() {
        val bottomNavigationView = findViewById<BottomNavigationView>(com.hackathon.watertestinginstant.R.id.bottom_nav)
        val navGraphIds = listOf(com.hackathon.watertestinginstant.R.navigation.home, com.hackathon.watertestinginstant.R.navigation.history, com.hackathon.watertestinginstant.R.navigation.profile)

        // Setup the bottom navigation view with a list of navigation graphs
        val controller = bottomNavigationView.setupWithNavController(
            navGraphIds = navGraphIds,
            fragmentManager = supportFragmentManager,
            containerId = com.hackathon.watertestinginstant.R.id.nav_host_fragment,
            intent = intent
        )

        // Whenever the selected controller changes, setup the action bar.
        controller.observe(this, Observer { navController ->
            setupActionBarWithNavController(navController)
        })
        currentNavController = controller
    }

    override fun onSupportNavigateUp(): Boolean {
        return currentNavController?.value?.navigateUp() ?: false
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(com.hackathon.watertestinginstant.R.menu.header_menu, menu);
        return true;
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            com.hackathon.watertestinginstant.R.id.sync -> {
                if (!application.isInternetConnection()) {
                    showSnackbarShort("No internet connection, can't sync with the system")
                    return false
                }
                return true
            }//TODO send file to ser
            com.hackathon.watertestinginstant.R.id.device -> {
                val intentBluetooth = Intent()
                intentBluetooth.action = android.provider.Settings.ACTION_BLUETOOTH_SETTINGS
                startActivity(intentBluetooth)
            }
        }
        return false
    }


    var isPause: Boolean = true

    fun setNav(bottomPosition: Int) {
        if (bottomPosition in 0 until bottom_nav.menu.size()) {
            Log.d(TAG, "Index out of bound")
            return
        }
        bottom_nav.selectedItemId = tabs[bottomPosition] ?: com.hackathon.watertestinginstant.R.id.home
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
        isPause = true
    }

    fun getDeviceId() {
        val tm = baseContext.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
//         Log.d("MainActivity" , "deviceid ${tm.deviceId}")
//        val androidId = Settings.Secure.getString(getContentResolver(),Settings.Secure.ANDROID_ID);
//        requestPermissions(,9)

//        val tmDevice: String
//        val tmSerial: String
//        val androidId: String
//        tmDevice = "" + tm.deviceId
//        tmSerial = "" + tm.simSerialNumber
//        androidId = "" + android.provider.Settings.Secure.getString(
//            getContentResolver(),
//            android.provider.Settings.Secure.ANDROID_ID
//        )
//
//        val deviceUuid =
//            UUID.fromString(androidId.hashCode(), tmDevice.hashCode().toLong() shl 32 or tmSerial.hashCode())
//        val deviceId = deviceUuid.toString()

    }

}
