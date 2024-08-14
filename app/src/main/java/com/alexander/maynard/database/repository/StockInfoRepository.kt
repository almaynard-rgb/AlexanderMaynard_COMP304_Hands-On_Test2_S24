package com.alexander.maynard.database.repository

import com.alexander.maynard.database.dao.StockInfoDao
import com.alexander.maynard.database.entity.StockInfo

//Student Number: 301170707

class StockInfoRepository(private val stockInfoDao: StockInfoDao) {

    suspend fun getStockInfoItem(stockSymbol: String): StockInfo? {
        return stockInfoDao.getStockInfoItem(stockSymbol)
    }

    suspend fun insertStockInfoItem(stockInfo: StockInfo) {
        stockInfoDao.insertStockInfoItem(stockInfo)
    }

    suspend fun deleteAllFromStockInfo() {
        stockInfoDao.deleteAllFromStockInfo()
    }
}