package com.android.challengervlt.ui.main.view

import androidx.databinding.DataBindingUtil
import android.os.Bundle
import com.android.challengervlt.R
import com.android.challengervlt.databinding.ActivityMainBinding
import com.android.challengervlt.di.ActivityComponent
import com.android.challengervlt.ui.base.view.BaseActivity
import com.android.challengervlt.ui.list.view.RatesFragment


class MainActivity : BaseActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        showRatesList()
    }

    private fun showRatesList() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.frame_layout, RatesFragment.newInstance())
            .commit()
    }

    override fun doInjections(activityComponent: ActivityComponent?) {
        activityComponent?.injectTo(this)
    }

    fun expandAppBar() {
        binding.appBar.setExpanded(true, true)
    }
}
