package com.hackathon.watertestinginstant.network

import com.google.android.gms.maps.model.LatLng
import com.hackathon.watertestinginstant.data.model.User
import com.hackathon.watertestinginstant.data.model.WaterData
import com.hackathon.watertestinginstant.data.respone.Noti
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import java.util.*

interface ClientService {
    @POST("save")
    fun sendDeviceId(@Body message: String): Call<String>


    @POST("/get-noti")
    fun receiveData(@Body idUser: String): Call<List<Noti>>

    @POST("/get-noti")
    fun receiveData2(@Body idUser: String): Call<String>

    @POST
    fun nearby(@Body loc: LatLng) : Call<List<LatLng>>

    @POST("save")
    fun signUp(@Body user: User): Call<String>


    @POST("save")
    fun sendData(@Body request: WaterData):Call<String>

    @POST
    fun receiveLoc(@Body request: String):Call<Objects>

}