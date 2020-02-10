package com.android.challengervlt.di

import com.android.challengervlt.App
import com.android.challengervlt.network.NetworkService
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [AppModule::class, NetworkServicesModule::class]
)
interface AppComponent {
    fun provideApp(): App

    fun provideNetworkService(): NetworkService
    fun inject(application: App)
}