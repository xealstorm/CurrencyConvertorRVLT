package com.android.challengervlt.ui.list.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.android.challengervlt.BuildConfig
import com.android.challengervlt.databinding.RowItemBinding
import com.android.challengervlt.model.CurrencyItem
import com.android.challengervlt.ui.base.view.BaseAdapter
import com.android.challengervlt.ui.base.view.OnItemClickListener
import com.android.challengervlt.util.format.DoubleFormatter
import com.android.challengervlt.util.log.L
import com.android.challengervlt.util.picasso.CircleTransform
import com.jakewharton.rxbinding2.widget.RxTextView
import com.squareup.picasso.Picasso
import io.reactivex.Observable

class RatesAdapter(private val data: MutableList<CurrencyItem> = arrayListOf<CurrencyItem>()) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>(), BaseAdapter<CurrencyItem> {

    var clickListener: OnItemClickListener<CurrencyItem>? = null
    var longClickListener: OnItemClickListener<CurrencyItem>? = null
    private var offlineCurrencies: List<String>? = null
    /**
     * The input of a user. All rates should be multiplied by this field when shown in UI.
     */
    private var inputBaseValue: Double = 1.0

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currencyItem = data[position]

        (holder as ItemViewHolder).binding.currencyCode.text = currencyItem.code
        holder.binding.valueInput.setText(
            if (inputBaseValue != 0.0) {
                DoubleFormatter.doubleFormatted(inputBaseValue * currencyItem.rateValue)
            } else {
                "" // in order to show a placeholder when the result equals to zero – return empty string
            }
        )
        // input field is enabled only for the first element
        holder.binding.valueInput.isEnabled = position == 0

        if (holder.binding.valueInput.isEnabled) {
            holder.binding.valueInput.requestFocus()
            // move the cursor to the end
            holder.binding.valueInput.setSelection(holder.binding.valueInput.length())
        }

        // if the field is changed and it's the top one - the inputBaseValue should be changed
        holder.valueInputTextChangeObservable
            .subscribe {
                if (data.indexOfFirst { item -> item.code == holder.binding.currencyCode.text.toString() } == 0) {
                    try {
                        inputBaseValue = if (it.isEmpty()) {
                            0.0
                        } else {
                            it.toDouble()
                        }
                        notifyItemRangeChanged(1, itemCount - 1)
                    } catch (e: NumberFormatException) {
                        L.e("The input format cannot be converted into a double", e)
                    } catch (e: IllegalStateException) {
                        L.e("The item range cannot be updated right now", e)
                    }
                }
            }

        holder.binding.currencyName.text = currencyItem.title
        holder.binding.root.isEnabled =
            offlineCurrencies == null || offlineCurrencies!!.contains(currencyItem.code)

        holder.binding.root.setOnClickListener {
            currencyItem.inputValue = holder.binding.valueInput.text.toString().toDouble()
            clickListener?.onItemClicked(currencyItem)
        }
        holder.binding.root.setOnLongClickListener {
            currencyItem.inputValue = holder.binding.valueInput.text.toString().toDouble()
            longClickListener?.onItemClicked(currencyItem)
            return@setOnLongClickListener true
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

    /**
     * Moves the current currencyItem to the top of the list
     */
    fun swapOnClick(currencyItem: CurrencyItem) {
        inputBaseValue = currencyItem.inputValue
        val currentPosition = data.indexOf(currencyItem)
        data.swap(currentPosition, 0)
        notifyItemMoved(currentPosition, 0)
    }

    /**
     * Sets the list of currency codes that are able to be base currencies in offline mode
     */
    fun updateOfflineCurrencies(currenciesWithResults: List<String>? = null) {
        this.offlineCurrencies = currenciesWithResults
        notifyDataSetChanged()
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
        return data[position].code.hashCode().toLong()
    }

    private fun addOrUpdate(model: CurrencyItem) {
        val index = data.indexOfFirst { it.code.equals(model.code) }
        if (index < 0) {
            data.add(model)
            notifyItemInserted(itemCount)
        } else if (data[index] != model) {
            data[index].rateValue = model.rateValue
            notifyItemChanged(index)
        }
    }

    private inline fun <reified T> MutableList<T>.swap(sourceIndex: Int, destinationIndex: Int) {
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

        val valueInputTextChangeObservable: Observable<String> = RxTextView.textChanges(binding.valueInput)
            .map {
                it.toString()
            }
    }
}

