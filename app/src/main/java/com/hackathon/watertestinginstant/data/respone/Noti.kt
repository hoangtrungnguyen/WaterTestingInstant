package com.hackathon.watertestinginstant.data.respone

import com.squareup.moshi.Json

data class Noti(
    @field:Json(name = "id")
    val time: Long = 0,
    @field:Json(name = "data")
    val data: Data = Data()
) {
    override fun toString(): String {
        return "Noti(time=$time, data=$data)"
    }
}

data class Data(
    @field:Json(name = "date")
    val date: String = "Mon, 01-01-2019",
    @field:Json(name = "tds")
    val tds: Int = 0,
    @field:Json(name = "ph")
    val ph: Double = 0.0,
    @field:Json(name = "turbidity")
    val turbidity: Int = 0
)