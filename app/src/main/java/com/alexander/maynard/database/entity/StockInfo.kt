package com.alexander.maynard.database.entity

import android.util.Log
import android.widget.Toast
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

//Student Number: 301170707

@Entity(tableName = "stock_info")
data class StockInfo(
    @PrimaryKey @ColumnInfo(name = "stock_symbol") private var stockSymbol: String,
    @ColumnInfo(name = "company_name") private var companyName: String,
    @ColumnInfo(name = "stock_quote") private var stockQuote: Double
) {
    //UUID.randomUUID.toString().substring() gives a random string with a length of 6
    constructor() : this(UUID.randomUUID().toString().substring(0, 5), companyName = "Not defined", stockQuote = 0.0)

    fun getStockSymbol(): String {
        return stockSymbol
    }

    fun getCompanyName(): String {
        return companyName
    }

    fun getStockQuote(): Double {
        return stockQuote
    }

    fun setStockSymbol(stockSymbol: String) {
        if(stockSymbol.isNotBlank()) {
            this.stockSymbol = stockSymbol
        }
        else {
            this.stockSymbol = UUID.randomUUID().toString().substring(0, 5)
            Log.e("Incorrect Stock Symbol", "The stock symbol was set auto set as it was incorrect")
        }
    }

    fun setCompanyName(companyName: String) {
        if(stockSymbol.isNotBlank()) {
            this.companyName = companyName
        }
        else {
            this.companyName = "Not defined"
            Log.e("Incorrect Company name format set", "The company name was set to 'Not defined' as it was incorrect")
        }
    }

    fun setStockQuote(stockQuote: Double) {
        if(stockQuote >= 0) {
            this.stockQuote = stockQuote
        }
        else {
            this.stockQuote = 0.0
            Log.v("Stock Quote Low", "Stock quote was too low. It was thus set as 0.0")
        }
    }

    override fun toString(): String {
        return "Company Name: ${getCompanyName()}\nStock Quote: ${getStockQuote()}"
    }
}