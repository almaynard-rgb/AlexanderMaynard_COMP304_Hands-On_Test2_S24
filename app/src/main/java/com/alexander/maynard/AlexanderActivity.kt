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
import androidx.annotation.RequiresApi
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

class AlexanderActivity : AppCompatActivity() {

    //create reference to the stockInfoViewModel
    private lateinit var stockInfoViewModel: StockInfoViewModel
    //create the broadcast receiver and filter variables
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

        //initialize the Broadcast receiver and filter
        receiver = StockInfoReceiver()
        filter = IntentFilter("com.alexander.maynard.CUSTOM_INTENT")

        //initialize the stockInfoViewModel
        stockInfoViewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        )[StockInfoViewModel::class.java]


        //delete everything from the database so we can always insert the new items when the activity is created again.
        lifecycleScope.launch {
            stockInfoViewModel.deleteAllFromStockInfo()

            //add first item manually to complete testing based on the criteria that only 2 items are inserted via the insert button
            //Also using the default constructor and setter methods to show that they work
            val googleStockInfoItem = StockInfo()
            showCustomToast(applicationContext, "First stock item (GOOGL) before setters: \nStock Symbol: ${googleStockInfoItem.getStockSymbol()}\n$googleStockInfoItem")
            googleStockInfoItem.setStockSymbol("GOOGL")
            googleStockInfoItem.setCompanyName("Google")
            googleStockInfoItem.setStockQuote(800.0)
            //show the same StockInfo item after setters
            showCustomToast(applicationContext, "First stock item (GOOGL) after setters: \nStock Symbol: ${googleStockInfoItem.getStockSymbol()}\n$googleStockInfoItem")


            //insert the googleStockInfoItem to the database
            stockInfoViewModel.insertStockInfoItem(googleStockInfoItem)
        }
    }


    //onStart register the receiver
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onStart() {
        super.onStart()
        //this is to correctly check the version and the required arguments (RECEIVER_EXPORTED or not).
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            registerReceiver(receiver, filter, RECEIVER_NOT_EXPORTED)
        }
        else {
            registerReceiver(receiver, filter)
        }
    }

    //onResume register the receiver using local broadcast manager
    override fun onResume() {
        super.onResume()
        // Register the broadcast receiver.
        val localBroadCastManager = LocalBroadcastManager.getInstance(this)
        localBroadCastManager.registerReceiver(receiver, filter)
    }

    //onPause unregister the receiver using local broadcast manager
    override fun onPause() {
        super.onPause()
        // Unregister the receiver
        val localBroadCastManager = LocalBroadcastManager.getInstance(this)
        localBroadCastManager.unregisterReceiver(receiver)
    }

    //onStop unregister the receiver
    override fun onStop() {
        super.onStop()
        unregisterReceiver(receiver)
    }

    //insert 2 stock items once the insert_stocks_button is pressed
    fun insertStocks(view: View) {
        //launch coroutine (to access the stockInfoViewModel)
        lifecycleScope.launch {
            stockInfoViewModel.insertStockInfoItem(StockInfo("AMZN", "Amazon", 990.0))
            stockInfoViewModel.insertStockInfoItem(StockInfo("SSNLF", "Samsung Electronics", 760.0))
        }
    }

    //Display stock does the following when the display_stocks_button is pressed:
    // 1. Select the stock symbol/company name from the RadioButton controls DONE
    // 2. retrieve the item in the database (with check), DONE
    // 3. display the info in textview DONE
    // 4. Passing the stock info to the broadcast receiver to later display using toast
    fun displayStocksInfo(view: View) {
        //reference to the display area where the retrieve StockInfo data will be displayed
        val stockInfoDisplayTextView = findViewById<TextView>(R.id.stock_info_display_text_view)

        //launch coroutine (to access the stockInfoViewModel)
        lifecycleScope.launch {
            val selectStockRadioGroup = findViewById<RadioGroup>(R.id.select_stock_radio_group)

            //get the Stock Symbol to Search in the database from the selected radiobutton
            val stockSymbolToSearch = findViewById<RadioButton>(selectStockRadioGroup.checkedRadioButtonId).text

            //get the stock info item (object) from the database
            val stockInfoItemFromDb = stockInfoViewModel.getStockInfoItem(stockSymbolToSearch.toString())

            //check if stock info item from the database is null
            if(stockInfoItemFromDb?.getStockSymbol() != null) {
                //if exists (not null) assign the text properly by that item
                stockInfoDisplayTextView.text = stockInfoItemFromDb.toString()
                //create an intent broadcast
                val i = Intent(applicationContext, StockInfoReceiver::class.java)
                i.putExtra("SentStockInfo", stockInfoItemFromDb.toString()) //pass the important information
                i.setAction("com.alexander.maynard.CUSTOM_INTENT") //set the intent action (set to our custom intent).
                sendBroadcast(i) //send the broadcast
            } else {
                //else if it does not exist then assign the error message to the stockInfoDisplayTextView
                stockInfoDisplayTextView.text = resources.getString(R.string.database_no_data_error_message)
            }
        }
    }


    //companion object so we can use the custom toast that wee created in this class and outside of it.
    companion object {

        //reference to instance of AlexanderActivity
        private var instance: AlexanderActivity? = null
        //custom toast to show all content that is needing to be displayed
        fun showCustomToast(context: Context, message: String) {
            val inflater: LayoutInflater = LayoutInflater.from(context)
            val customToastLayout: View = inflater.inflate(R.layout.custom_toast, instance?.findViewById(R.id.toast_root))
            val toastMessage = customToastLayout.findViewById<TextView>(R.id.toast_message_text)
            toastMessage.text = message
            val toast = Toast(context)
            toast.duration = Toast.LENGTH_LONG
            toast.view = customToastLayout
            toast.show() //show the custom toast
        }
    }
}