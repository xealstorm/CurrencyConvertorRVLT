package com.android.challengervlt.data

import io.realm.Realm

class RateRepositoryImpl(private val realm: Realm) : RateRepository {

    override fun addItem(
        currencyCode: String,
        baseCurrencyCode: String,
        date: Long,
        value: Double
    ) = realm.executeTransaction {
        it.insertOrUpdate(RateDao(currencyCode, baseCurrencyCode, date, value))
    }


    override fun getValuesByBase(base: String): List<RateDao?> =
        realm.where(RateDao::class.java)
            .equalTo(RateDao.BASE_FIELD, base)
            .findAll()

    override fun deleteAll() {
        realm.executeTransaction {
            it.delete(RateDao::class.java)
        }
    }
}