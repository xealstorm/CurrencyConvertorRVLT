package com.android.challengervlt.data

interface CurrencyRepository {
    fun addItem(
        code: String,
        title: String
    )

    fun getItems(): List<CurrencyDao>

    fun deleteAll()

    fun hasAny(): Boolean
}