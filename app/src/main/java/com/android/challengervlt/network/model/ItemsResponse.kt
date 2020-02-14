package com.android.challengervlt.network.model

import com.google.gson.annotations.SerializedName

/**
 * Response with currency rates
 * @base - currency code of the base currency (ex. EUR)
 * @dateString - date of the currency rates (ex. 14-02-2020)
 * @rates - map of currency codes and the rates to the given base (ex. {"USD": 0.0902, "AUD": 1.9521, ...})
 */
data class ItemsResponse(
    @SerializedName("base")
    var base: String? = null,
    @SerializedName("date")
    var dateString: String? = null,
    var rates: Map<String?, Double?>? = null
)