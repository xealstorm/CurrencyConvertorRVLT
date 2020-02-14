package com.android.challengervlt.ui.list.presenter

import com.android.challengervlt.R
import com.android.challengervlt.data.CurrencyRepository
import com.android.challengervlt.data.RateRepository
import com.android.challengervlt.model.CurrencyItem
import com.android.challengervlt.network.NetworkService
import com.android.challengervlt.ui.list.view.RatesView
import com.android.challengervlt.util.format.TimeFormatter

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class RatesPresenterImpl(
    private val networkService: NetworkService,
    private val itemsRepository: CurrencyRepository,
    private val rateRepository: RateRepository
) : RatesPresenter {
    private var view: RatesView? = null
    private var currentBaseCurrency: String = DEFAULT_BASE_CURRENCY

    private var subscription: Disposable? = null

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
                view?.showErrorMessage(R.string.offline_no_data_message)
            })
    }

    override fun pauseUpdates() {
        subscription?.dispose()
    }

    private fun loadRates(baseCurrency: String) {
        pauseUpdates()
        subscription = networkService.getRates(baseCurrency)
            .repeatWhen { completed -> completed.delay(1, TimeUnit.SECONDS) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroiSchedulers.mainThread())
            .map { rates ->
                saveRates(
                    rates.base ?: "",
                    rates.rates,
                    TimeFormatter.dateDeFormatted(rates.dateString)?.millis ?: 0L
                )
                provideCurrencyItems(rates.base ?: "")
            }
            .subscribe({
                updateList(it)
            }, {
                updateList(provideCurrencyItems(currentBaseCurrency))
                view?.updateClickables(getCurrenciesWithResult())
                view?.showErrorMessage(R.string.offline_message)
            })
    }

    override fun updateList(items: List<CurrencyItem>) {
        if (items.isNotEmpty()) {
            view?.updateItems(items)
        } else {
            view?.resetList()
        }
    }

    private fun saveRates(base: String, rates: Map<String?, Double?>?, dateTimeInMillis: Long) {
        val filteredCurrencies = itemsRepository.getItems()

        rates?.filter { it.key != null && it.value != null }?.forEach { item ->
            if (filteredCurrencies.any { it.code == item.key }) {
                rateRepository.addItem(
                    item.key!!,
                    base,
                    dateTimeInMillis,
                    item.value!!
                )
            }

        }
        if (!rates?.filter { it.key != null && it.value != null }.isNullOrEmpty()) {
            rateRepository.addItem(
                base,
                base,
                dateTimeInMillis,
                DEFAULT_BASE_RATE_VALUE
            )
        }
    }


    private fun provideCurrencyItems(base: String): List<CurrencyItem> {
        val filteredCurrencies = itemsRepository.getItems()
        val rates = rateRepository.getValuesByBase(base)

        val rateItems = mutableListOf<CurrencyItem>()
        rates.filterNotNull().forEach { item ->
            if (filteredCurrencies.any { it.code == item.currency }) {
                val currencyItem = CurrencyItem(
                    item.currency,
                    filteredCurrencies.first { it.code == item.currency }.title,
                    item.rate
                )
                if (currencyItem.code == base) {
                    rateItems.add(
                        DEFAULT_BASE_POSITION_IN_LIST, currencyItem
                    )
                } else {
                    rateItems.add(currencyItem)
                }
            }

        }
        return rateItems.toList()
    }

    override fun getCurrenciesWithResult() = itemsRepository.getItems().filter {
        !rateRepository.getValuesByBase(it.code).isNullOrEmpty()
    }.map { it.code }


    companion object {
        private val TAG = RatesPresenterImpl::class.java.toString()
        private const val DEFAULT_BASE_CURRENCY = "EUR"
        private const val DEFAULT_BASE_POSITION_IN_LIST = 0
        private const val DEFAULT_BASE_RATE_VALUE = 1.0
    }
}