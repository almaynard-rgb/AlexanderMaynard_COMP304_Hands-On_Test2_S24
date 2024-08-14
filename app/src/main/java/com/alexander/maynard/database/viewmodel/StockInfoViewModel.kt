package com.alexander.maynard.database.viewmodel


import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewmodel.viewModelFactory
import com.alexander.maynard.database.StockInfoDatabase
import androidx.lifecycle.viewModelScope
import com.alexander.maynard.database.entity.StockInfo
import com.alexander.maynard.database.repository.StockInfoRepository
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

//Student Number: 301170707

class StockInfoViewModel(application: Application): AndroidViewModel(application) {
    private val stockInfoRepo: StockInfoRepository

    init {
        val stockInfoDao = StockInfoDatabase.getDatabase(application).stockInfoDao()
        stockInfoRepo = StockInfoRepository(stockInfoDao)
    }

    suspend fun getStockInfoItem(stockSymbol: String): StockInfo? {
        val deferredStockInfoItem: Deferred<StockInfo?> = viewModelScope.async {
            stockInfoRepo.getStockInfoItem(stockSymbol)
        }
        return deferredStockInfoItem.await()
    }
    suspend fun insertStockInfoItem(stockInfo: StockInfo) = viewModelScope.launch {
        stockInfoRepo.insertStockInfoItem(stockInfo)
    }

    suspend fun deleteAllFromStockInfo() = viewModelScope.launch {
        stockInfoRepo.deleteAllFromStockInfo()
    }
}