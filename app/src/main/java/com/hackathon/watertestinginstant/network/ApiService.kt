package com.hackathon.watertestinginstant.network

import com.squareup.moshi.Json
import kotlinx.coroutines.Deferred
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*
import java.util.*

interface ApiService {
    @GET("realestate")
     fun getPropertiesAsync(): Deferred<List<MarsProperty>>

//    @Headers("Content-Type: text/html")
    @POST("save")
    suspend fun saveTestingData(@Body data: String):Response<String>

    @POST("save")
    fun saveTestingData(@Body requestBody: RequestBody):Deferred<String>

    @POST("")
    fun getSample(): Deferred<String>

    @POST()
    fun requestBody(@Body requestBody: RequestBody):Deferred<String>

}
/**
 * This data class defines a Mars property which includes an ID, the image URL, the type (sale
 * or rental) and the price (monthly if it's a rental).
 * The property names of this data class are used by Moshi to match the names of values in JSON.
 */
data class MarsProperty(
    val id: String,
    // used to map img_src from the JSON to imgSrcUrl in our class
    @Json(name = "img_src") val imgSrcUrl: String,
    val type: String,
    val price: Double)