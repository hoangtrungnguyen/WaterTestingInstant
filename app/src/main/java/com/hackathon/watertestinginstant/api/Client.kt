package com.hackathon.watertestinginstant.api

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


object Client {
    fun getProperties(): Flow<String> = flow {
        // flow builder
        val data = WaterApi.retrofitService.getPropertiesAsync().await()
        emit(data)
    }

}
