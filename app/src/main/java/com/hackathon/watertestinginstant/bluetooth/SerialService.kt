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
public class SerialService : Service(), SerialListener {

    private val mainLooper: Handler
    private val binder: IBinder
    private val queue1: Queue<QueueItem>
    private val queue2: Queue<QueueItem>

    private var listener: SerialListener? = null
    private var connected: Boolean = false
    private var notificationMsg: String? = null

    internal inner class SerialBinder : Binder() {
        val service: SerialService
            get() = this@SerialService
    }

    private enum class QueueType {
        Connect, ConnectError, Read, IoError
    }

    private inner class QueueItem internal constructor(internal var type: QueueType, internal var data: ByteArray, internal var e: Exception)

    /**
     * Lifecylce
     */
    init {
        mainLooper = Handler(Looper.getMainLooper())
        binder = SerialBinder()
        queue1 = LinkedList()
        queue2 = LinkedList()
    }

    override fun onDestroy() {
        cancelNotification()
        disconnect()
        super.onDestroy()
    }

    override fun onBind(intent: Intent): IBinder? {
        return binder
    }

    /**
     * Api
     */
    fun connect(listener: SerialListener, notificationMsg: String) {
        this.listener = listener
        connected = true
        this.notificationMsg = notificationMsg
    }

    fun disconnect() {
        listener = null
        connected = false
        notificationMsg = null
    }

    fun attach(listener: SerialListener) {
        kotlin.require(!(Looper.getMainLooper().thread !== Thread.currentThread())) { "not in main thread" }
        cancelNotification()
        // use synchronized() to prevent new items in queue2
        // new items will not be added to queue1 because mainLooper.post and attach() run in main thread
        if (connected) {
            synchronized(this) {
                this.listener = listener
            }
        }
        for (item in queue1) {
            when (item.type) {
                SerialService.QueueType.Connect -> listener.onSerialConnect()
                SerialService.QueueType.ConnectError -> listener.onSerialConnectError(item.e)
                SerialService.QueueType.Read -> listener.onSerialRead(item.data)
                SerialService.QueueType.IoError -> listener.onSerialIoError(item.e)
            }
        }
        for (item in queue2) {
            when (item.type) {
                SerialService.QueueType.Connect -> listener.onSerialConnect()
                SerialService.QueueType.ConnectError -> listener.onSerialConnectError(item.e)
                SerialService.QueueType.Read -> listener.onSerialRead(item.data)
                SerialService.QueueType.IoError -> listener.onSerialIoError(item.e)
            }
        }
        queue1.clear()
        queue2.clear()
    }

    fun detach() {
        if (connected)
            createNotification(notificationMsg ?: "No Message")
        // items already in event queue (posted before detach() to mainLooper) will end up in queue1
        // items occurring later, will be moved directly to queue2
        // detach() and mainLooper.post run in the main thread, so all items are caught
        listener = null
    }


    private fun cancelNotification() {
        stopForeground(true)
    }

    /**
     * SerialListener
     */
    override fun onSerialConnect() {
        if (connected) {
            synchronized(this) {
                if (listener != null) {
                    mainLooper.post {
                        if (listener != null) {
                            listener!!.onSerialConnect()
                        } else {
                            queue1.add(QueueItem(QueueType.Connect, ByteArray(0), java.lang.Exception()))
                        }
                    }
                } else {
                    queue2.add(QueueItem(QueueType.Connect, ByteArray(0), java.lang.Exception()))
                }
            }
        }
    }

    override fun onSerialConnectError(e: Exception) {
        if (connected) {
            synchronized(this) {
                if (listener != null) {
                    mainLooper.post {
                        if (listener != null) {
                            listener!!.onSerialConnectError(e)
                        } else {
                            queue1.add(QueueItem(QueueType.ConnectError, ByteArray(0), e))
                            cancelNotification()
                            disconnect()
                        }
                    }
                } else {
                    queue2.add(QueueItem(QueueType.ConnectError, ByteArray(0), e))
                    cancelNotification()
                    disconnect()
                }
            }
        }
    }

    override fun onSerialRead(data: ByteArray) {
        if (connected) {
            synchronized(this) {
                if (listener != null) {
                    mainLooper.post {
                        if (listener != null) {
                            listener!!.onSerialRead(data)
                        } else {
                            queue1.add(QueueItem(QueueType.Read, data, java.lang.Exception()))
                            createMessageNotification(data.toString())
                        }
                    }
                } else {
                    queue2.add(QueueItem(QueueType.Read, data, java.lang.Exception()))
                    createMessageNotification(data.toString())
                }
            }
        }
    }

    override fun onSerialIoError(e: Exception) {
        if (connected) {
            synchronized(this) {
                if (listener != null) {
                    mainLooper.post {
                        if (listener != null) {
                            listener!!.onSerialIoError(e)
                        } else {
                            queue1.add(QueueItem(QueueType.IoError, ByteArray(0), e))
                            cancelNotification()
                            disconnect()
                        }
                    }
                } else {
                    queue2.add(QueueItem(QueueType.IoError, ByteArray(0), e))
                    cancelNotification()
                    disconnect()
                }
            }
        }
    }

}
