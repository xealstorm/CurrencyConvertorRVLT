package com.android.challengervlt.util.extensions

import android.app.Activity
import android.view.View
import com.android.challengervlt.R
import com.google.android.material.snackbar.Snackbar

fun Activity.showErrorSnackBar(
    rootViewRes: Int,
    stringRes: Int
): Snackbar {
    val snackbar = Snackbar.make(findViewById<View>(rootViewRes), stringRes, Snackbar.LENGTH_INDEFINITE)
    showSnackbar(snackbar)
    return snackbar
}

fun Activity.showClipboardSnackBar(
    rootView: View = findViewById(R.id.frame_layout),
    stringRes: Int = R.string.copied_to_clipboard_message,
    dismissedAction: () -> Unit
): Snackbar {
    val snackbar = Snackbar.make(rootView, stringRes, Snackbar.LENGTH_SHORT)
    snackbar.addCallback(object : Snackbar.Callback() {
        override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
            dismissedAction()
        }
    })
    showSnackbar(snackbar)
    return snackbar
}


private fun showSnackbar(snackbar: Snackbar) = snackbar.apply { show() }