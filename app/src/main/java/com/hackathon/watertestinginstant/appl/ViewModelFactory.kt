package com.hackathon.watertestinginstant.appl

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.hackathon.watertestinginstant.service.database.WaterDao
import com.hackathon.watertestinginstant.ui.login.LoginViewModel
import com.hackathon.watertestinginstant.ui.main.MainViewModel
import com.hackathon.watertestinginstant.ui.main.history.HistoryViewModel
import com.hackathon.watertestinginstant.ui.main.home.ConnectBluetoothViewModel
import com.hackathon.watertestinginstant.ui.main.home.MapViewModel
import com.hackathon.watertestinginstant.ui.main.profile.ProfileViewModel


/**
 * Factory for ViewModels
 */
class ViewModelFactory (val waterDao: WaterDao): ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(
                mAuth = WaterTestingApplication.mAuth,
                application = WaterTestingApplication.application
            ) as T
        } else if(modelClass.isAssignableFrom(MainViewModel::class.java)){
            return MainViewModel( application = WaterTestingApplication.application, waterDao = waterDao) as T
        } else if (modelClass.isAssignableFrom(HistoryViewModel::class.java))
            return HistoryViewModel(waterDao, WaterTestingApplication.application) as T
        else if (modelClass.isAssignableFrom(ProfileViewModel::class.java))
            return ProfileViewModel(waterDao) as T
        else if (modelClass.isAssignableFrom(ConnectBluetoothViewModel::class.java))
            return ConnectBluetoothViewModel(waterDao,WaterTestingApplication.application) as T
        else if (modelClass.isAssignableFrom(MapViewModel::class.java))
            return MapViewModel(WaterTestingApplication.application) as T
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}