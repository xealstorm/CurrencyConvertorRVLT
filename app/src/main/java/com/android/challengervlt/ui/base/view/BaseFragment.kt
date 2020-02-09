package com.android.challengervlt.ui.base.view

import android.os.Bundle
import com.android.challengervlt.di.ActivityComponent

abstract class BaseFragment : androidx.fragment.app.Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (activity != null && BaseActivity::class.java.isAssignableFrom(activity!!.javaClass)) {
            doInjections((activity as BaseActivity).activityComponent)
        }
    }

    protected abstract fun doInjections(activityComponent: ActivityComponent)
}