package com.android.challengervlt.util.format

import java.text.DecimalFormat
import java.text.NumberFormat

object DoubleFormatter {
    var numberFormat: NumberFormat = DecimalFormat("#.##")

    fun doubleFormatted(double: Double): String {
        return numberFormat.format(double)
    }
}