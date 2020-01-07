package com.hackathon.watertestinginstant.ui.login


import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders

import com.hackathon.watertestinginstant.R
import com.hackathon.watertestinginstant.ui.main.MainActivity
import com.hackathon.watertestinginstant.ui.util.hideKeyBoard
import com.hackathon.watertestinginstant.ui.util.showError
import com.hackathon.watertestinginstant.ui.util.showSnackbarShort
import kotlinx.android.synthetic.main.activity_login.*
import com.hackathon.watertestinginstant.data.Result
/**
 * A simple [Fragment] subclass.
 */
class SignInFragment : Fragment() {

    private lateinit var loginViewModel: LoginViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_in, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loginViewModel = ViewModelProviders.of(this, LoginViewModelFactory())
            .get(LoginViewModel::class.java)

        loginViewModel.loginFormState.observe(viewLifecycleOwner, Observer {
            val loginState = it ?: return@Observer

            // disable login button unless both username / password is valid
            login.isEnabled = loginState.isDataValid

            if (loginState.usernameError != null) {
                username.error = getString(loginState.usernameError)
            }
            if (loginState.passwordError != null) {
                password.error = getString(loginState.passwordError)
            }
        })

        loginViewModel.loginResult.observe(viewLifecycleOwner, Observer {
            val loginResult = it ?: return@Observer

            loading.visibility = View.GONE
            if (loginResult is Result.Success && loginResult.data != null) {
                MainActivity.newInstance(context!!)
                activity?.setResult(Activity.RESULT_OK)
                activity?.finish()
            }
            if (loginResult is Result.Error) {
                activity?.showError(loginResult.exception)
            }
        })

        loginViewModel.isInternetConnected.observe(this, Observer {
            if (it == false) {
                activity?.showSnackbarShort("No internet connection")
                loading.visibility = View.GONE

            }
        })

        username.afterTextChanged {
            loginViewModel.loginDataChanged(
                username.text.toString(),
                password.text.toString()
            )
        }

        password.apply {
            //For test purpose only
            this.setText("12345678")
            username.setText("user2@gmail.com")



            afterTextChanged {
                loginViewModel.loginDataChanged(
                    username.text.toString(),
                    password.text.toString()
                )
            }

            setOnEditorActionListener { _, actionId, _ ->
                when (actionId) {
                    EditorInfo.IME_ACTION_DONE ->
                        loginViewModel.login(
                            username.text.toString(),
                            password.text.toString()
                        )
                }
                false
            }

            login.setOnClickListener {
                loading.visibility = View.VISIBLE
                activity?.hideKeyBoard()
                loginViewModel.login(username.text.toString(), password.text.toString())

            }
        }
    }
}
