package com.hackathon.watertestinginstant.data.model

import com.squareup.moshi.Json

data class Message(
    val status: Int,
    @Json(name = "body")
    val message: String
) {
    override fun toString(): String {
        return "Message(status=$status, message='$message')"
    }
}