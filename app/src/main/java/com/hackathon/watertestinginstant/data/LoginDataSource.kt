package com.hackathon.watertestinginstant.data

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.hackathon.watertestinginstant.appl.WaterTestingApplication
import com.hackathon.watertestinginstant.data.model.LoggedInUser
import java.io.IOException
import java.lang.NullPointerException


/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class LoginDataSource {
    private val TAG: String = this@LoginDataSource.javaClass.simpleName

    val result = MutableLiveData<Result<LoggedInUser>>()

    val mAuth: FirebaseAuth? = WaterTestingApplication.mAuth
    fun login(username: String, password: String)/*: Result<LoggedInUser>*/ {
        try {
            // TODO: handle loggedInUser authentication
//            val fakeUser = LoggedInUser(java.util.UUID.randomUUID().toString(), "Jane Doe")
            // Configure Google Sign In



//            return Result.Success(fakeUser)
        } catch (e: Throwable) {
            result.postValue(Result.Error(IOException("Error logging in", e)))
        }
    }

    fun logout() {
        // TODO: revoke authentication
    }
}

