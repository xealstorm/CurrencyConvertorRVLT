package com.android.challengervlt.util.format

import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat

object TimeFormatter {
    val DATE_FORMAT = "yyyy-MM-dd"

    fun dateFormatted(dateTime: DateTime): String? {
        return dateTime.toString(DATE_FORMAT)
    }

    fun dateDeFormatted(date: String?): DateTime? {
        return DateTime.parse(date, DateTimeFormat.forPattern(DATE_FORMAT))
    }
}