package com.hackathon.watertestinginstant.appl

import com.hackathon.watertestinginstant.BuildConfig
import java.util.*

// values have to be globally unique
public val INTENT_ACTION_DISCONNECT = BuildConfig.APPLICATION_ID + ".Disconnect"
public val NOTIFICATION_CHANNEL = BuildConfig.APPLICATION_ID + ".Channel"
public val INTENT_CLASS_MAIN_ACTIVITY = BuildConfig.APPLICATION_ID + ".MainActivity"
public val NOTIFY_MANAGER_START_FOREGROUND_SERVICE = 1001

val BLUETOOTH_SPP = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")