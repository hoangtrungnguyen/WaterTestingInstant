package com.hackathon.watertestinginstant.ui.main.home


    import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.hackathon.watertestinginstant.appl.NOTIFICATION_DATA
import com.hackathon.watertestinginstant.appl.ViewModelFactory
import com.hackathon.watertestinginstant.appl.WaterTestingApplication
import com.hackathon.watertestinginstant.data.model.WaterData
import com.hackathon.watertestinginstant.database.AppDataBase
import com.hackathon.watertestinginstant.ui.customview.ProgressBarAnimation
import com.hackathon.watertestinginstant.ui.customview.fadeOut
import com.hackathon.watertestinginstant.ui.main.MainViewModel
import com.hackathon.watertestinginstant.ui.main.PACKAGE_MAIN
import com.hackathon.watertestinginstant.ui.main.history.HistoryAdapter
import com.hackathon.watertestinginstant.util.showDailog
import kotlinx.android.synthetic.main.fragment_landing.*
import kotlinx.coroutines.*


const val MAP_VIEW_BUNDLE_KEY = "MapViewBundleKey"

@Suppress("DEPRECATION")
class LandingFragment : Fragment() {
    private val TAG = this.javaClass.simpleName

    private lateinit var viewModelConnect: ConnectBluetoothViewModel

    private lateinit var viewModelMain: MainViewModel

    private lateinit var receiver: BroadcastReceiver

    private val adapter = HistoryAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModelConnect = ViewModelProviders.of(
            activity!!,
            ViewModelFactory(AppDataBase.getInstance(context!!).waterDao())
        )[ConnectBluetoothViewModel::class.java]
        activity?.let {
            viewModelMain = ViewModelProviders.of(it, ViewModelFactory(AppDataBase.getInstance(
                WaterTestingApplication.application).waterDao())).get(MainViewModel::class.java)
        }


        receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                Log.d(TAG,"intent ${intent == null}")
                if(intent?.action == NOTIFICATION_DATA){
                    val data = intent.getSerializableExtra(PACKAGE_MAIN) as WaterData
                    Log.d(TAG,"intent ${data}")
                    data.let { context?.showDailog(data as? HashMap<String, String>) }
                }
            }
        }
        LocalBroadcastManager.getInstance(context!!).registerReceiver(
            receiver, IntentFilter(NOTIFICATION_DATA)
        )

        activity?.registerReceiver(receiver, IntentFilter(NOTIFICATION_DATA))



    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        return inflater.inflate(
            com.hackathon.watertestinginstant.R.layout.fragment_landing,
            container,
            false
        )
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


//        viewModelConnect.waterData.observe(viewLifecycleOwner, Observer {
//            it.sortedBy { it.time }
//            val waterData = it.firstOrNull()
//            waterData?.let {
//                ph.text = String.format("%.2f", waterData.PH)
//                turbidity.text = String.format("%.2f", waterData.Turbidity)
//                tds.text = String.format("%.2f", waterData.TDS)
//                date.text = waterData.time.toString()
//                nodataView.visibility = View.VISIBLE
//            }
//            if (waterData == null) {
//                viewWaterData.visibility = View.GONE
//                nodataView.visibility = View.VISIBLE
//            }
//        })

        viewModelConnect.latest().observe(viewLifecycleOwner, Observer {
            val progressAnim = ProgressBarAnimation(main_progress_bar, 0F, it.toFloat())
            progressAnim.start()
            var progressStatus: Int = 0

            CoroutineScope(Job()).launch {
                withContext(Dispatchers.Main) {
                    try {
                        while (progressStatus < it) {

                            progressStatus += 1
                            // Update the progress bar and display the
                            //current value in the text view
                            main_progress_bar.progress = progressStatus;
                            water_quality.text = "${progressStatus}%"
                            delay(5)
                        }
                        when {
                            (it < 50) -> {
                                water_quality.setTextColor(Color.parseColor("#F44336"))
//                                main_progress_bar.progressDrawable = (
//                                        DrawableGradient(
//                                            intArrayOf(
//                                                Color.parseColor("#FFEBEE"),
//                                                Color.parseColor("#EF9A9A"),
//                                                Color.parseColor("#F44336")
//                                            ), 0
//                                        ).SetTransparency(10)
//                                        )
                            }
                            (it < 75) -> {
                                water_quality.setTextColor(Color.parseColor("#EF6C00"))
//                                main_progress_bar.progressDrawable =
//                                    DrawableGradient(
//                                        intArrayOf(
//                                            Color.parseColor("#FFF8E1"),
//                                            Color.parseColor("#FFD54F"),
//                                            Color.parseColor("#FFB300")
//                                        ), 0
//                                    ).SetTransparency(10)
                            }
                            (it < 100) -> {
                                water_quality.setTextColor(Color.parseColor("#0277BD"))
//                                main_progress_bar.progressDrawable = (
//                                        DrawableGradient(
//                                            intArrayOf(
//                                                Color.parseColor("#E1F5FE"),
//                                                Color.parseColor("#4FC3F7"),
//                                                Color.parseColor("#039BE5")
//                                            ), 0
//                                        ).SetTransparency(10)
//                                        )
                            }
                        }

                        analyzing.text = "Analyze finish"
                        analyzing.fadeOut()
                    } catch (e: Exception) {
                        Log.d(TAG, e.message.toString())
                    }
                }
            }
        })

        rcvData.adapter = adapter

        viewModelMain.waterData.observe(viewLifecycleOwner, Observer { it
            val waterData = it.sortedBy { it.time }
            waterData.lastOrNull()?.let {
                adapter.updateData(listOf(it))
            }
        })


        viewNearby_container.setOnClickListener {
//            val action = LandingFragmentDirections.actionLandingFragmentToMapFragment()
//            findNavController().navigate(action    )
        }



    }


    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onStop() {
        super.onStop()
    }
}




