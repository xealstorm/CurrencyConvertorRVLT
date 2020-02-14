package com.android.challengervlt.model

/**
 * Represents a currency item in a list
 * @code - Code of the currency (ex. EUR)
 * @title - Name of the currency (ex. Euro)
 * @rateValue - Rate of the currency for a specified base currency for a given date (ex. 1.9602)
 */
data class CurrencyItem(
    val code: String,
    val title: String,
    var rateValue: Double
) {
    /**
     * Service field needed to convert a currency code to a country code
     * in order to get a flag from an external API
     */
    val countryCode = code.substring(0, 2)

    /**
     * Service field needed to persist the value that was typed in the UI
     * for saving it to the clipboard or for handling during the base currency rate change
     */
    var inputValue: Double = 1.0
}