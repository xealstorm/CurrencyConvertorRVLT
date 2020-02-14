package com.android.challengervlt.model

data class CurrencyItem(
    val code: String,
    val title: String,
    var rateValue: Double
) {
    val countryCode = code.substring(0, 2)
    var inputValue: Double = 1.0
}