package com.android.challengervlt.ui.base.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.android.challengervlt.App
import com.android.challengervlt.di.ActivityComponent
import com.android.challengervlt.di.ActivityModule
import com.android.challengervlt.di.DaggerActivityComponent

abstract class BaseActivity : AppCompatActivity() {
    lateinit var activityComponent: ActivityComponent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityComponent = createActivityComponent()
        doInjections(activityComponent)
    }

    protected abstract fun doInjections(activityComponent: ActivityComponent?)

    private fun createActivityComponent(): ActivityComponent {
        val app = applicationContext as App
        return DaggerActivityComponent
            .builder()
            .appComponent(app.getAppComponent())
            .activityModule(ActivityModule(this))
            .build()
    }
}