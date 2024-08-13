package com.alexander.maynard.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.alexander.maynard.database.dao.StockInfoDao
import com.alexander.maynard.database.entity.StockInfo

//database for the application. uses an array of only StockInfo to build the application database
@Database(entities = arrayOf(StockInfo::class), version = 1, exportSchema = false)
abstract class StockInfoDatabase: RoomDatabase() {

    //abstract stockInfoDao reference to be used by the view models (and repositories)
    abstract fun stockInfoDao(): StockInfoDao

    //make sure that there is only one instance of the database, and to make sure to build the database if there is no instance
    companion object {
        @Volatile
        private var INSTANCE: StockInfoDatabase? = null
        fun getDatabase(context: Context): StockInfoDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    StockInfoDatabase::class.java,
                    "stock_info_database")
                    .addMigrations()
                    .build()
                INSTANCE = instance

                instance
            }
        }
    }
}