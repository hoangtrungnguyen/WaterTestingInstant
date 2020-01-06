package com.hackathon.watertestinginstant.ui.main

import android.bluetooth.BluetoothDevice
import android.os.Handler
import android.os.Looper
import android.os.Message
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.hackathon.watertestinginstant.appl.WaterTestingApplication
import com.hackathon.watertestinginstant.bluetooth.ConnectStatus
import com.hackathon.watertestinginstant.bluetooth.SerialSocket
import com.hackathon.watertestinginstant.ui.util.useCancellably
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withTimeout
import java.io.Closeable
import java.net.ServerSocket
import kotlin.coroutines.resume

class MainViewModel(val application: WaterTestingApplication) :
    AndroidViewModel(application) {

    private val mainLooper: Handler = object : Handler(Looper.getMainLooper()) {
        /*
         * handleMessage() defines the operations to perform when
         * the Handler receives a new Message to process.
         */
        override fun handleMessage(inputMessage: Message) {
            val result = inputMessage.obj as? Result<*>?

        }
    }

    private val _status = MutableLiveData<ConnectStatus>()
    val status: LiveData<ConnectStatus> = _status

    private val _data = MutableLiveData<Result<ByteArray>>()
    val data: LiveData<Result<ByteArray>> = _data
    fun postResult(data: Result<ByteArray>) {
        _data.postValue(data)
    }

    var serialSocket: SerialSocket? = null

    init {

    }

    fun postStatus(status: ConnectStatus) {
        _status.postValue(status)
    }

    fun connect(device: BluetoothDevice) {
        try {
            serialSocket = SerialSocket()
            _status.postValue(ConnectStatus.Pending("Connecting"))
            serialSocket?.connect(application, this, device)
        } catch (e: Exception) {
            _status.postValue(e.message)
        }
    }

    fun coroutinConnect() {
    }

    fun disconnect() {
        serialSocket?.disconnect()
    }


    /**
     * SerialListener
     */
//    fun onSerialConnect() {
//        if (_state.value == ConnectStatus.True) {
//            synchronized(this) {
//                if (listener != null) {
//                    mainLooper.post {
//                        if (listener != null) {
//                            listener!!.onSerialConnect()
//                        } else {
//                            queue1.add(
//                                QueueItem(
//                                    QueueType.Connect,
//                                    ByteArray(0),
//                                    java.lang.Exception()
//                                )
//                            )
//                        }
//                    }
//                } else {
//                    queue2.add(QueueItem(QueueType.Connect, ByteArray(0), java.lang.Exception()))
//                }
//            }
//        }
//    }


//     fun onSerialConnectError(e: Exception) {
//        if (_state.value == ConnectStatus.True) {
//            synchronized(this) {
//                    mainLooper.post {
//                        if (listener != null) {
//                            listener!!.onSerialConnectError(e)
//                        } else {
//                            queue1.add(QueueItem(QueueType.ConnectError, ByteArray(0), e))
//                            cancelNotification()
//                            disconnect()
//                        }
//                    }
//                } else {
//                    queue2.add(QueueItem(QueueType.ConnectError, ByteArray(0), e))
//                    cancelNotification()
//                    disconnect()
//                }
//            }
//        }
//    }


    fun onSerialRead(data: ByteArray) {
        if (_status.value is ConnectStatus.True) {
            synchronized(this) {
                mainLooper.post {  }
            }
        }
    }


//     fun onSerialIoError(e: Exception) {
//        if (_state.value == ConnectStatus.True) {
//            synchronized(this) {
//                if (listener != null) {
//                    mainLooper.post {
//                        if (listener != null) {
//                            listener!!.onSerialIoError(e)
//                        } else {
//                            queue1.add(QueueItem(QueueType.IoError, ByteArray(0), e))
//                            cancelNotification()
//                            disconnect()
//                        }
//                    }
//                } else {
//                    queue2.add(QueueItem(QueueType.IoError, ByteArray(0), e))
//                    cancelNotification()
//                    disconnect()
//                }
//            }
//        }
//    }
}