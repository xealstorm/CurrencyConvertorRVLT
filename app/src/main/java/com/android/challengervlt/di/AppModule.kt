package com.android.challengervlt.di

import com.android.challengervlt.App
import com.android.challengervlt.data.RealmCreator
import dagger.Module
import dagger.Provides
import io.realm.Realm
import javax.inject.Singleton

@Module
class AppModule(val app: App) {

    @Provides
    @Singleton
    fun provideApp(): App {
        return app
    }

    @Provides
    @Singleton
    fun provideRealm(app: App): Realm {
        return RealmCreator.create(app.applicationContext)
    }
}