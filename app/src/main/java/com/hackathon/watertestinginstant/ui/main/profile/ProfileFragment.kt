package com.hackathon.watertestinginstant.ui.main.profile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.firebase.ui.auth.AuthUI
import com.hackathon.watertestinginstant.R
import com.hackathon.watertestinginstant.appl.ViewModelFactory
import com.hackathon.watertestinginstant.appl.WaterTestingApplication
import com.hackathon.watertestinginstant.data.Result
import com.hackathon.watertestinginstant.database.AppDataBase
import com.hackathon.watertestinginstant.ui.login.LoginActivity
import com.hackathon.watertestinginstant.ui.main.scan.ScanActivity
import com.hackathon.watertestinginstant.ui.util.showSnackbarShort
import kotlinx.android.synthetic.main.fragment_profile.*

@Suppress("DEPRECATION")
class ProfileFragment : Fragment() {
    private val TAG = this.javaClass.simpleName
    private lateinit var profileViewModel: ProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        profileViewModel =
            ViewModelProviders.of(
                this,
                ViewModelFactory(AppDataBase.getInstance(context!!).waterDao())
            )[ProfileViewModel::class.java]
        val root = inflater.inflate(R.layout.fragment_profile, container, false)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tvLogout.setOnClickListener {
            Log.d("SignOut",(WaterTestingApplication.mAuth.currentUser?.uid).toString())
            WaterTestingApplication.mAuth.signOut()
            AuthUI.getInstance()
                .signOut(context!!)
                .addOnCompleteListener {
                    // ...
                }
            Log.d("SignOut",(WaterTestingApplication.mAuth.currentUser?.uid).toString())
            activity?.finish()
            LoginActivity.newInstance(activity!!)
        }

        profileViewModel.mars.observe(viewLifecycleOwner, Observer {
            if (it.isSuccess) {
                json.text = (it.getOrNull()?.size.toString())
            } else {
                json.text = (it.exceptionOrNull()?.message)
            }
        })

        profileViewModel.result.observe(viewLifecycleOwner, Observer {
            try {
                activity!!.showSnackbarShort(it.toString())
                json.text = "API Water: $it"
            } catch (e: Exception){

            }
        })

        add_dummy_data.setOnClickListener { profileViewModel.saveData() }
        call_api.setOnClickListener { profileViewModel.maintain() }
        sync.setOnClickListener { profileViewModel.syncData() }

        add_device.setOnClickListener {
            findNavController().navigate(R.id.action_profile_to_scan)
        }
    }
}