package com.android.challengervlt.ui.base.view

interface BaseAdapter<T> {
    fun addItem(model: T)

    fun resetList()
}