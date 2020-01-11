package com.hackathon.watertestinginstant.data

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.hackathon.watertestinginstant.appl.WaterTestingApplication
import com.hackathon.watertestinginstant.data.model.User
import java.io.IOException


/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class LoginDataSource {
    private val TAG: String = this@LoginDataSource.javaClass.simpleName

    val result = MutableLiveData<Result<User>>()

    val mAuth: FirebaseAuth? = WaterTestingApplication.mAuth
    fun login(user:User)/*: Result<User>*/ {
        try {



        } catch (e: Throwable) {
            result.postValue(Result.failure(IOException("Error logging in", e)))
        }
    }

    fun logout() {
        // TODO: revoke authentication
    }
}

