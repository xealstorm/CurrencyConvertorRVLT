package com.android.challengervlt

import android.app.Application
import com.android.challengervlt.di.*
import com.android.challengervlt.util.ui.PicassoHelper

class App : Application() {
    private var instance: App? = null

    private var appComponent: AppComponent? = null

    override fun onCreate() {
        super.onCreate()
        instance = this
        appComponent = DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .networkServicesModule(NetworkServicesModule(BuildConfig.RATES_URL))
            .build()
        PicassoHelper.setupPicasso(this)
    }

    fun getAppComponent(): AppComponent? {
        return appComponent
    }
}