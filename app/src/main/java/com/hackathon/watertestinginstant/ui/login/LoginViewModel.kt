package com.hackathon.watertestinginstant.ui.login

import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.hackathon.watertestinginstant.R
import com.hackathon.watertestinginstant.appl.WaterTestingApplication
import com.hackathon.watertestinginstant.data.LoginRepository
import com.hackathon.watertestinginstant.data.Result
import com.hackathon.watertestinginstant.ui.util.isInternetConnection
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

    private val _isInternetConnected = MutableLiveData<Boolean>()
    val isInternetConnected: LiveData<Boolean> = _isInternetConnected

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
//            val fakeUser = LoggedInUser(java.util.UUID.randomUUID().toString(), "Jane Doe")
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
                                    Result.Success(user)
                                } else Result.Error(NullPointerException("No user found"))
                            )
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmailAndPassword:failure", task.getException())
                            _loginResult.postValue(
                                Result.Error(
                                    task.exception ?: Exception("Unknown Exception")
                                )
                            )
                        }
                    }

            }

        } catch (e: Throwable) {
            _loginResult.postValue(Result.Error(IOException("Error logging in", e)))
        }

    }


    fun signUp(email: String, password: String) {
        mAuth?.apply {
            createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful()) { // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "createUserWithEmail:success")
                        val user = mAuth.currentUser
                        _loginResult.postValue(
                            if (user != null) {
                                Result.Success(user)

                            } else Result.Error(NullPointerException("No user found"))
                        )
                    } else { // If sign in fails, display a message to the user.
                        Log.w(TAG, "createUserWithEmail:failure", task.getException())
                        _loginResult.postValue(
                            Result.Error(
                                task.exception ?: Exception("Unknown Exception")
                            )
                        )
                    }
                }
        }
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
}
