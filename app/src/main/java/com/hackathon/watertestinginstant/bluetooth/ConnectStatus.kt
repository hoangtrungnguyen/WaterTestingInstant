package com.hackathon.watertestinginstant.bluetooth

sealed class ConnectStatus {

    data class True(var status: String)
    data class Pending(var status: String)
    data class False(var status: String)

}