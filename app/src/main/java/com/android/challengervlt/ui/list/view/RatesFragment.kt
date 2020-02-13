package com.android.challengervlt.ui.list.view

import android.content.ClipData
import android.content.ClipboardManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getSystemService
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.*
import androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_IDLE
import com.android.challengervlt.R
import com.android.challengervlt.databinding.FragmentItemsListBinding
import com.android.challengervlt.di.ActivityComponent
import com.android.challengervlt.di.RatesModule
import com.android.challengervlt.model.CurrencyItem
import com.android.challengervlt.ui.base.view.BaseFragment
import com.android.challengervlt.ui.base.view.OnItemClickListener
import com.android.challengervlt.ui.list.presenter.RatesPresenter
import com.android.challengervlt.ui.main.view.MainActivity
import com.android.challengervlt.util.network.InternetConnectivityWatcher
import com.android.challengervlt.util.ui.showClipboardSnackBar
import com.android.challengervlt.util.ui.showErrorSnackBar
import com.google.android.material.snackbar.Snackbar
import javax.inject.Inject


class RatesFragment : BaseFragment(), RatesView {
    @Inject
    lateinit var presenter: RatesPresenter
    @Inject
    lateinit var adapter: RatesAdapter
    lateinit var binding: FragmentItemsListBinding
    private var offlineSnackbar: Snackbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        InternetConnectivityWatcher(this.activity!!).observe(this,
            Observer { connected ->
                if (connected == true) {
                    offlineSnackbar?.dismiss()
                    offlineSnackbar = null
                    adapter.updateClickables()
                    presenter.loadItems()
                } else if (connected == false) {
                    adapter.updateClickables(presenter.getCurrenciesWithResult())
                }
            })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_items_list, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initRecyclerView()
        presenter.setView(this)
    }

    override fun onResume() {
        super.onResume()
        presenter.loadItems()
    }

    override fun doInjections(activityComponent: ActivityComponent) {
        activityComponent.plus(RatesModule()).injectTo(this)
    }

    private fun initRecyclerView() {
        adapter.clickListener = object : OnItemClickListener<CurrencyItem> {
            override fun onItemClicked(item: CurrencyItem) {
                if (binding.ratesViewGroup.layoutManager?.isSmoothScrolling == false) {
                    adapter.swapOnClick(item)
                    scrollToTop()
                    presenter.loadItems(item.code)
                }
            }
        }
        adapter.longClickListener = object : OnItemClickListener<CurrencyItem> {
            override fun onItemClicked(item: CurrencyItem) {
                val clipboard: ClipboardManager? =
                    getSystemService<ClipboardManager>(context!!, ClipboardManager::class.java)
                val clip = ClipData.newPlainText(item.code, item.rateValue.toString())
                clipboard?.setPrimaryClip(clip)
                activity?.showClipboardSnackBar { offlineSnackbar?.show() }
            }
        }
        with(binding.ratesViewGroup) {
            this@RatesFragment.adapter.setHasStableIds(true)
            setHasFixedSize(true)
            val itemAnimator = DefaultItemAnimator()
            itemAnimator.supportsChangeAnimations = false
            this.itemAnimator = itemAnimator
            layoutManager = LinearLayoutManager(activity)
            adapter = this@RatesFragment.adapter
            addItemDecoration(
                DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
            )
        }
    }

    override fun updateItems(items: List<CurrencyItem>) {
        items.forEach { adapter.addItem(it) }
    }

    override fun resetList() {
        adapter.resetList()
        adapter.notifyDataSetChanged()
    }

    override fun showErrorMessage(errorMessageResId: Int?) {
        offlineSnackbar =
            activity?.showErrorSnackBar(R.id.frame_layout,
                errorMessageResId ?: R.string.undefined_error_message)
    }

    fun scrollToTop() {
        val layoutManager = binding.ratesViewGroup.layoutManager as LinearLayoutManager?
        if (layoutManager != null) {
            binding.ratesViewGroup.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    if (binding.ratesViewGroup.scrollState == SCROLL_STATE_IDLE) {
                        if (layoutManager.findFirstVisibleItemPosition() == 0) {
                            expandAppBarWhenOnScrolledToTop()
                        }
                        binding.ratesViewGroup.removeOnScrollListener(this)
                    }
                }
            })
            if (layoutManager.findFirstVisibleItemPosition() > 1) {
                binding.ratesViewGroup.smoothScrollToPosition(0)
            } else {
                binding.ratesViewGroup.scrollToPosition(0)
            }
        }
    }

    private fun expandAppBarWhenOnScrolledToTop() {
        (activity as MainActivity?)?.expandAppBar()
    }

    companion object {
        fun newInstance(): RatesFragment {
            val fragment = RatesFragment()
            fragment.retainInstance = true
            return fragment
        }
    }
}