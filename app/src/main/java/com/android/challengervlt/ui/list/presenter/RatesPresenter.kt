package com.android.challengervlt.ui.list.presenter

import android.view.View
import com.android.challengervlt.model.CurrencyItem
import com.android.challengervlt.ui.base.presenter.BasePresenter
import com.android.challengervlt.ui.list.view.RatesView

interface RatesPresenter : BasePresenter<RatesView> {
    fun updateList(items: List<CurrencyItem>)

    fun loadItems(baseCurrency: String? = null)

    fun getCurrenciesWithResult(): List<String>
}