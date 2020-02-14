package com.android.challengervlt.network

import com.android.challengervlt.network.model.ItemsResponse
import io.reactivex.Single

interface NetworkService {
    /**
     * Provides a map where the key is the currency code and the value is the currency name
     * (ex. {"RUB": "Russian Ruble", "USD": "United States Dollar",...}
     */
    fun getCurrencies(): Single<Map<String?, String?>>

    /**
     * Provides the list of currency rates for a specific date for a specified base currency
     * @base: Currency code (ex. EUR)
     */
    fun getRates(base: String): Single<ItemsResponse>
}