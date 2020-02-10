package com.android.challengervlt

import android.app.Application
import com.android.challengervlt.di.*

class App : Application() {
    private var instance: App? = null

    private var appComponent: AppComponent? = null

    override fun onCreate() {
        super.onCreate()
        instance = this
        appComponent = DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .build()
        PicassoHelper.setupPicasso(this)
    }

    fun getAppComponent(): AppComponent? {
        return appComponent
    }
}