package com.hackathon.watertestinginstant.bluetooth

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import com.hackathon.watertestinginstant.notification.createMessageNotification
import com.hackathon.watertestinginstant.notification.createNotification
import java.util.*

/**
 * create notification and queue serial data while activity is not in the foreground
 * use listener chain: SerialSocket -> SerialService -> UI fragment
 */

public val BLUETOOTH_SERVICE_ID = 88888
public class SerialService : Service() {
    override fun onBind(intent: Intent?): IBinder? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}
