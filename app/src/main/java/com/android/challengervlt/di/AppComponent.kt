package com.android.challengervlt.di

import com.android.challengervlt.App
import com.android.challengervlt.data.CurrencyRepository
import com.android.challengervlt.data.RateRepository
import com.android.challengervlt.network.NetworkService
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [AppModule::class, NetworkServicesModule::class, RepositoryModule::class]
)
interface AppComponent {
    fun provideApp(): App

    fun provideNetworkService(): NetworkService

    fun provideCurrencyRepository(): CurrencyRepository

    fun provideRatesRepository(): RateRepository

    fun inject(application: App)
}