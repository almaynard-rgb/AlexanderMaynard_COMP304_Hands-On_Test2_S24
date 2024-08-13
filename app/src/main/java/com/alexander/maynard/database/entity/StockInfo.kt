package com.alexander.maynard.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "stock_info")
data class StockInfo(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "stock_symbol") private var stockSymbol: String,
    @ColumnInfo(name = "company_name") private var companyName: String,
    @ColumnInfo(name = "stock_quote") private var stockQuote: Double
) {
    //UUID.randomUUID.toString().substring() gives a random string with a length of 6
    constructor(companyName: String, stockQuote: Double) : this(UUID.randomUUID().toString().substring(0, 5), "Not set", 0.0)

    fun stockSymbol(): String {
        return stockSymbol
    }

    fun companyName(): String {
        return companyName
    }

    fun stockQuote(): Double {
        return stockQuote
    }

    fun setStockSymbol(stockSymbol: String) {
        this.stockSymbol = stockSymbol
    }

    fun setCompanyName(companyName: String) {
        this.companyName = companyName
    }

    fun setStockQuote(stockQuote: Double) {
        this.stockQuote = stockQuote
    }

    override fun toString(): String {
        return "Stock Info received:\nCompany Name: $companyName\nStock Quote: $stockQuote"
    }
}