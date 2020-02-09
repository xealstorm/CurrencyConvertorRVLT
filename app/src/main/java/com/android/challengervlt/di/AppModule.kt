package com.android.challengervlt.di

import com.android.challengervlt.App
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(val app: App) {

    @Provides
    @Singleton
    fun provideApp(): App {
        return app
    }
}