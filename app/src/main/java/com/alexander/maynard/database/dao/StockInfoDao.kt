package com.alexander.maynard.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.alexander.maynard.database.entity.StockInfo

//Student Number: 301170707

//StockInfo Dao to interact with StockInfo (entity) table in the "stock_info_database" (room database)
@Dao
interface StockInfoDao {
    //get a stock info object by searching by the stock symbol
    @Query("SELECT * FROM stock_info WHERE stock_symbol = :stockSymbol")
    suspend fun getStockInfoItem(stockSymbol: String): StockInfo?

    //insert a stock info item by passing a StockInfo object
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertStockInfoItem(stockInfo: StockInfo)

    /*delete all stock info objects in the database
    (this is to reset the database to make the Hands-On
    Test 2 more testable each time that it is started)*/
    @Query("DELETE FROM stock_info")
    suspend fun deleteAllFromStockInfo()
}