package com.hackathon.watertestinginstant.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.hackathon.watertestinginstant.R
import com.hackathon.watertestinginstant.appl.INTENT_ACTION_DISCONNECT
import com.hackathon.watertestinginstant.appl.INTENT_CLASS_MAIN_ACTIVITY
import com.hackathon.watertestinginstant.appl.NOTIFICATION_CHANNEL
import com.hackathon.watertestinginstant.appl.NOTIFY_MANAGER_START_FOREGROUND_SERVICE
import com.hackathon.watertestinginstant.ui.main.MainActivity

val FIREBASE_SERVICE = "FireBaseService"
val BLUETOOTH_SERVICE = "BluetootheService"


fun Service.createNotification(notificationMsg: String) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val nc = NotificationChannel(
            NOTIFICATION_CHANNEL,
            BLUETOOTH_SERVICE,
            NotificationManager.IMPORTANCE_LOW
        )
        nc.setShowBadge(false)
        val nm = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        nm.createNotificationChannel(nc)
    }
    val disconnectIntent = Intent()
        .setAction(INTENT_ACTION_DISCONNECT)
    val restartIntent = Intent()
        .setClassName(this, INTENT_CLASS_MAIN_ACTIVITY)
        .setAction(Intent.ACTION_MAIN)
        .addCategory(Intent.CATEGORY_LAUNCHER)
    val disconnectPendingIntent =
        PendingIntent.getBroadcast(this, 1, disconnectIntent, PendingIntent.FLAG_UPDATE_CURRENT)
    val restartPendingIntent =
        PendingIntent.getActivity(this, 1, restartIntent, PendingIntent.FLAG_UPDATE_CURRENT)
    val builder = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL)
        .setSmallIcon(R.drawable.common_google_signin_btn_icon_dark)
        .setColor(resources.getColor(R.color.colorPrimary))
        .setContentTitle(resources.getString(R.string.app_name))
        .setContentText(notificationMsg)
        .setContentIntent(restartPendingIntent)
        .setOngoing(true)
        .addAction(
            NotificationCompat.Action(
                android.R.drawable.alert_light_frame,
                "Disconnect",
                disconnectPendingIntent
            )
        )
    // @drawable/ic_notification created with Android Studio -> New -> Image Asset using @color/colorPrimaryDark as background color
    // Android < API 21 does not support vectorDrawables in notifications, so both drawables used here, are created as .png instead of .xml
    val notification = builder.build()
    startForeground(NOTIFY_MANAGER_START_FOREGROUND_SERVICE, notification)
}

fun Service.createMessageNotification(message: String) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val nc = NotificationChannel(
            NOTIFICATION_CHANNEL,
            BLUETOOTH_SERVICE,
            NotificationManager.IMPORTANCE_LOW
        )
        nc.setShowBadge(false)
        val nm = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        nm.createNotificationChannel(nc)
    }
    val notificationIntent = Intent(this, MainActivity::class.java)
    notificationIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
    val builder = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL)
        .setSmallIcon(R.drawable.common_google_signin_btn_icon_light)
        .setColor(resources.getColor(R.color.colorPrimary))
        .setContentTitle(resources.getString(R.string.app_name))
        .setContentText(message)
        .setContentIntent(
            PendingIntent.getActivity(
                this,
                2,
                notificationIntent,
                PendingIntent.FLAG_NO_CREATE
            )
        )
    // @drawable/ic_notification created with Android Studio -> New -> Image Asset using @color/colorPrimaryDark as background color
    // Android < API 21 does not support vectorDrawables in notifications, so both drawables used here, are created as .png instead of .xml
    val notification = builder.build()
    startForeground(NOTIFY_MANAGER_START_FOREGROUND_SERVICE, notification)
}


fun Context.notificationReceiver() {

}