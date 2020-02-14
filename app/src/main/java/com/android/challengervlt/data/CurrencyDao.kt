package com.android.challengervlt.data

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

/**
 * Represents a currency cell in the database.
 * @code - Code of the currency (ex. EUR)
 * @title - Name of the currency (ex. Euro)
 */
open class CurrencyDao(
    @PrimaryKey
    var code: String = "",
    var title: String = ""
) : RealmObject()