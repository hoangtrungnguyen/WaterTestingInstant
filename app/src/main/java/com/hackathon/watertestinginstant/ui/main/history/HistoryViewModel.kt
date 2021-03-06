package com.hackathon.watertestinginstant.ui.main.history

import android.util.Log
import androidx.lifecycle.*
import com.google.android.gms.maps.model.LatLng
import com.hackathon.watertestinginstant.appl.WaterTestingApplication
import com.hackathon.watertestinginstant.data.model.WaterData
import com.hackathon.watertestinginstant.service.database.WaterDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

enum class State {
    LOADING,
    FINISH
}

class HistoryViewModel(val waterDao: WaterDao, val application: WaterTestingApplication) :
    AndroidViewModel(application) {

    private val _text = MutableLiveData<String>().apply {
        value = "This is dashboard Fragment"
    }
    val text: LiveData<String> = _text

    val state: MutableLiveData<State> = MutableLiveData<State>()

    val dbRef = WaterTestingApplication.fireBaseDB.reference

    val store = WaterTestingApplication.fireBaseFireStore

    val userId: String? = WaterTestingApplication.mAuth.uid

    init {
    }

    val waterData = waterDao.getAll()


    fun getHistoryServer() {
        userId ?: return

        val docRef = store.collection("User").document(userId).collection("Noti")
            .get()
            .addOnSuccessListener { notis ->
                val docSnap = notis.documents
                for (doc in docSnap) {
                    val obj = doc.data

                    Log.d("fffff", obj.toString())
                }
                viewModelScope.launch {
                    withContext(Dispatchers.IO) {
                        val e = docSnap.map { it ->
                            val data = it.data
                            waterDao.insert(
                                WaterData(
                                    PH = (data?.get("ph") as? String)?.toDouble() ?: 0.0,
                                    TDS = (data?.get("tds") as? String)?.toDouble() ?: 0.0,
                                    Turbidity = (data?.get("turbidity") as? String)?.toDouble() ?: 0.0,
                                    date = (data?.get("date") as? String)?.toString() ?: "NULL",
                                    latLng = LatLng((data?.get("latitude") as? String)?.toDouble() ?: 0.0,(data?.get("longtitude") as? String)?.toDouble() ?: 0.0)

                                )
                            )
                        }
                        state.postValue(State.FINISH)
                    }
                }
            }
            .addOnFailureListener { exception ->
                Log.w("fffff", "Error getting documents: ", exception)
                state.postValue(State.FINISH)
            }

    }
}