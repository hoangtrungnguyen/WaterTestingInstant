package com.hackathon.watertestinginstant.ui.main.profile

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.hackathon.watertestinginstant.R

@Suppress("DEPRECATION")
class AddDeviceFragment : Fragment() {

    companion object {
        fun newInstance() = AddDeviceFragment()
    }

    private lateinit var viewModel: AddDeviceViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.add_device_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(AddDeviceViewModel::class.java)
        // TODO: Use the ViewModel


    }

}
