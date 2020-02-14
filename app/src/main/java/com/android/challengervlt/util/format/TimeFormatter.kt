package com.android.challengervlt.util.format

import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat

object TimeFormatter {
    private const val DATE_FORMAT = "yyyy-MM-dd"

    fun dateDeFormatted(date: String?): DateTime? {
        return DateTime.parse(date, DateTimeFormat.forPattern(DATE_FORMAT))
    }
}