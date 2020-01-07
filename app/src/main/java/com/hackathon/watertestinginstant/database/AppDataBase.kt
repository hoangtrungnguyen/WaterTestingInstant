package com.hackathon.watertestinginstant.database

import android.content.Context
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteDatabase
import com.hackathon.watertestinginstant.data.model.Converters
import com.hackathon.watertestinginstant.data.model.WaterData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random


@Database(entities = [WaterData::class], version = 1)
//@TypeConverters(Converters::class)
abstract class AppDataBase : RoomDatabase() {
    abstract fun waterDao(): WaterDao

    companion object {
        @Volatile
        private var INSTANCE: AppDataBase? = null

        fun getInstance(context: Context): AppDataBase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        AppDataBase::class.java,
                        "water_testing_history_database"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }

    private class WordDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {

        override fun onOpen(db: SupportSQLiteDatabase) {
            super.onOpen(db)
            INSTANCE?.let { database ->
                scope.launch {
                    var wordDao = database.waterDao()

                    // Delete all content here.
                    wordDao.deleteAll()

                    // Add sample words.
                    var word = WaterData(TDS = Random(10).nextDouble())
                    wordDao.insert(word)
                    word = WaterData(TDS = Random(15).nextDouble(), PH = Random(7).nextDouble())
                    wordDao.insert(word)
                }
            }
        }
    }

}