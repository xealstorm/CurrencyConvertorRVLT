package com.android.challengervlt.di

import com.android.challengervlt.ui.list.view.RatesAdapter
import dagger.Module
import dagger.Provides

@Module
class RatesModule {
    @FragmentScope
    @Provides
    fun requestRatesAdapter(): RatesAdapter {
        return RatesAdapter()
    }
}