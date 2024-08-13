//Student Number: 301170707

package com.alexander.maynard

import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.alexander.maynard.database.entity.StockInfo
import com.alexander.maynard.database.viewmodel.StockInfoViewModel
import kotlinx.coroutines.launch

private lateinit var stockInfoViewModel: StockInfoViewModel
class AlexanderActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_alexander)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        //initialize the locationsViewModel
        stockInfoViewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        )[StockInfoViewModel::class.java]

        //delete everything from the database so we can always insert the new items
        lifecycleScope.launch {
            stockInfoViewModel.deleteAllFromStockInfo()
        }
    }

    //insert stock items once the button is pressed
    fun insertStocks(view: View) {
        lifecycleScope.launch {
            stockInfoViewModel.insertStockInfoItem(StockInfo("AMZN", "Amazon", 990.0))
            stockInfoViewModel.insertStockInfoItem(StockInfo("GOOGL", "Google", 800.0))
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
            val textToDisplayAsRadioButton: RadioButton = findViewById(selectStockRadioGroup.checkedRadioButtonId)

            //check if stocks are inserted by the getter for the stockSymbol
            if(stockInfoViewModel.getStockInfoItem(textToDisplayAsRadioButton.text.toString())?.getStockSymbol() != null) {
                //if exists assign the text properly by that item
                stockInfoDisplayTextView.text = stockInfoViewModel.getStockInfoItem(textToDisplayAsRadioButton.text.toString()).toString()
            } else {
                //else if it does not exist then assign the error message to the stockInfoDisplayTextView
                stockInfoDisplayTextView.text = resources.getString(R.string.database_no_data_error_message)
            }
        }
    }
}