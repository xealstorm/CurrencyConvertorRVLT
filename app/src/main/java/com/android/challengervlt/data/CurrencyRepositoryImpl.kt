package com.android.challengervlt.data

import io.realm.Realm

class CurrencyRepositoryImpl(private val realm: Realm) : CurrencyRepository {

    override fun addItem(
        code: String,
        title: String
    ) {
        realm.executeTransaction {
            it.insertOrUpdate(
                CurrencyDao(
                    code,
                    title
                )
            )
        }
    }

    override fun getItems(): List<CurrencyDao> = realm.where(CurrencyDao::class.java).findAll()

    override fun deleteAll() {
        realm.executeTransaction {
            it.delete(CurrencyDao::class.java)
        }
    }

    override fun hasAny() = realm.where(CurrencyDao::class.java).count() > 0
}