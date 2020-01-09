package com.hackathon.watertestinginstant.ui.splash

import android.content.Context
import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import com.hackathon.watertestinginstant.R
import com.hackathon.watertestinginstant.appl.WaterTestingApplication
import com.hackathon.watertestinginstant.ui.login.LoginActivity
import com.hackathon.watertestinginstant.ui.main.MainActivity
import kotlinx.coroutines.*


class SplashActivity : AppCompatActivity() {
    // Declare SplashViewModel with Koin and allow constructor dependency injection
    lateinit var context: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.view_flash)
        // go straight to main if a token is stored
        context = this

        CoroutineScope(Dispatchers.Main).launch{
            delay(1000)
            withContext(Dispatchers.Main){
                if(WaterTestingApplication.mAuth.currentUser != null){
                    MainActivity.newInstance(context)
                    finish()
                } else {
                    LoginActivity.newInstance(context)
                    finish()
                }
            }
        }
    }

    private fun delay(){

    }



}