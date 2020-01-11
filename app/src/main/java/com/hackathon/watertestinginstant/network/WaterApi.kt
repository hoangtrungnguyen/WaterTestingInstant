package com.hackathon.watertestinginstant.network

import com.hackathon.watertestinginstant.network.RetrofitClient.retrofit

object WaterApi {
    val retrofitService : ApiService by lazy { retrofit.create(ApiService::class.java) }
    val service : ClientService by lazy { retrofit.create(ClientService::class.java) }
}