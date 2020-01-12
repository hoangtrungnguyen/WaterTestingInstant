package com.hackathon.watertestinginstant

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.hackathon.watertestinginstant.service.network.ApiService
import com.hackathon.watertestinginstant.service.network.WaterApi

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.hackathon.watertestinginstant", appContext.packageName)
    }

    lateinit var apiService: ApiService
    @Before
    fun prepare() {
        apiService = WaterApi.retrofitService
    }
    @Test
    fun save(){
    }
}
