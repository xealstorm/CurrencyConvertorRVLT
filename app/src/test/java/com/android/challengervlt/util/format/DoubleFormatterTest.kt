package com.android.challengervlt.util.format

import org.junit.Assert.assertEquals
import org.junit.Test

class DoubleFormatterTest {

    @Test
    fun `numbers rounded correctly`() {
        assertEquals(DoubleFormatter.doubleFormatted(20.224), "20.22")
        assertEquals(DoubleFormatter.doubleFormatted(20.225), "20.23")
        assertEquals(DoubleFormatter.doubleFormatted(20.226), "20.23")
    }

    @Test
    fun `zeros after separators unused`() {
        assertEquals(DoubleFormatter.doubleFormatted(1.001), "1")
        assertEquals(DoubleFormatter.doubleFormatted(1.0), "1")
        assertEquals(DoubleFormatter.doubleFormatted(10.2001), "10.2")
        assertEquals(DoubleFormatter.doubleFormatted(0.0), "0")
    }
}