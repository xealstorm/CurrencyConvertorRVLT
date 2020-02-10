package com.android.challengervlt.network

import com.android.challengervlt.network.model.ItemsResponse
import io.reactivex.Single

interface NetworkService {
    fun getRates(base: String): Single<ItemsResponse>
}