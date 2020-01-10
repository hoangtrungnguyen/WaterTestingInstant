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
import java.time.format.DateTimeFormatter
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

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(_data[position])


    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(item: WaterData) {
            val tvDate: TextView = view.findViewById(R.id.date)
            val tvHP: TextView = view.findViewById(R.id.ph)
            val tvTurbidity: TextView = view.findViewById(R.id.turbidity)
            val tvTDS: TextView = view.findViewById(R.id.tds)
            tvTurbidity.text = String.format("%.2f", item.Turbidity)
            try {
                tvDate.text =
                    LocalDate.ofEpochDay(item.time).format(DateTimeFormatter.RFC_1123_DATE_TIME)
            }catch (e:Exception){

            }
            tvHP.text = String.format("%.2f", item.PH)
            tvTDS.text = String.format("%.2f", item.TDS)
        }
    }
}