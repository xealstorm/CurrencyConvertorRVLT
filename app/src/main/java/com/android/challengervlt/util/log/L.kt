package com.android.challengervlt.util.log

import android.util.Log
import com.android.challengervlt.BuildConfig

// This class has been written outside of the challenge and being just reused here
object L {
    private val TAG = "Log"
    private val LOG_ENABLED = BuildConfig.DEBUG
    private val MAX_SYMBOLS_PER_ROW = 120

    fun d(message: String) {
        d(TAG, message)
    }

    fun d(tag: String, message: String) {
        if (isLogEnable(tag, message)) {
            for (i in 0..message.length / MAX_SYMBOLS_PER_ROW) {
                val start = i * MAX_SYMBOLS_PER_ROW
                var end = (i + 1) * MAX_SYMBOLS_PER_ROW
                end = if (end > message.length) message.length else end
                Log.d(tag, message.substring(start, end))
            }
        }
    }

    fun e(e: Throwable) {
        e(TAG, e.message, e)
    }

    fun e(message: String, e: Throwable) {
        e(TAG, message, e)
    }

    fun e(tag: String, message: String) {
        if (isLogEnable(tag, message)) {
            Log.e(tag, message)
        }
    }

    fun e(tag: String, message: String?, e: Throwable) {
        if (isLogEnable(tag, message)) {
            Log.e(tag, message, e)
        }
    }

    fun i(tag: String, message: String) {
        if (isLogEnable(tag, message)) {
            Log.i(tag, message)
        }
    }

    fun v(tag: String, message: String) {
        if (isLogEnable(tag, message)) {
            Log.v(tag, message)
        }
    }

    fun w(tag: String, message: String) {
        if (isLogEnable(tag, message)) {
            Log.w(tag, message)
        }
    }

    fun w(tag: String, message: String, e: Throwable) {
        if (isLogEnable(tag, message)) {
            Log.w(tag, message, e)
        }
    }

    private fun isLogEnable(tag: String?, message: String?): Boolean {
        return LOG_ENABLED && !tag.isNullOrEmpty() && !message.isNullOrEmpty()
    }

}