package com.android.challengervlt.network.model

import com.google.gson.annotations.SerializedName

data class ItemsResponse(
    @SerializedName("base")
    var base: String? = null,
    @SerializedName("date")
    var dateString: String? = null,
    var rates: Map<String?, Double?>? = null
)