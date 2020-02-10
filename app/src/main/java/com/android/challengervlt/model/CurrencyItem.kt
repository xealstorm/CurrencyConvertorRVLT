package com.android.challengervlt.model

data class CurrencyItem(
    val code: String,
    val title: String,
    val rateValue: Double
) {
    val countryCode = code.substring(0, 2)

}