package com.android.challengervlt.ui.list.view

import android.view.LayoutInflater
import androidx.databinding.DataBindingUtil
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.challengervlt.R
import com.android.challengervlt.databinding.RowItemBinding
import com.android.challengervlt.model.CurrencyItem
import com.android.challengervlt.ui.base.view.BaseAdapter

class RatesAdapter(private val data: MutableList<CurrencyItem> = arrayListOf<CurrencyItem>()) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>(), BaseAdapter<CurrencyItem> {

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewModel = data[position]

        (holder as ItemViewHolder).binding.currencyCode.text = viewModel.code
        holder.binding.valueInput.text =
            String.format(
                holder.binding.root.context.getString(R.string.rate_format),
                viewModel.rateValue
            )
        holder.binding.currencyName.text = viewModel.title
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RowItemBinding.inflate(inflater, parent, false)
        return ItemViewHolder(binding.getRoot())
    }

    override fun getItemCount() = data.size

    override fun addItem(model: CurrencyItem) {
        addOrUpdate(model)
    }

    override fun resetList() {
        data.clear()
    }

    private fun addOrUpdate(model: CurrencyItem) {
        val index = data.indexOfFirst { it.code.equals(model.code) }
        if (index < 0) {
            data.add(model)
        } else {
            data[index] = model
        }
    }

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding: RowItemBinding = DataBindingUtil.bind(itemView)!!
    }
}

