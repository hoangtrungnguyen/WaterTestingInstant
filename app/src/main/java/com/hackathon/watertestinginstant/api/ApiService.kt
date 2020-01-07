package com.hackathon.watertestinginstant.api

import com.squareup.moshi.Json
import io.reactivex.Observable
import kotlinx.coroutines.Deferred
import retrofit2.http.GET

interface ApiService {
    @GET("realestate")
    fun getPropertiesAsync(): Deferred<List<MarsProperty>>

    @GET("save")
    fun saveTestingData()
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