package com.android.challengervlt.di

import com.android.challengervlt.ui.main.view.MainActivity
import com.android.challengervlt.ui.base.view.BaseActivity
import dagger.Component

@ActivityScope
@Component(dependencies = [AppComponent::class],
    modules = [ActivityModule::class, PresenterModule::class])
interface ActivityComponent {
    fun baseActivity(): BaseActivity

    fun injectTo(activity: MainActivity)

    fun plus(ratesModule: RatesModule): RatesComponent

}