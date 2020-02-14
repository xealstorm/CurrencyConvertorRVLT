package com.android.challengervlt.ui.list.view

import com.android.challengervlt.model.CurrencyItem

interface RatesView {
    /**
     * Updates the items in the UI based on the
     * @items
     */
    fun updateItems(items: List<CurrencyItem>)

    /**
     * Removes all excising items from the UI
     */
    fun resetList()

    /**
     * Indicates that an error occured
     */
    fun showErrorMessage(errorMessageResId: Int? = null)

    /**
     * Provides a list of the currency codes that have offline results
     */
    fun updateOfflineCurrencies(currenciesWithResults: List<String>? = null)
}