package com.android.challengervlt.util.format

import org.joda.time.DateTime
import org.junit.Assert.assertEquals
import org.junit.Test

class TimeFormatterTest {

    @Test
    fun `yyyy-MM-dd is supported`() {
        assertEquals(TimeFormatter.dateDeFormatted(CORRECT_DATE_MOCK), DATE_TIME_MOCK)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `dd-MM-yyyy is not supported`() {
        TimeFormatter.dateDeFormatted(INCORRECT_DATE_MOCKS[0])
    }

    @Test(expected = IllegalArgumentException::class)
    fun `dd(dot)MM(dot)yyyy is not supported`() {
        TimeFormatter.dateDeFormatted(INCORRECT_DATE_MOCKS[1])
    }

    @Test(expected = IllegalArgumentException::class)
    fun `yyyy(dot)MM(dot)dd is not supported`() {
        TimeFormatter.dateDeFormatted(INCORRECT_DATE_MOCKS[2])
    }

    companion object {
        const val CORRECT_DATE_MOCK = "2020-02-15"
        val INCORRECT_DATE_MOCKS = listOf("15-02-2020", "15.02.2020", "2020.02.15")
        val DATE_TIME_MOCK = DateTime(1581721200000)
    }
}