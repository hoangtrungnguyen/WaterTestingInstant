package com.hackathon.watertestinginstant.ui.main.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {

    private val _drinkPercent = MutableLiveData<Double>()
    val drinkPercent: LiveData<Double>
        get() = _drinkPercent


    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text

}