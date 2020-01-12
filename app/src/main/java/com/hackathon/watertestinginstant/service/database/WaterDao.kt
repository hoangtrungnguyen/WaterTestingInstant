package com.hackathon.watertestinginstant.service.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.hackathon.watertestinginstant.data.model.WaterData


@Dao
interface WaterDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(data: WaterData)

    @Query("DELETE FROM WaterDataTable")
    fun deleteAll()

    @Query("SELECT * FROM WaterDataTable ORDER BY time DESC")
    fun getLatest(): LiveData<WaterData>

    @Query("SELECT * FROM WaterDataTable")
    fun getAll(): LiveData<List<WaterData>>

    @Query("SELECT * FROM WaterDataTable WHERE time between :from and :to")
    fun getDataBetween(from: Long, to: Long): List<WaterData>?

}