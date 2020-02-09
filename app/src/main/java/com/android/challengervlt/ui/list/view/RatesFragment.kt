package com.android.challengervlt.ui.list.view

import androidx.databinding.DataBindingUtil
import android.os.Bundle
import com.android.challengervlt.R
import com.android.challengervlt.databinding.FragmentItemsListBinding
import com.android.challengervlt.di.ActivityComponent
import com.android.challengervlt.di.RatesModule
import com.android.challengervlt.ui.base.view.BaseFragment
import com.android.challengervlt.ui.list.presenter.RatesPresenter
import javax.inject.Inject
import android.view.*
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.challengervlt.model.CurrencyItem


class RatesFragment : BaseFragment(), RatesView {
    @Inject
    lateinit var presenter: RatesPresenter
    @Inject
    lateinit var adapter: RatesAdapter
    lateinit var binding: FragmentItemsListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_items_list, container, false)
        return binding.getRoot()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initRecyclerView()
        presenter.setView(this)
    }

    override fun onResume() {
        super.onResume()
        presenter.generateList()
    }

    override fun doInjections(activityComponent: ActivityComponent) {
        activityComponent.plus(RatesModule()).injectTo(this)
    }

    fun initRecyclerView() {
        adapter.setOnItemClickListener(object : OnItemClickListener<CurrencyItem> {
            override fun onItemClicked(item: CurrencyItem) {
                Handler().postDelayed(
                    { binding.ratesViewGroup.smoothScrollToPosition(0) },
                    DELAY_MILLLIS
                )
            }
        })
        with(binding.ratesViewGroup) {
            setHasFixedSize(true)
            setItemAnimator(DefaultItemAnimator())
            setLayoutManager(LinearLayoutManager(activity))
            setAdapter(this@RatesFragment.adapter)
            addItemDecoration(
                androidx.recyclerview.widget.DividerItemDecoration(
                    context,
                    androidx.recyclerview.widget.DividerItemDecoration.VERTICAL
                )
            )
        }
    }

    override fun updateItems(items: List<CurrencyItem>) {
        items.forEach { adapter.addItem(it) }
        adapter.notifyDataSetChanged()
    }

    override fun resetList() {
        adapter.resetList()
        adapter.notifyDataSetChanged()
    }

    companion object {
        private val DELAY_MILLLIS = 200L

        fun newInstance(): RatesFragment {
            val fragment = RatesFragment()
            fragment.retainInstance = true
            return fragment
        }
    }
}