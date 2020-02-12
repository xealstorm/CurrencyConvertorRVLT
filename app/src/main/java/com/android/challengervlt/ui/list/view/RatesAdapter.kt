package com.android.challengervlt.ui.list.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.android.challengecoup.util.ui.CircleTransform
import com.android.challengervlt.BuildConfig
import com.android.challengervlt.R
import com.android.challengervlt.databinding.RowItemBinding
import com.android.challengervlt.model.CurrencyItem
import com.android.challengervlt.ui.base.view.BaseAdapter
import com.android.challengervlt.ui.base.view.OnItemClickListener
import com.squareup.picasso.Picasso

class RatesAdapter(private val data: MutableList<CurrencyItem> = arrayListOf<CurrencyItem>()) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>(), BaseAdapter<CurrencyItem> {

    var clickListener: OnItemClickListener<CurrencyItem>? = null

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currencyItem = data[position]

        (holder as ItemViewHolder).binding.currencyCode.text = currencyItem.code
        holder.binding.valueInput.text =
            String.format(
                holder.binding.root.context.getString(R.string.rate_format),
                currencyItem.rateValue
            )
        holder.binding.currencyName.text = currencyItem.title
        holder.binding.root.setOnClickListener {
            clickListener?.onItemClicked(currencyItem)
        }
        Picasso
            .with(holder.binding.root.context)
            .load(
                String.format(
                    BuildConfig.FLAGS_URL,
                    currencyItem.countryCode.toLowerCase()
                )
            )
            .transform(CircleTransform())
            .into(holder.binding.currencyIcon)
    }

    fun swapOnClick(currencyItem: CurrencyItem) {
        val currentPosition = data.indexOf(currencyItem)
        data.swap(currentPosition, 0)
        notifyItemMoved(currentPosition, 0)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RowItemBinding.inflate(inflater, parent, false)
        return ItemViewHolder(binding.root)
    }

    override fun getItemCount() = data.size

    override fun addItem(model: CurrencyItem) {
        addOrUpdate(model)
    }

    override fun resetList() {
        data.clear()
    }

    override fun getItemId(position: Int): Long {
        return data.get(position).code.hashCode().toLong()
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
        if (sourceIndex != destinationIndex && sourceIndex >= 0 && destinationIndex >= 0
            && sourceIndex < this.size && destinationIndex < this.size
        ) {
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

