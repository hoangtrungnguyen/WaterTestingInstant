package com.hackathon.watertestinginstant.appl

import android.app.Service
import android.content.Intent
import android.os.IBinder

/**
 * A bound Service that instantiates the authenticator
 * when started.
 */

class AuthenticatorService: Service(){
    // Instance field that stores the authenticator object
    private lateinit var mAuthenticator: Authenticator

    override fun onCreate() {
        // Create a new authenticator object
        mAuthenticator = Authenticator(this)
    }

    override fun onBind(intent: Intent?): IBinder? {
       return mAuthenticator.iBinder
    }


}