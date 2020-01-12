package com.hackathon.watertestinginstant.service.database

import android.content.Context
import android.content.SharedPreferences
import com.hackathon.watertestinginstant.appl.WaterTestingApplication

interface Cache {
    val size: Int

    operator fun set(key: Any, value: Any)

    operator fun get(key: Any): Any?

    fun remove(key: Any): Any?

    fun clear()
}

//Key _
val deviceId = "deviceId"
val userId = "userId"


class WaterTestingCache : Cache {
    private val PreferenceName = "WaterTestingInstant"
    private var editor: SharedPreferences.Editor? = null
    private var preferences: SharedPreferences? = null

    companion object {
        private var _instance: WaterTestingCache? = null
        @Synchronized
        fun getInstance(): WaterTestingCache? {
            if (_instance == null) _instance = WaterTestingCache()
            return _instance
        }
    }


    init {
        //do nothing
        preferences = WaterTestingApplication.application.applicationContext
            .getSharedPreferences(
                PreferenceName,
                Context.MODE_PRIVATE
            )
        editor = preferences?.edit()
    }



    private val cache = HashMap<Any, Any>()

    override val size: Int
        get() = cache.size

    override fun set(key: Any, value: Any) {
        this.cache[key] = value
    }

    override fun remove(key: Any) = this.cache.remove(key)

    override fun get(key: Any) = this.cache[key]

    override fun clear() = this.cache.clear()
}