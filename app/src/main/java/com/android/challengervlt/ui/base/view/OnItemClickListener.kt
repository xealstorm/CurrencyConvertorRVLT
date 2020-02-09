package com.android.challengervlt.ui.base.view

import android.view.View

interface OnItemClickListener<T> {
    fun onItemClicked(item: T)
}