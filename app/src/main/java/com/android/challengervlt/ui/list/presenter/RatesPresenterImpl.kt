package com.android.challengervlt.ui.list.presenter

import com.android.challengervlt.data.CurrencyRepository
import com.android.challengervlt.model.CurrencyItem
import com.android.challengervlt.network.NetworkService
import com.android.challengervlt.util.log.L
import com.android.challengervlt.ui.list.view.RatesView

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class RatesPresenterImpl(
    private val networkService: NetworkService,
    private val itemsRepository: CurrencyRepository
) : RatesPresenter {
    private var view: RatesView? = null
    private var currentBaseCurrency: String = DEFAULT_BASE_CURRENCY

    private var subscribtion: Disposable? = null

    override fun setView(view: RatesView) {
        this.view = view
    }

    override fun loadItems(baseCurrency: String?) {
        if (baseCurrency != null) {
            currentBaseCurrency = baseCurrency
        }
        if (itemsRepository.hasAny()) {
            loadRates(currentBaseCurrency)
        } else {
            loadCurrenciesAndRates(currentBaseCurrency)
        }
    }

    private fun loadCurrenciesAndRates(baseCurrency: String) {
        networkService.getCurrencies()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map {
                it.filter { it.key != null && it.value != null }
                    .forEach {
                        itemsRepository.addItem(it.key!!, it.value!!)
                    }
            }
            .subscribe({
                loadRates(baseCurrency)
            }, {
                L.e(TAG, "Error when getting products", it)
            })
    }

    private fun loadRates(baseCurrency: String) {
        subscribtion?.dispose()
        subscribtion = networkService.getRates(baseCurrency)
            .repeatWhen { completed -> completed.delay(1, TimeUnit.SECONDS) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map { rates ->
                val filteredCurrencies = itemsRepository.getItems()
                val rateItems = mutableListOf<CurrencyItem>()
                rates.rates?.filter { it.key != null && it.value != null }?.forEach { item ->
                    if (filteredCurrencies.any { it.code == item.key }) {
                        rateItems.add(
                            CurrencyItem(
                                item.key!!,
                                filteredCurrencies.first { it.code == item.key }.title,
                                item.value!!
                            )
                        )
                    }

                }
                if (rates.base != null) {
                    rateItems.add(
                        DEFAULT_BASE_POSITION_IN_LIST,
                        CurrencyItem(
                            rates.base!!,
                            filteredCurrencies.first { it.code == rates.base }.title,
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