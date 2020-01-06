package com.hackathon.watertestinginstant.bluetooth

interface SerialListener{
     fun onSerialConnect()
     fun onSerialConnectError(e: Exception)
     fun onSerialRead(data: ByteArray)
     fun onSerialIoError(e: Exception)
}