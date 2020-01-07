package com.hackathon.watertestinginstant.api

import io.reactivex.Observable
import kotlinx.coroutines.Deferred
import retrofit2.http.GET

interface ApiService {
    @GET("realestate")
    fun getPropertiesAsync(): Deferred<String>
}