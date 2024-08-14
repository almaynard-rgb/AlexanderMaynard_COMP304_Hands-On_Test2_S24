package com.alexander.maynard

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.alexander.maynard.database.entity.StockInfo
import com.alexander.maynard.database.viewmodel.StockInfoViewModel
import kotlinx.coroutines.launch

//Student Number: 301170707

private lateinit var stockInfoViewModel: StockInfoViewModel
class AlexanderActivity : AppCompatActivity() {
    private lateinit var receiver: BroadcastReceiver
    private lateinit var filter: IntentFilter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_alexander)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        receiver = StockInfoReceiver()
        filter = IntentFilter("com.alexander.maynard.CUSTOM_INTENT")

        //initialize the locationsViewModel
        stockInfoViewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        )[StockInfoViewModel::class.java]
        //delete everything from the database so we can always insert the new items
        lifecycleScope.launch {
            stockInfoViewModel.deleteAllFromStockInfo()

            //add first item manually to complete testing based on the criteria that only 2 items are inserted via the insert button
            //Also using the default constructor and setter methods to show that they work
            val googleStockInfoItem = StockInfo()
            showToast(applicationContext, "First stock item before setters: \n$googleStockInfoItem")
            googleStockInfoItem.setStockSymbol("GOOGL")
            googleStockInfoItem.setCompanyName("Google")
            googleStockInfoItem.setStockQuote(800.0)

            stockInfoViewModel.insertStockInfoItem(googleStockInfoItem)
        }
    }

    override fun onStart() {
        super.onStart()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            registerReceiver(receiver, filter, RECEIVER_NOT_EXPORTED)
        }
        else {
            registerReceiver(receiver, filter)
        }
    }

    override fun onResume() {
        super.onResume()
        // Register the broadcast receiver.
        val lbm = LocalBroadcastManager.getInstance(this)
        lbm.registerReceiver(receiver, filter)
    }

    override fun onPause() {
        super.onPause()
        // Unregister the receiver
        val lbm = LocalBroadcastManager.getInstance(this)
        lbm.unregisterReceiver(receiver)
    }

    override fun onStop() {
        super.onStop()
        unregisterReceiver(receiver)
    }

    //insert stock items once the button is pressed
    fun insertStocks(view: View) {
        lifecycleScope.launch {
            stockInfoViewModel.insertStockInfoItem(StockInfo("AMZN", "Amazon", 990.0))
            stockInfoViewModel.insertStockInfoItem(StockInfo("SSNLF", "Samsung Electronics", 760.0))
        }
    }

    //Display stock does the following:
    // 1. Select the stock symbol/company name from the RadioButton controls DONE
    // 2. retrieve the item in the database (with check), DONE
    // 3. display the info in textview DONE
    // 4. Passing the stock info to the broadcast receiver to later display using toast
    fun displayStocks(view: View) {
        val stockInfoDisplayTextView = findViewById<TextView>(R.id.stock_info_display_text_view)
        lifecycleScope.launch {
            val selectStockRadioGroup = findViewById<RadioGroup>(R.id.select_stock_radio_group)
            val textToDisplay = findViewById<RadioButton>(selectStockRadioGroup.checkedRadioButtonId).text

            val stockInfoItemFromDb = stockInfoViewModel.getStockInfoItem(textToDisplay.toString())

            //check if stock info item is in the database at all
            if(stockInfoItemFromDb?.getStockSymbol() != null) {
                //if exists assign the text properly by that item
                stockInfoDisplayTextView.text = stockInfoItemFromDb.toString()
                val i = Intent(applicationContext, StockInfoReceiver::class.java)
                i.putExtra("SentStockInfo", stockInfoItemFromDb.toString())
                i.setAction("com.alexander.maynard.CUSTOM_INTENT")
                sendBroadcast(i)
            } else {
                //else if it does not exist then assign the error message to the stockInfoDisplayTextView
                stockInfoDisplayTextView.text = resources.getString(R.string.database_no_data_error_message)
            }
        }
    }

    companion object {

        private var instance: AlexanderActivity? = null
        //custom toast to show all content that is needing to be displayed
        fun showToast(context: Context, message: String) {
            val inflater: LayoutInflater = LayoutInflater.from(context)
            val customToastLayout: View = inflater.inflate(R.layout.custom_toast, instance?.findViewById(R.id.toast_root))
            val toastMessage = customToastLayout.findViewById<TextView>(R.id.toast_message_text)
            toastMessage.text = message
            val toast = Toast(context)
            toast.duration = Toast.LENGTH_LONG
            toast.view = customToastLayout
            toast.show()
        }
    }
}