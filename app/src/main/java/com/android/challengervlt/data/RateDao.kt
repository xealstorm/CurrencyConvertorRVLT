package com.android.challengervlt.data

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.util.*

// data access object for a currency
open class RateDao(
    var currency: String = "",
    var base: String = "",
    var date: Long = 0L,
    var rate: Double = 0.0
) : RealmObject() {
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