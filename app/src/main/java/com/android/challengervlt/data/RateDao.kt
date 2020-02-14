package com.android.challengervlt.data

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.util.*

/**
 * Represents a currency rate cell in the database.
 * @currency - code of the currency (ex. EUR)
 * @base - currency code of the base currency (ex. EUR)
 * @date - specifies the date for the current currency rate (stored as a timestamp)
 * @rate - currency rate for a specific date with the base currency specified
 */
open class RateDao(
    var currency: String = "",
    var base: String = "",
    var date: Long = 0L,
    var rate: Double = 0.0
) : RealmObject() {
    /**
     * Used as a primary key because neither of the data fields are unique
     */
    @PrimaryKey
    var index: Int = Objects.hash(currency, base)

    override fun equals(other: Any?): Boolean {
        return this.currency.hashCode() == other.hashCode()
    }

    override fun hashCode(): Int {
        return Objects.hash(currency, base, date, rate)
    }

    companion object {
        const val BASE_FIELD = "base"
    }
}