package com.android.challengervlt.ui.list.view

import com.android.challengervlt.model.CurrencyItem

interface RatesView {
    fun updateItems(items: List<CurrencyItem>)

    fun resetList()
}