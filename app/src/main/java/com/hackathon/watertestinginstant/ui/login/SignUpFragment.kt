@file:Suppress("DEPRECATION")

package com.hackathon.watertestinginstant.ui.login


import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.hackathon.watertestinginstant.R
import com.hackathon.watertestinginstant.appl.ViewModelFactory
import com.hackathon.watertestinginstant.appl.WaterTestingApplication
import com.hackathon.watertestinginstant.data.Result
import com.hackathon.watertestinginstant.database.AppDataBase
import com.hackathon.watertestinginstant.ui.main.MainActivity
import com.hackathon.watertestinginstant.ui.util.afterTextChanged
import com.hackathon.watertestinginstant.ui.util.showError
import com.hackathon.watertestinginstant.ui.util.showSnackbarShort
import kotlinx.android.synthetic.main.fragment_sign_up.*

/**
 * A simple [Fragment] subclass.
 */
class SignUpFragment : Fragment() {

    lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginViewModel = ViewModelProviders.of(
            this,
            LoginViewModelFactory()
        )[LoginViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_up, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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

        }


        loginViewModel.loginResult.observe(viewLifecycleOwner, Observer {
            if (it == null) activity?.showSnackbarShort("Created Failed")
            val loginResult = it

            loading.visibility = View.GONE
            if (loginResult is Result.Success && loginResult.data != null) {
                MainActivity.newInstance(context!!)
                activity?.setResult(Activity.RESULT_OK)
                activity?.finish()
            }
            if (loginResult is Result.Error) {
                activity?.showError(loginResult.exception)
                activity?.setResult(Activity.RESULT_CANCELED)
            }

        })


        signup.setOnClickListener {
            loading.visibility = View.VISIBLE
            loginViewModel.signUp(username.text.toString(), password.text.toString())
        }
    }
}
