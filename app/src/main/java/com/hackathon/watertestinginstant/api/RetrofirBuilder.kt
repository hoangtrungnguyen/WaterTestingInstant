package com.hackathon.watertestinginstant.api

import com.hackathon.watertestinginstant.appl.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory


object RetrofitClient {

    val retrofit: Retrofit by lazy {
        getClient(BASE_URL)
    }

    private fun getClient(baseUrl: String): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
    }
}