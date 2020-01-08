package com.hackathon.watertestinginstant.ui.main.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.hackathon.watertestinginstant.R
import com.hackathon.watertestinginstant.appl.ViewModelFactory
import com.hackathon.watertestinginstant.appl.WaterTestingApplication
import com.hackathon.watertestinginstant.data.Result
import com.hackathon.watertestinginstant.database.AppDataBase
import com.hackathon.watertestinginstant.ui.login.LoginActivity
import kotlinx.android.synthetic.main.fragment_profile.*

@Suppress("DEPRECATION")
class ProfileFragment : Fragment() {

    private lateinit var profileViewModel: ProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        profileViewModel =
            ViewModelProviders.of(this,ViewModelFactory(AppDataBase.getInstance(context!!).waterDao()))[ProfileViewModel::class.java]
        val root = inflater.inflate(R.layout.fragment_profile, container, false)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tvLogout.setOnClickListener {
            WaterTestingApplication.mAuth.signOut()
            LoginActivity.newInstance(activity!!)
        }

        profileViewModel.mars.observe(viewLifecycleOwner, Observer {
            if (it.isSuccess) {
                json.text = (it.getOrNull()?.size.toString())
            } else {
                json.text = (it.exceptionOrNull()?.message)
            }
        })

        call_api.setOnClickListener { profileViewModel.callApi() }
        add_dummy_data.setOnClickListener { profileViewModel.saveData() }
        sync.setOnClickListener { profileViewModel.syncData() }
    }
}