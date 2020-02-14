package com.android.challengervlt.ui.list.presenter

import com.android.challengervlt.model.CurrencyItem
import com.android.challengervlt.ui.base.presenter.BasePresenter
import com.android.challengervlt.ui.list.view.RatesView

interface RatesPresenter : BasePresenter<RatesView> {
    /**
     * Updates the UI with the list of CurrencyItems provided
     */
    fun updateList(items: List<CurrencyItem>)

    /**
     * Initiates loading the data (rates and (if needed) currencies)
     * @baseCurrency - a base currency code to get the rates (ex. EUR)
     */
    fun loadItems(baseCurrency: String? = null)

    /**
     * Provides a list of currency codes that have rates for them being a base
     * that are stored in the database
     */
    fun getCurrenciesWithResult(): List<String>

    /**
     * Pauses updates of the currency rates
     */
    fun pauseUpdates()
}