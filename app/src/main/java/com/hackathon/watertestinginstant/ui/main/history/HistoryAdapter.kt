package com.hackathon.watertestinginstant.ui.main.history

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hackathon.watertestinginstant.R
import com.hackathon.watertestinginstant.data.model.WaterData
import java.time.LocalDate
import java.util.zip.Inflater

class HistoryAdapter : RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {
    private var _data = mutableListOf<WaterData>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }
        get() = field


    fun updateData(data: List<WaterData>) {
        _data.clear()
        _data.addAll(data)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater
            .inflate(R.layout.item_history, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int = _data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(_data[position])


    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        fun bind(item: WaterData) {
            val tvDate: TextView = view.findViewById(R.id.date)
            val tvHP : TextView = view.findViewById(R.id.ph)
            val tvTurbidity: TextView = view.findViewById(R.id.turbidity)
            val tvTDS: TextView = view.findViewById(R.id.tds)
            tvTurbidity.text = item.Turbidity.toString()
            tvDate.text = item.time.toString()
            tvHP.text = item.PH.toString()
            tvTDS.text = item.TDS.toString()
        }
    }
}