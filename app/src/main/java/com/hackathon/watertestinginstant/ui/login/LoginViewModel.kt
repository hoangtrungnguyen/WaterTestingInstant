package com.hackathon.watertestinginstant.ui.login

import android.util.Log
import android.util.Patterns
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.hackathon.watertestinginstant.R
import com.hackathon.watertestinginstant.appl.WaterTestingApplication
import com.hackathon.watertestinginstant.data.model.User
import com.hackathon.watertestinginstant.service.database.AppDataBase
import com.hackathon.watertestinginstant.service.network.WaterApi
import com.hackathon.watertestinginstant.util.isInternetConnection
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException


class LoginViewModel(
    private val mAuth: FirebaseAuth?,
    private val application: WaterTestingApplication
) : AndroidViewModel(application) {
    private val TAG: String = this@LoginViewModel.javaClass.simpleName

    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginForm

    private val _loginResult = MutableLiveData<Result<FirebaseUser>>()
    val loginResult: LiveData<Result<FirebaseUser>> = _loginResult

    private val _signUpResult = MutableLiveData<Result<String>>()
    val signUpResult: LiveData<Result<String>> = _signUpResult

    private val _isInternetConnected = MutableLiveData<Boolean>()
    val isInternetConnected: LiveData<Boolean> = _isInternetConnected

    val DBref = WaterTestingApplication.fireBaseDB.reference

    init {

        _isInternetConnected.postValue(application.isInternetConnection())
    }

    //Sign In Form
    fun login(username: String, password: String) {
        // can be launched in a separate asynchronous job
        _isInternetConnected.postValue(application.isInternetConnection())
        if (!application.isInternetConnection()) return
        try {
            // TODO: handle loggedInUser authentication
//            val fakeUser = User(java.util.UUID.randomUUID().toString(), "Jane Doe")
            // Configure Google Sign In
            mAuth?.apply {
                signInWithEmailAndPassword(username, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success")
                            val user = mAuth.currentUser
                            _loginResult.postValue(
                                if (user != null) {
                                    Result.success(user)
                                } else Result.failure(NullPointerException("No user found"))
                            )
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmailAndPassword:failure", task.getException())
                            _loginResult.postValue(
                                Result.failure(
                                    task.exception ?: Exception("Unknown Exception")
                                )
                            )
                        }
                    }

            }

        } catch (e: Throwable) {
            _loginResult.postValue(Result.failure(IOException("Error logging in", e)))
        }

    }


    fun signUp(user: User) {
        WaterApi.service.signUp(user).enqueue(object : Callback<String>{
            override fun onFailure(call: Call<String>, t: Throwable) {
                _signUpResult.postValue(Result.failure(t))

            }

            override fun onResponse(call: Call<String>, response: Response<String>) {
                _signUpResult.postValue(Result.success(response.body() ?: "Null"))
            }

        })

    }


    //Check user form

    fun loginDataChanged(username: String, password: String) {
        if (!isUserNameValid(username)) {
            _loginForm.value = LoginFormState(usernameError = R.string.invalid_username)
        } else if (!isPasswordValid(password)) {
            _loginForm.value = LoginFormState(passwordError = R.string.invalid_password)
        } else {
            _loginForm.value = LoginFormState(isDataValid = true)
        }
    }

    // A placeholder username validation check
    private fun isUserNameValid(username: String): Boolean {
        return if (username.contains('@')) {
            Patterns.EMAIL_ADDRESS.matcher(username).matches()
        } else {
            username.isNotBlank()
        }
    }

    // A placeholder password validation check
    private fun isPasswordValid(password: String): Boolean {
        return password.length > 5
    }

    fun delete() {
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                AppDataBase.getInstance(application).waterDao().deleteAll()
            }
        }
    }
}
