package com.android.challengervlt.network

import com.android.challengervlt.network.model.ItemsResponse
import io.reactivex.Single

interface NetworkService {
    fun getCurrencies(): Single<Map<String?, String?>>

    fun getRates(base: String): Single<ItemsResponse>
}