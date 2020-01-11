@file:Suppress("DEPRECATION")

package com.hackathon.watertestinginstant.ui.login


import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.hackathon.watertestinginstant.R
import com.hackathon.watertestinginstant.data.model.User
import com.hackathon.watertestinginstant.ui.main.MainActivity
import com.hackathon.watertestinginstant.util.afterTextChanged
import com.hackathon.watertestinginstant.util.hideKeyBoard
import com.hackathon.watertestinginstant.util.showSnackbarShort
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

        useremail.afterTextChanged {
            loginViewModel.loginDataChanged(
                useremail.text.toString(),
                password.text.toString()
            )
        }

        password.apply {
            //For test purpose only
            this.setText("12345678")
            useremail.setText("user43@gmail.com")



            afterTextChanged {
                loginViewModel.loginDataChanged(
                    useremail.text.toString(),
                    password.text.toString()
                )
            }

            setOnEditorActionListener { _, actionId, _ ->
                when (actionId) {
                    EditorInfo.IME_ACTION_DONE ->
                        loginViewModel.login(
                            useremail.text.toString(),
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
            if (loginResult.isSuccess && loginResult.getOrNull() != null) {
                MainActivity.newInstance(context!!)
                activity?.setResult(Activity.RESULT_OK)
                activity?.finish()
            }
            it.onFailure {
                activity?.showSnackbarShort(it.message ?: "unknown")
                activity?.setResult(Activity.RESULT_CANCELED)
            }


        })

        loginViewModel.signUpResult.observe(viewLifecycleOwner, Observer {
            it.onSuccess {
                activity!!.showSnackbarShort(it)
                loading.visibility = View.GONE
            }

            it.onFailure {
                activity!!.showSnackbarShort(it.message ?: "Unknown exception")
                loading.visibility = View.GONE
            }
        })


        signup.setOnClickListener {
            loading.visibility = View.VISIBLE
            loginViewModel.signUp(
                User(
                    email = useremail.text.toString(),
                    pass = password.text.toString(),
                    phone = phone_number.text.toString(),
                    address = address.selectedItem.toString()
                )
            )
            activity?.hideKeyBoard()
        }

        signin.setOnClickListener {
            findNavController().popBackStack()
        }


// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter.createFromResource(
            context!!,
            R.array.address_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            address.adapter = adapter
        }
    }
}
