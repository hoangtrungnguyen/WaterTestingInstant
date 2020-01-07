package com.hackathon.watertestinginstant.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.hackathon.watertestinginstant.data.model.WaterData
import java.util.*


@Dao
interface WaterDao {
    @Insert
    fun insert(data: WaterData)

    @Query("DELETE FROM WaterDataTable")
    fun deleteAll()

    @Query("SELECT * FROM WaterDataTable")
    fun getAll(): LiveData<List<WaterData>>

    @Query("SELECT * FROM WaterDataTable ")
    fun getDataBetween(from: Date, to: Date): List<WaterData>

}