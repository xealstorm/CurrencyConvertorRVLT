package com.android.challengervlt.di

import com.android.challengervlt.ui.list.view.RatesFragment
import dagger.Subcomponent

@FragmentScope
@Subcomponent(modules = [RatesModule::class])
interface RatesComponent {
    fun injectTo(fragment: RatesFragment)
}