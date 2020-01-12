package com.hackathon.watertestinginstant.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.Exclude
import java.io.Serializable
import java.lang.Exception
import java.util.*


@Entity(tableName = "WaterDataTable")
data class WaterData(
    val PH: Double = 7.0,
    val Turbidity: Double = 0.0,
    val TDS: Double = 0.0,
    val date:String = "Tues, 02-01-2019",
    val latLng: LatLng = LatLng(0.0,0.0),
    @PrimaryKey
    @Exclude
    val time: Long = System.currentTimeMillis()
) : Serializable{
    override fun toString(): String {
        return "WaterData(PH=$PH, Turbidity=$Turbidity, TDS=$TDS, date='$date', latLng=$latLng, time=$time)"
    }
}

class Converters {
    @TypeConverter
    fun fromLatLngToString(value: LatLng?): String? {
        return value?.let { "Lat:${value.latitude}Lng:${value.longitude}" }
    }

    @TypeConverter
    fun fromStringToLatLng(value: String?): LatLng? {
        try {

            val a = value?.split("L[atng]{2}:")
            val lat = a?.get(0)?.toDouble() ?: 0.0
            val lng = a?.get(0)?.toDouble() ?: 0.0
            return LatLng(lat,lng)
        }catch (e : Exception){
            return LatLng(0.0,0.0)
        }
    }
}