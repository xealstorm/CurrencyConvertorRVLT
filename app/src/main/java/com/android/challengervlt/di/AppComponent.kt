package com.android.challengervlt.di

import com.android.challengervlt.App
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [AppModule::class]
)
interface AppComponent {
    fun provideApp(): App
    fun inject(application: App)
}