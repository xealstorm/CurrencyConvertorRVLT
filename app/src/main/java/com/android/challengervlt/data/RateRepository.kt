package com.android.challengervlt.data


interface RateRepository {
    fun addItem(
        currencyCode: String,
        baseCurrencyCode: String,
        date: Long,
        value: Double
    )

    fun getValuesByBase(base: String): List<RateDao?>

    fun deleteAll()
}