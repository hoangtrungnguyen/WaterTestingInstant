package com.hackathon.watertestinginstant.ui.util
fun ByteArray.toHex() = joinToString("") { String.format("%02X", (it.toInt() and 0xff)) }

/**
 * Converts an UTF-8 array into a [String]. Replaces invalid input sequences with a default character.
 */