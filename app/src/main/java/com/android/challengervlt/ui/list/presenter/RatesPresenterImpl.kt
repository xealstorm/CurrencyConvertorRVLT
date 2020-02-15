package com.android.challengervlt.ui.list.presenter

import androidx.annotation.VisibleForTesting
import com.android.challengervlt.R
import com.android.challengervlt.data.CurrencyRepository
import com.android.challengervlt.data.RateRepository
import com.android.challengervlt.model.CurrencyItem
import com.android.challengervlt.network.NetworkService
import com.android.challengervlt.ui.list.view.RatesView
import com.android.challengervlt.util.format.TimeFormatter
import com.android.challengervlt.util.scedulers.SchedulerProvider

import io.reactivex.disposables.Disposable
import java.util.concurrent.TimeUnit

class RatesPresenterImpl(
    private val networkService: NetworkService,
    private val currencyRepository: CurrencyRepository,
    private val rateRepository: RateRepository,
    private val schedulerProvider: SchedulerProvider
) : RatesPresenter {
    private var view: RatesView? = null
    private var currentBaseCurrency: String = DEFAULT_BASE_CURRENCY

    private var subscription: Disposable? = null

    override fun setView(view: RatesView) {
        this.view = view
    }

    /**
     * Loads currencies (if none in database)
     * Loads currency rates
     */
    override fun loadItems(baseCurrency: String?) {
        if (baseCurrency != null) {
            currentBaseCurrency = baseCurrency
        }
        if (currencyRepository.hasAny()) {
            loadRates(currentBaseCurrency)
        } else {
            loadCurrenciesAndRates(currentBaseCurrency)
        }
    }

    /**
     * Loads currenices
     * Saves them to DB
     * Loads currency rates then
     */
    @VisibleForTesting
    internal fun loadCurrenciesAndRates(baseCurrency: String) {
        networkService.getCurrencies()
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .map {
                saveCurrencies(it)
            }
            .subscribe({
                loadRates(baseCurrency)
            }, {
                view?.showErrorMessage(R.string.offline_no_data_message)
            })
    }

    /**
     * Loads currency rates once per DEFAULT_INTERVAL_IN_SECONDS
     * Saves them to the DB
     * Provides CurrencyItems for the UI
     */
    @VisibleForTesting
    internal fun loadRates(baseCurrency: String) {
        pauseUpdates()
        subscription = networkService.getRates(baseCurrency)
            .repeatWhen { completed ->
                completed.delay(
                    DEFAULT_INTERVAL_IN_SECONDS,
                    TimeUnit.SECONDS
                )
            }
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
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
                // if an error occurred the currency items from the DB are provided
                // the clickable items are provided
                // an error message is shown
                updateList(provideCurrencyItems(currentBaseCurrency))
                view?.updateOfflineCurrencies(getCurrenciesWithResult())
                view?.showErrorMessage(R.string.offline_message)
            })
    }

    override fun pauseUpdates() {
        subscription?.dispose()
    }

    override fun updateList(items: List<CurrencyItem>) {
        if (items.isNotEmpty()) {
            view?.updateItems(items)
        } else {
            view?.resetList()
        }
    }

    override fun getCurrenciesWithResult() = currencyRepository.getItems().filter {
        !rateRepository.getValuesByBase(it.code).isNullOrEmpty()
    }.map { it.code }


    /**
     * Saves currencies locally
     */
    private fun saveCurrencies(currencies: Map<String?, String?>) {
        currencies.filter { it.key != null && it.value != null }
            .forEach {
                currencyRepository.addItem(it.key!!, it.value!!)
            }
    }

    /**
     * Saves rates locally
     */
    private fun saveRates(base: String, rates: Map<String?, Double?>?, dateTimeInMillis: Long) {
        val filteredCurrencies = currencyRepository.getItems()

        // saving the currency rates (ex. {"AUD", "EUR", timestamp, 1.5601})
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
        // saving the base currency rate (ex. {"EUR", "EUR", timestamp, 1.0})
        if (!rates?.filter { it.key != null && it.value != null }.isNullOrEmpty()) {
            rateRepository.addItem(
                base,
                base,
                dateTimeInMillis,
                DEFAULT_BASE_RATE_VALUE
            )
        }
    }

    /**
     * Provides a list of currency items (currencies + rates) to be used in the UI
     */
    private fun provideCurrencyItems(base: String): List<CurrencyItem> {
        val filteredCurrencies = currencyRepository.getItems()
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

    @VisibleForTesting
    internal fun getView(): RatesView? = view

    companion object {
        private val TAG = RatesPresenterImpl::class.java.toString()
        private const val DEFAULT_BASE_CURRENCY = "EUR"
        private const val DEFAULT_BASE_POSITION_IN_LIST = 0
        private const val DEFAULT_BASE_RATE_VALUE = 1.0
        private const val DEFAULT_INTERVAL_IN_SECONDS = 1L
    }
}