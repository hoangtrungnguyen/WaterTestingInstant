package com.hackathon.watertestinginstant.appl

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.hackathon.watertestinginstant.R
import com.hackathon.watertestinginstant.data.model.WaterData
import com.hackathon.watertestinginstant.database.AppDataBase
import com.hackathon.watertestinginstant.network.WaterApi
import com.hackathon.watertestinginstant.util.FIREBASE_SERVICE
import com.hackathon.watertestinginstant.ui.login.LoginActivity
import com.hackathon.watertestinginstant.ui.main.MainActivity
import com.hackathon.watertestinginstant.ui.main.PACKAGE_MAIN
import com.hackathon.watertestinginstant.ui.splash.SplashActivity

const val DEVICE_TOKEN = "DeviceToken"
const val NOTIFICATION_DATA = "notificationData"

class FirebaseMessageService : FirebaseMessagingService() {
    private val TAG = "FireBase"


    companion object {
        fun getToken(context: Context): String {
            return context.getSharedPreferences(DEVICE_TOKEN, MODE_PRIVATE).getString(
                DEVICE_TOKEN, "NULL"
            ) ?: ""
        }
    }


    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        // [START_EXCLUDE]
        // There are two types of messages data messages and notification messages. Data messages are handled
        // here in onMessageReceived whether the app is in the foreground or background. Data messages are the type
        // traditionally used with GCM. Notification messages are only received here in onMessageReceived when the app
        // is in the foreground. When the app is in the background an automatically generated notification is displayed.
        // When the user taps on the notification they are returned to the app. Messages containing both notification
        // and data payloads are treated as notification messages. The Firebase console always sends notification
        // messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options
        // [END_EXCLUDE]

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: ${remoteMessage.from}")

        // Check if message contains a data payload.
        remoteMessage.data.isNotEmpty().let {
            Log.d(TAG, "Message data payload: " + remoteMessage.data)
            val waterData = WaterData(
                PH = remoteMessage.data["ph"]?.toDouble() ?: 7.0,
                Turbidity = remoteMessage.data["turbidity"]?.toDouble() ?: 0.0,
                TDS = remoteMessage.data["tds"]?.toDouble() ?: 0.0,
                date = remoteMessage.data["time"] ?: "Null"
            )
            AppDataBase.getInstance(this).waterDao().insert(waterData)
        }
            // Check if message contains a notification payload.
        remoteMessage.notification?.let {
            Log.d(TAG, "Message Notification Body: ${it.body}")
        }
        // if id notification == this user id
        remoteMessage.let {
            sendNotification(it.notification?.body ?: "NULL ", HashMap(it.data))
        }

    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("FireBase", "New token $token")
        getSharedPreferences(DEVICE_TOKEN, MODE_PRIVATE).edit().putString(DEVICE_TOKEN, token)
            .apply();
    }


    /**
     * Long running work
     * Schedule async work using WorkManager.
     */
    private fun scheduleJob() {
        // [START dispatch_job]
//        val work = OneTimeWorkRequest.Builder(MyWorker::class.java).build()
//        WorkManager.getInstance().beginWith(work).enqueue()
        // [END dispatch_job]
    }

    private fun handleNow() {

    }

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageBody FCM message body received.
     */
    private fun sendNotification(messageBody: String, data: HashMap<String, String>) {
        val intent = Intent(
            this,
            if (WaterTestingApplication.mAuth.currentUser == null) LoginActivity::class.java else MainActivity::class.java
        )

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.putExtra(NOTIFICATION_DATA, data)
        val pendingIntent = PendingIntent.getActivity(
            this, 0 /* Request code */, intent,
            PendingIntent.FLAG_ONE_SHOT
        )

        val channelId = FIREBASE_SERVICE

        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.common_google_signin_btn_icon_dark)
            .setContentTitle("Message from server")
            .setContentText(messageBody)
            .setAutoCancel(true)
//            .setVibrate(LongArray(1,300,1000))
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Channel human readable title",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build())
    }


}