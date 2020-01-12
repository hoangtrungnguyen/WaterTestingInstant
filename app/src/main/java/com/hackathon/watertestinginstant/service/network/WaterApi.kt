package com.hackathon.watertestinginstant.service.network

import com.hackathon.watertestinginstant.service.network.RetrofitClient.retrofit

object WaterApi {
    val retrofitService : ApiService by lazy { retrofit.create(ApiService::class.java) }
    val service : ClientService by lazy { retrofit.create(ClientService::class.java) }
}