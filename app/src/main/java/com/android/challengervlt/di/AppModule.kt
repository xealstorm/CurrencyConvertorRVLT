package com.android.challengervlt.di

import com.android.challengervlt.App
import com.android.challengervlt.data.RealmCreator
import com.android.challengervlt.util.scedulers.AppSchedulerProvider
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(val app: App) {

    @Provides
    @Singleton
    fun provideApp(): App = app


    @Provides
    @Singleton
    fun provideRealm(app: App) = RealmCreator.create(app.applicationContext)

    @Provides
    @Singleton
    fun provideSchedulerProvider() = AppSchedulerProvider()
}