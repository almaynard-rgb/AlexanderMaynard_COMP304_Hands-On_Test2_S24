package com.alexander.maynard.database.viewmodel


import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.alexander.maynard.database.StockInfoDatabase
import androidx.lifecycle.viewModelScope
import com.alexander.maynard.database.entity.StockInfo
import com.alexander.maynard.database.repository.StockInfoRepository
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

//Student Number: 301170707

//StockInfoViewModel will interact with the views and LocationsRepository to keep needed info
class StockInfoViewModel(application: Application): AndroidViewModel(application) {
    //instance of the stockInfoRepository
    private val stockInfoRepo: StockInfoRepository

    //initialize values
    init {
        //initialize the dao by using the instance of the StockInfoDatabase.getDatabase().stockInfoDao()
        val stockInfoDao = StockInfoDatabase.getDatabase(application).stockInfoDao()
        //assign the repository
        stockInfoRepo = StockInfoRepository(stockInfoDao)
    }

    //get a single StockInfo item (object)
    suspend fun getStockInfoItem(stockSymbol: String): StockInfo? {
        val deferredStockInfoItem: Deferred<StockInfo?> = viewModelScope.async {
            stockInfoRepo.getStockInfoItem(stockSymbol)
        }
        return deferredStockInfoItem.await()
    }

    //insert a single StockInfo item (object)
    suspend fun insertStockInfoItem(stockInfo: StockInfo) = viewModelScope.launch {
        stockInfoRepo.insertStockInfoItem(stockInfo)
    }

    /*delete all stock info objects in the database
    (this is to reset the database to make the Hands-On
    Test 2 more testable each time that it is started)*/
    suspend fun deleteAllFromStockInfo() = viewModelScope.launch {
        stockInfoRepo.deleteAllFromStockInfo()
    }
}