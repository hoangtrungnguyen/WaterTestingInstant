package com.hackathon.watertestinginstant.data.model

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
data class User(
    val userId: String = "",
    val email: String = "",
    val name: String = "",
    val pass: String = "12345678",
    val address: String = "",
    val phone: String = ""
)

enum class Address{
    CAU_GIAY
}
