package com.android.challengervlt.di

import com.android.challengervlt.ui.base.view.BaseActivity
import dagger.Module
import dagger.Provides

@Module
class ActivityModule(val baseActivity: BaseActivity) {

    @Provides
    @ActivityScope
    fun baseActivity(): BaseActivity {
        return baseActivity
    }
}