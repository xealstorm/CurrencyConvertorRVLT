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
import com.android.challengervlt.util.extensions.showClipboardSnackBar
import com.android.challengervlt.util.extensions.showErrorSnackBar
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
                handleConnectionChanged(connected)
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

    override fun onPause() {
        super.onPause()
        presenter.pauseUpdates()
    }

    override fun doInjections(activityComponent: ActivityComponent) {
        activityComponent.plus(RatesModule()).injectTo(this)
    }

    private fun initRecyclerView() {
        adapter.clickListener = object : OnItemClickListener<CurrencyItem> {
            override fun onItemClicked(item: CurrencyItem) {
                if (binding.ratesViewGroup.layoutManager?.isSmoothScrolling == false) {
                    handleItemClick(item)
                }
            }
        }
        adapter.longClickListener = object : OnItemClickListener<CurrencyItem> {
            override fun onItemClicked(item: CurrencyItem) {
                handleItemLongClick(item)
            }
        }
        with(binding.ratesViewGroup) {
            this@RatesFragment.adapter.setHasStableIds(true)
            setHasFixedSize(true)
            val itemAnimator = DefaultItemAnimator()
            itemAnimator.supportsChangeAnimations = false // to prevent the list from blinking
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
            activity?.showErrorSnackBar(
                R.id.frame_layout,
                errorMessageResId ?: R.string.undefined_error_message
            )
    }

    override fun updateOfflineCurrencies(currenciesWithResults: List<String>?) {
        adapter.updateOfflineCurrencies(currenciesWithResults)
    }

    /**
     * Makes the recycler view scroll to top
     * Expands the app bar if needed
     */
    private fun scrollToTop() {
        val positionToStartSmoothScroll = 9
        val layoutManager = binding.ratesViewGroup.layoutManager as LinearLayoutManager?
        if (layoutManager != null) {
            // if we are on the top of the page – no need to scroll smoothly
            if (layoutManager.findFirstVisibleItemPosition() > positionToStartSmoothScroll) {
                binding.ratesViewGroup.scrollToPosition(positionToStartSmoothScroll)
                binding.ratesViewGroup.smoothScrollToPosition(0)
            } else {
                binding.ratesViewGroup.scrollToPosition(0)
            }
        }
    }

    private fun expandAppBarWhenOnScrolledToTop() {
        (activity as MainActivity?)?.expandAppBar()
    }

    private fun handleConnectionChanged(isConnected: Boolean?) {
        if (isConnected == true) {
            // remove the snackbar
            offlineSnackbar?.dismiss()
            offlineSnackbar = null
            // make all items clickable
            updateOfflineCurrencies()
            // update currency rates
            presenter.loadItems()
        } else if (isConnected == false) {
            // set up the clickable items
            updateOfflineCurrencies(presenter.getCurrenciesWithResult())
        }
    }

    private fun handleItemClick(currencyItem: CurrencyItem) {
        presenter.pauseUpdates()
        adapter.swapOnClick(currencyItem)
        scrollToTop()
        presenter.loadItems(currencyItem.code)
    }

    private fun handleItemLongClick(currencyItem: CurrencyItem) {
        // copy to clipboard
        val clipboard: ClipboardManager? =
            getSystemService<ClipboardManager>(context!!, ClipboardManager::class.java)
        val clip = ClipData.newPlainText(currencyItem.code, currencyItem.inputValue.toString())
        clipboard?.setPrimaryClip(clip)
        // notify with the snackbar
        // if we are offline – show offline snackbar again afterwards
        activity?.showClipboardSnackBar { offlineSnackbar?.show() }

    }

    companion object {
        fun newInstance(): RatesFragment {
            val fragment = RatesFragment()
            fragment.retainInstance = true
            return fragment
        }
    }
}