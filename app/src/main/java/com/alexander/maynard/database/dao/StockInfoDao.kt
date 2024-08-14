package com.alexander.maynard.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.alexander.maynard.database.entity.StockInfo

//Student Number: 301170707

@Dao
interface StockInfoDao {
    @Query("SELECT * FROM stock_info WHERE stock_symbol = :stockSymbol")
    suspend fun getStockInfoItem(stockSymbol: String): StockInfo?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertStockInfoItem(stockInfo: StockInfo)

    @Query("DELETE FROM stock_info")
    suspend fun deleteAllFromStockInfo()
}