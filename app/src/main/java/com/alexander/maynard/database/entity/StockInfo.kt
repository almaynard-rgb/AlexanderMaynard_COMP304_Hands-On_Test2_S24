package com.alexander.maynard.database.entity

import android.util.Log
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

//Student Number: 301170707


//StockInfo entity data class defines the structure of the "stock_info" table
@Entity(tableName = "stock_info")
data class StockInfo(
    @PrimaryKey @ColumnInfo(name = "stock_symbol") private var stockSymbol: String,
    @ColumnInfo(name = "company_name") private var companyName: String,
    @ColumnInfo(name = "stock_quote") private var stockQuote: Double
) {


    //default constructor that will auto assign values
    //Note: UUID.randomUUID.toString().substring() gives a random string with a length of 5
    constructor() : this(UUID.randomUUID().toString().substring(0, 5), companyName = "Not defined", stockQuote = 0.0)

    //getter for the stockSymbol value of a StockInfo object
    fun getStockSymbol(): String {
        return stockSymbol
    }

    //getter for the companyName value of a StockInfo object
    fun getCompanyName(): String {
        return companyName
    }

    //getter for the stockQuote value of a StockInfo object
    fun getStockQuote(): Double {
        return stockQuote
    }

    /*setter for the stockSymbol value of a StockInfo object.
    Also makes sure to not let a blank string to be entered. If so,
    a default stockSymbol value will be set. */
    fun setStockSymbol(stockSymbol: String) {
        if(stockSymbol.isNotBlank()) {
            this.stockSymbol = stockSymbol
        }
        else {
            this.stockSymbol = UUID.randomUUID().toString().substring(0, 5)
            Log.e("Incorrect Stock Symbol", "The stock symbol was set auto set as it was incorrect")
        }
    }

    /*setter for the companyName value of a StockInfo object.
    Also makes sure to not let a blank string to be entered. If so,
    a default companyName value will be set. */
    fun setCompanyName(companyName: String) {
        if(companyName.isNotBlank()) {
            this.companyName = companyName
        }
        else {
            this.companyName = "Not defined"
            Log.e("Incorrect Company name format set", "The company name was set to 'Not defined' as it was incorrect")
        }
    }

    /*setter for the stockQuote value of a StockInfo object.
    Also makes sure to not let a negative double to be entered. If so,
    a default stockQuote value will be set. */
    fun setStockQuote(stockQuote: Double) {
        if(stockQuote >= 0) {
            this.stockQuote = stockQuote
        }
        else {
            this.stockQuote = 0.0
            Log.v("Incorrect Stock Quote", "Stock quote was too low. It was thus set as 0.0")
        }
    }

    /*override of the StockInfo toString method to custom
    display the relevant StockInfo information when toString
    for the object is called. */
    override fun toString(): String {
        return "Company Name: ${getCompanyName()}\nStock Quote: ${getStockQuote()}"
    }
}