package com.hackathon.watertestinginstant.ui.main

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.telephony.TelephonyManager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.NavController
import androidx.navigation.ui.setupActionBarWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.hackathon.watertestinginstant.R
import com.hackathon.watertestinginstant.appl.NOTIFICATION_DATA
import com.hackathon.watertestinginstant.appl.ViewModelFactory
import com.hackathon.watertestinginstant.appl.WaterTestingApplication
import com.hackathon.watertestinginstant.data.model.WaterData
import com.hackathon.watertestinginstant.database.AppDataBase
import com.hackathon.watertestinginstant.util.isInternetConnection
import com.hackathon.watertestinginstant.util.showDailog
import com.hackathon.watertestinginstant.util.showSnackbarShort
import kotlinx.android.synthetic.main.activity_main.*


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
            TAB_HOME to R.id.home,
            TAB_HISTORY to R.id.history,
            TAB_PROFILE to R.id.profile
        )

    }

    private val TAG = "MainActivity"

    private lateinit var viewModel: MainViewModel

    private var currentNavController: LiveData<NavController>? = null

    private lateinit var receiver: BroadcastReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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


        setUpNavigationDrawer()

        if(!isInternetConnection()) showSnackbarShort("No internet connection")
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
            val tvName = navigation_view.getHeaderView(0).findViewById<TextView>(R.id.nav_view_name)
            val tvEmail =
                navigation_view.getHeaderView(0).findViewById<TextView>(R.id.nav_view_email)
            tvName.text = it.displayName
            tvEmail.text = it.email
        })

        viewModel.waterData.observe(this, Observer {
            Log.d(TAG,it.toString())
//            showSnackbarShort(it.toString())
        })
    }

    /**
     * Called on first creation and when restoring state.
     */
    private fun setupBottomNavigationBar() {
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_nav)
        val navGraphIds = listOf(R.navigation.home, R.navigation.history, R.navigation.profile)

        // Setup the bottom navigation view with a list of navigation graphs
        val controller = bottomNavigationView.setupWithNavController(
            navGraphIds = navGraphIds,
            fragmentManager = supportFragmentManager,
            containerId = R.id.nav_host_fragment,
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
        menuInflater.inflate(R.menu.header_menu, menu);
        return true;
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.sync -> {
                if (!application.isInternetConnection()) {
                    showSnackbarShort("No internet connection, can't sync with the system")
                    return false
                }
                return true
            }//TODO send file to ser
            R.id.device -> {
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
        bottom_nav.selectedItemId = tabs[bottomPosition] ?: R.id.home
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
