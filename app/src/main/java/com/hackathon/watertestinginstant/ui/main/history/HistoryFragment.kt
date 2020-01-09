package com.hackathon.watertestinginstant.ui.main.history

import android.graphics.Canvas
import android.graphics.Rect
import android.os.Bundle
import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.hackathon.watertestinginstant.R
import com.hackathon.watertestinginstant.appl.ViewModelFactory
import com.hackathon.watertestinginstant.appl.WaterTestingApplication
import com.hackathon.watertestinginstant.database.AppDataBase
import com.hackathon.watertestinginstant.ui.main.MainViewModel
import kotlinx.android.synthetic.main.fragment_history.*

@Suppress("DEPRECATION")
class HistoryFragment : Fragment() {

    private lateinit var historyViewModel: HistoryViewModel

    private val adapterHistory = HistoryAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        historyViewModel = ViewModelProviders.of(
            this,
            ViewModelFactory(AppDataBase.getInstance(context!!).waterDao())
        )[HistoryViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_history, container, false)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        with(rcvData) {
            this.adapter = adapterHistory
            addItemDecoration(object : RecyclerView.ItemDecoration(){
                override fun getItemOffsets(
                    outRect: Rect,
                    view: View,
                    parent: RecyclerView,
                    state: RecyclerView.State
                ) {
                    view.setPadding(10,10,10,10)
                }
            })
        }
        historyViewModel.waterData.observe(viewLifecycleOwner, Observer {
            it?.let { adapterHistory.updateData(it) }
        })
    }
}