package com.alexander.maynard.database.repository

import com.alexander.maynard.database.dao.StockInfoDao
import com.alexander.maynard.database.entity.StockInfo

//Student Number: 301170707

//StockInfoRepository to interact with the StockInfoDao and later be interacted through the StockInfoViewModel
class StockInfoRepository(private val stockInfoDao: StockInfoDao) {

    //get (return) a StockInfo item (object) by passing the stockSymbol string
    suspend fun getStockInfoItem(stockSymbol: String): StockInfo? {
        return stockInfoDao.getStockInfoItem(stockSymbol)
    }

    //insert a StockInfo item by a passing a StockInfo object
    suspend fun insertStockInfoItem(stockInfo: StockInfo) {
        stockInfoDao.insertStockInfoItem(stockInfo)
    }

    /*delete all stock info objects in the database
    (this is to reset the database to make the Hands-On
    Test 2 more testable each time that it is started)*/
    suspend fun deleteAllFromStockInfo() {
        stockInfoDao.deleteAllFromStockInfo()
    }
}