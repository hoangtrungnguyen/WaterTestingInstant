package com.hackathon.watertestinginstant.ui.main.home

import android.bluetooth.BluetoothAdapter
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.ivbaranov.rxbluetooth.BluetoothConnection
import com.hackathon.watertestinginstant.appl.BLUETOOTH_SPP
import com.hackathon.watertestinginstant.appl.WaterTestingApplication.Companion.rxBleClient
import com.hackathon.watertestinginstant.bluetooth.ConnectStatus
import com.polidea.rxandroidble2.RxBleConnection
import com.polidea.rxandroidble2.RxBleDevice
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable


class ConnectBluetoothViewModel(val macAddress: String) : ViewModel() /*, SerialListener*/ {
    val compositeDisposable = CompositeDisposable()
    val data = MutableLiveData<String>()
    val status = MutableLiveData<String>()
    val connected = MutableLiveData<ConnectStatus>()
    val bleDevice : RxBleDevice

    init {
        bleDevice = rxBleClient.getBleDevice(macAddress)
    }


    fun connect(deviceAddress: String?) {
        try {
            status.postValue("connecting")
            val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
            val device = bluetoothAdapter.getRemoteDevice(deviceAddress)
            val deviceName = if (device.name != null) device.name else device.address
            val bluetoothSocket = device.createRfcommSocketToServiceRecord(BLUETOOTH_SPP);
            val bluetoothConnection = BluetoothConnection(bluetoothSocket)

            // Observe every byte received
//            compositeDisposable.add(
//                bluetoothConnection.observeStringStream()
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribeOn(Schedulers.io())
//                    .subscribe({
//                        data.postValue(it)
//                    }, {
//                        status.postValue("connection failed rx: ${it.message}")
//                    })
//            )


        } catch (e: Exception) {
            status.postValue("connection failed: " + e.message)
        }

    }

    fun write(macAddress: String, message:String){
    }

    private fun prepareConnectionObservable(): Observable<RxBleConnection> = bleDevice
            .establishConnection(false)
//            .takeUntil(disconnectTriggerSubject)
//            .compose(ReplayingShare.instance())

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }


    /*synchronized sẽ chỉ được gọi một lần vào một thời điểm nhất định.
    Nếu có hai thread cùng gọi phương thức
    thì Java sẽ ưu tiên xử lý ở thread nào tới trước, thread tới sau sẽ bị tạm thời dừng lại
    cho tới khi thread1 xử lý xong.*/
    /**
     * SerialListener
     */
//    override fun onSerialConnect() {
//        if (connected) {
//            synchronized(this) {
//                if (listener != null) {
//                    mainLooper.post {
//                        if (listener != null) {
//                            listener!!.onSerialConnect()
//                        } else {
//                            queue1.add(QueueItem(QueueType.Connect, ByteArray(0), java.lang.Exception()))
//                        }
//                    }
//                } else {
//                    queue2.add(QueueItem(QueueType.Connect, ByteArray(0), java.lang.Exception()))
//                }
//            }
//        }
//    }
//
//    override fun onSerialConnectError(e: Exception) {
//        if (connected) {
//            synchronized(this) {
//                if (listener != null) {
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
//
//    override fun onSerialRead(data: ByteArray) {
//        if (connected) {
//            synchronized(this) {
//                if (listener != null) {
//                    mainLooper.post {
//                        if (listener != null) {
//                            listener!!.onSerialRead(data)
//                        } else {
//                            queue1.add(QueueItem(QueueType.Read, data, java.lang.Exception()))
//                        }
//                    }
//                } else {
//                    queue2.add(QueueItem(QueueType.Read, data, java.lang.Exception()))
//                }
//            }
//        }
//    }
//
//    override fun onSerialIoError(e: Exception) {
//        if (connected) {
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