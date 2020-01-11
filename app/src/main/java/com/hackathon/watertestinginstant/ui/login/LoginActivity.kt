package com.hackathon.watertestinginstant.ui.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.firebase.ui.auth.AuthUI
import com.google.android.gms.maps.SupportMapFragment
import com.hackathon.watertestinginstant.R
import com.hackathon.watertestinginstant.appl.NOTIFICATION_DATA
import com.hackathon.watertestinginstant.appl.ViewModelFactory
import com.hackathon.watertestinginstant.database.AppDataBase
import com.hackathon.watertestinginstant.util.isLocationPermissionGranted
import com.hackathon.watertestinginstant.util.requestLocationPermission
import com.hackathon.watertestinginstant.util.showDailog


val SIGN_IN_WITH_SOCIAL_ACC = 72
@Suppress("DEPRECATION")
class LoginActivity : AppCompatActivity() {

    companion object {
        fun newInstance(context: Context) {
            val intent = Intent(context, LoginActivity::class.java)
            context.startActivity(intent)
        }

        val providers = arrayListOf(
            AuthUI.IdpConfig.GoogleBuilder().build()
        )
    }

    private lateinit var loginViewModel: LoginViewModel
    override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {
        return super.onCreateView(name, context, attrs)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.hackathon.watertestinginstant.R.layout.activity_login)
        if(!isLocationPermissionGranted()){
            requestLocationPermission()
        }
        loginViewModel.delete()
//        checkPermission()
    }

    override fun onStart() {
        super.onStart()
        loginViewModel = ViewModelProviders.of(
            this,
            ViewModelFactory(AppDataBase.getInstance(this).waterDao())
        ).get(LoginViewModel::class.java)

    }

    override fun onPause() {
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        val data = intent.getSerializableExtra(NOTIFICATION_DATA)
        data?.let { this.showDailog(data as? HashMap<String, String>) }
    }
}
