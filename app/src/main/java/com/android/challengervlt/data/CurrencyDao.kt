package com.android.challengervlt.data

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

// data access object for a currency
open class CurrencyDao(
    @PrimaryKey
    var code: String = "",
    var title: String = ""
) : RealmObject()