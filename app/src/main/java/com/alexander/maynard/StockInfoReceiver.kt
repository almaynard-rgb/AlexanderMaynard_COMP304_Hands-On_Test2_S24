package com.alexander.maynard

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

//Student Number: 301170707


//StockInfoReceiver waits for a broadcast from AlexanderActivity
class StockInfoReceiver: BroadcastReceiver() {

    //override what happens when a broadcast is received
    override fun onReceive(context: Context?, intent: Intent?) {

        //get the stringExtra from the intent/broadcast passed by the displayStocksInfo function in AlexanderActivity
        val stockInfoText = intent?.getStringExtra("SentStockInfo")
        //create custom toast from the showCustomToast companion object in AlexanderActivity
        //show the StockInfo information passed by the broadcast
        AlexanderActivity.showCustomToast(context!!, "Stock Info received:\n$stockInfoText")
    }
}