package com.hackathon.watertestinginstant.ui.main.history

import android.os.Build
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.hackathon.watertestinginstant.database.WaterDao
import java.time.LocalDate

class HistoryViewModel(val waterDao: WaterDao) : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is dashboard Fragment"
    }
    val text: LiveData<String> = _text


    val waterData = waterDao.getAll()


    val waterDataString = Transformations.map(waterData) {
        it.map {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                "The quality of the water:\nPH: ${it.PH}\nTurbidity: ${it.Turbidity}\nTDS${it.TDS}\n" +
                        "at ${LocalDate.ofEpochDay(it.time)}"
            } else {
                "The quality of the water:\nPH: ${it.PH}\nTurbidity: ${it.Turbidity}\nTDS${it.TDS}\n" +
                        "at ${it.time}"
            }
        }
    }
}