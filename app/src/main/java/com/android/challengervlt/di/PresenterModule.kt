package com.android.challengervlt.di

import com.android.challengervlt.data.CurrencyRepository
import com.android.challengervlt.network.NetworkService
import com.android.challengervlt.ui.list.presenter.RatesPresenter
import com.android.challengervlt.ui.list.presenter.RatesPresenterImpl
import dagger.Module
import dagger.Provides

@Module
class PresenterModule {
    @Provides
    @ActivityScope
    fun provideRatesPresenter(
        networkService: NetworkService, currencyRepository: CurrencyRepository
    ): RatesPresenter {
        return RatesPresenterImpl(
            networkService, currencyRepository
            )
    }
}