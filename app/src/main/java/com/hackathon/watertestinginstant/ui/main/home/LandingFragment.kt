package com.hackathon.watertestinginstant.ui.main.home


import android.annotation.SuppressLint
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders

import com.hackathon.watertestinginstant.R
import com.hackathon.watertestinginstant.appl.ViewModelFactory
import com.hackathon.watertestinginstant.database.AppDataBase
import com.hackathon.watertestinginstant.ui.customview.ProgressBarAnimation
import com.hackathon.watertestinginstant.ui.util.setBkg
import kotlinx.android.synthetic.main.fragment_landing.*
import kotlinx.android.synthetic.main.item_history.*
import kotlinx.coroutines.*
import java.lang.Exception
import java.time.LocalTime
import java.util.*
import kotlin.coroutines.coroutineContext
import kotlin.math.floor

/**
 * A simple [Fragment] subclass.
 */
@Suppress("DEPRECATION")
class LandingFragment : Fragment() {
    private val TAG = this.javaClass.simpleName

    private lateinit var viewModelConnect: ConnectBluetoothViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModelConnect = ViewModelProviders.of(
            activity!!,
            ViewModelFactory(AppDataBase.getInstance(context!!).waterDao())
        )[ConnectBluetoothViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_landing, container, false)
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        viewModelConnect.waterData.observe(viewLifecycleOwner, Observer {
            it.sortedBy { it.time }
            val waterData = it.first()
            hp.text = waterData.PH.toString()
            turbidity.text = waterData.Turbidity.toString()
            tds.text = waterData.TDS.toString()
            date.text = waterData.time.toString()
        })

        viewModelConnect.latest().observe(viewLifecycleOwner, Observer {
            val progressAnim = ProgressBarAnimation(main_progress_bar, 0F, it.toFloat())
            progressAnim.start()
            var progressStatus: Int = 0

            CoroutineScope(Job()).launch {
                withContext(Dispatchers.Main) {
                    try {
                        while (progressStatus < it) {

                            progressStatus += 1;
                            // Update the progress bar and display the
                            //current value in the text view
                            main_progress_bar.progress = progressStatus;
                            water_quality.text = "${progressStatus}%"
                            delay(1)
                        }
                    } catch (e: Exception) {
                        Log.d(TAG, e.message.toString())
                    }
                }
            }
//            when {
//                it> 75.0 -> m
//                it > 50 ->
//                else ->
//            }
        })
    }
}
