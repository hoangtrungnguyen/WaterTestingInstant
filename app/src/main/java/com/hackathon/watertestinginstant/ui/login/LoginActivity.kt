package com.hackathon.watertestinginstant.ui.login

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.hackathon.watertestinginstant.appl.ViewModelFactory
import com.hackathon.watertestinginstant.appl.WaterTestingApplication
import com.hackathon.watertestinginstant.database.AppDataBase
import com.hackathon.watertestinginstant.ui.main.MainActivity


@Suppress("DEPRECATION")
class LoginActivity : AppCompatActivity() {

    companion object {
        fun newInstance(context: Context) {
            val intent = Intent(context, LoginActivity::class.java)
            context.startActivity(intent)
        }
    }

    private lateinit var loginViewModel: LoginViewModel
    override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {
        return super.onCreateView(name, context, attrs)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        val currentUser = WaterTestingApplication.mAuth.currentUser
//        if (currentUser != null) {
//            MainActivity.newInstance(this)
//            setResult(Activity.RESULT_OK)
//        }
        setContentView(com.hackathon.watertestinginstant.R.layout.activity_login)
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
}

