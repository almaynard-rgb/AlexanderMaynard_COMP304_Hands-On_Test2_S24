package com.alexander.maynard

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.Toast

//Student Number: 301170707

class StockInfoReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {

        //get the stringExtra from the intent passed by the display button in AlexanderActivity
        val stockInfoText = intent?.getStringExtra("SentStockInfo")
        //create custom toast from the showToast companion object in AlexanderActivity
        AlexanderActivity.showToast(context!!, "Stock Info received:\n$stockInfoText")
    }
}