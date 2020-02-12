package com.android.challengervlt.di

import com.android.challengervlt.data.CurrencyRepository
import com.android.challengervlt.data.CurrencyRepositoryImpl
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
}
