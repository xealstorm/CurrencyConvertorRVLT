package com.android.challengervlt.data

import io.realm.annotations.RealmModule

@RealmModule(
    library = true,
    classes = [CurrencyDao::class, RateDao::class]
)
class RealmModule
