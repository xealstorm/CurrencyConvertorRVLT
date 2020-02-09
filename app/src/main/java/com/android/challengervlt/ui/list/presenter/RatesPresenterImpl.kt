package com.android.challengervlt.ui.list.presenter

import com.android.challengervlt.model.CurrencyItem
import com.android.challengervlt.ui.list.view.RatesView

class RatesPresenterImpl(
) : RatesPresenter {
    private var view: RatesView? = null

    override fun setView(view: RatesView) {
        this.view = view
    }

    override fun generateList() {
        //TODO: replace with api call
        val items = arrayListOf<CurrencyItem>()
        items.add(CurrencyItem("EUR", "Euro", 1.0))
        items.add(CurrencyItem("THB", "Thai Baht", 38.78))
        items.add(CurrencyItem("JPY", "Japanese Yen", 132.0))
        items.add(CurrencyItem("DKK", "Danish Krone", 7.44))
        updateList(items)
    }

    private fun updateList(items: List<CurrencyItem>) {
        if (items.isNotEmpty()) {
            view?.updateItems(items)
        } else {
            view?.resetList()
        }
    }

}