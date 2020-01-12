package com.hackathon.watertestinginstant.ui.main.history

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.hackathon.watertestinginstant.R
import com.hackathon.watertestinginstant.data.model.WaterData
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.zip.Inflater

class HistoryAdapter(val listener: (item: WaterData) -> Unit) :
    RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {
    var _data = mutableListOf<WaterData>()
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

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(_data[position], listener)


    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        fun bind(item: WaterData, listener: (item: WaterData) -> Unit) {
            view.setOnClickListener { listener(item) }
            val tvDate: TextView = view.findViewById(R.id.date)
            val tvHP: TextView = view.findViewById(R.id.ph)
            val tvTurbidity: TextView = view.findViewById(R.id.turbidity)
            val tvTDS: TextView = view.findViewById(R.id.tds)
            tvTurbidity.text = String.format("%.2f", item.Turbidity)
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    tvDate.text =
                        LocalDate.ofEpochDay(item.time).format(DateTimeFormatter.RFC_1123_DATE_TIME)
                } else {
                    tvDate.text = "Sunday, 17-01-2020"
                }
            } catch (e: Exception) {

            }
            if(item.PH < 2) tvHP.text = "NaN"
            else tvHP.text = String.format("%.2f", item.PH)
            tvTDS.text = String.format("%.2f", item.TDS)
        }
    }
}