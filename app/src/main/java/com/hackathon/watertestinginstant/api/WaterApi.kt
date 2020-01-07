package com.hackathon.watertestinginstant.api

import com.hackathon.watertestinginstant.api.RetrofitClient.retrofit

object WaterApi {
    val retrofitService : ApiService by lazy { retrofit.create(ApiService::class.java) }
}