package com.android.challengervlt.ui.list.presenter

import com.android.challengervlt.model.CurrencyItem
import com.android.challengervlt.network.NetworkService
import com.android.challengervlt.util.log.L
import com.android.challengervlt.ui.list.view.RatesView

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
class RatesPresenterImpl(
    private val networkService: NetworkService
) : RatesPresenter {
    private var view: RatesView? = null
    private var currentBaseCurrency: String = DEFAULT_BASE_CURRENCY

    override fun setView(view: RatesView) {
        this.view = view
    }

    override fun loadItems(baseCurrency: String?) {
        if (baseCurrency != null) {
            currentBaseCurrency = baseCurrency
        }
        loadRates(currentBaseCurrency)
    }

    private fun loadRates(baseCurrency: String) {
        networkService.getRates(baseCurrency)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map { rates ->
                val rateItems = mutableListOf<CurrencyItem>()
                rates.rates?.filter { it.key != null && it.value != null }?.forEach { item ->
                    rateItems.add(
                        CurrencyItem(
                            item.key!!,
                            "title placeholder", //TODO: See RVLT270
                            item.value!!
                        )
                    )

                }
                if (rates.base != null) {
                    rateItems.add(
                        DEFAULT_BASE_POSITION_IN_LIST,
                        CurrencyItem(
                            rates.base!!,
                            "title placeholder", //TODO: See RVLT270
                            DEFAULT_BASE_RATE_VALUE
                        )
                    )
                }
                rateItems.toList()
            }
            .subscribe({
                updateList(it)
            }, {
                L.e(TAG, "Error when getting products", it)
            })
    }

    override fun updateList(items: List<CurrencyItem>) {
        if (items.isNotEmpty()) {
            view?.updateItems(items)
        } else {
            view?.resetList()
        }
    }

    companion object {
        private val TAG = RatesPresenterImpl::class.java.toString()
        private const val DEFAULT_BASE_CURRENCY = "EUR"
        private const val DEFAULT_BASE_POSITION_IN_LIST = 0
        private const val DEFAULT_BASE_RATE_VALUE = 1.0
    }
}