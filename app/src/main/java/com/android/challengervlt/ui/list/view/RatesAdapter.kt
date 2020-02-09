package com.android.challengervlt.ui.list.view

import android.view.LayoutInflater
import androidx.databinding.DataBindingUtil
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.android.challengervlt.R
import com.android.challengervlt.ui.base.view.OnItemClickListener
import com.android.challengervlt.databinding.RowItemBinding
import com.android.challengervlt.model.CurrencyItem
import com.android.challengervlt.ui.base.view.BaseAdapter

class RatesAdapter(private val data: MutableList<CurrencyItem> = arrayListOf<CurrencyItem>()) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>(), BaseAdapter<CurrencyItem> {

    private var clickListener: OnItemClickListener<CurrencyItem>? = null

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currencyitem = data[position]

        (holder as ItemViewHolder).binding.currencyCode.text = currencyitem.code
        holder.binding.valueInput.text =
            String.format(
                holder.binding.root.context.getString(R.string.rate_format),
                currencyitem.rateValue
            )
        holder.binding.currencyName.text = currencyitem.title
        holder.binding.root.setOnClickListener {
            swapOnClick()
        }
    }

    private fun swapOnClick(currencyitem: CurrencyItem) {
        val currentPosition = data.indexOf(currencyitem)
        data.swap(currentPosition, 0)
        notifyItemMoved(currentPosition, 0)
        if (clickListener != null) {
            clickListener!!.onItemClicked(currencyitem)
        }
    }

    fun setOnItemClickListener(clickListener: OnItemClickListener<CurrencyItem>) {
        this.clickListener = clickListener
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

    inline fun <reified T> MutableList<T>.swap(sourceIndex: Int, destinationIndex: Int) {
        if (sourceIndex != destinationIndex && sourceIndex >= 0 && destinationIndex >= 0) {
            val datum = this[sourceIndex]
            if (sourceIndex < destinationIndex) {
                this.add(destinationIndex, datum)
                this.removeAt(sourceIndex)
            } else {
                this.removeAt(sourceIndex)
                this.add(destinationIndex, datum)
            }
        }
    }

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding: RowItemBinding = DataBindingUtil.bind(itemView)!!
    }
}

