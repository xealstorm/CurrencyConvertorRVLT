package com.android.challengervlt.di

import com.android.challengervlt.data.CurrencyRepository
import com.android.challengervlt.data.CurrencyRepositoryImpl
import com.android.challengervlt.data.RateRepository
import com.android.challengervlt.data.RateRepositoryImpl
import dagger.Module
import dagger.Provides
import io.realm.Realm
import javax.inject.Singleton

@Module
class RepositoryModule {
    @Provides
    @Singleton
    fun provideCurrencyRepository(realm: Realm): CurrencyRepository {
        return CurrencyRepositoryImpl(realm)
    }

    @Provides
    @Singleton
    fun provideRatesRepository(realm: Realm): RateRepository {
        return RateRepositoryImpl(realm)
    }
}
