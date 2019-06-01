package com.example.sonyadmin

import android.content.Context
import android.content.res.Resources
import net.danlew.android.joda.JodaTimeAndroid
import org.joda.time.DateTime
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.mockito.ArgumentMatchers
import org.mockito.Mockito
import java.io.InputStream

class MyCoroutineWorkerKtTest {

    @Before
    fun onStart(){
        val context = Mockito.mock(Context::class.java)
        val appContext = Mockito.mock(Context::class.java)
        val resources = Mockito.mock(Resources::class.java)
        Mockito.`when`(resources.openRawResource(ArgumentMatchers.anyInt()))
            .thenReturn(Mockito.mock(InputStream::class.java))
        Mockito.`when`(appContext.getResources()).thenReturn(resources)
        Mockito.`when`(context.getApplicationContext()).thenReturn(appContext)
        JodaTimeAndroid.init(context)
    }

    @Test
    fun isWorkingDayFinished() {
        assertEquals( true,
            isWorkingDayFinished( DateTime.now().minusDays(1).withHourOfDay(9).withMinuteOfHour(0),
                DateTime.now().withHourOfDay(9).withMinuteOfHour(0)))
    }
    @Test
    fun isWorkingDayFinished_00() {
        assertEquals( true,
            isWorkingDayFinished( DateTime.now().withHourOfDay(0).withMinuteOfHour(0),
                DateTime.now().withHourOfDay(9).withMinuteOfHour(0).withSecondOfMinute(0)))
    }
    @Test
    fun isWorkingDayFinished_09() {
        assertEquals( false,
            isWorkingDayFinished( DateTime.now().withHourOfDay(9).withMinuteOfHour(0),
                DateTime.now().withHourOfDay(12).withMinuteOfHour(10)))
    }
    @Test
    fun isWorkingDayFinished_9_18() {
        assertEquals( false,
            isWorkingDayFinished( DateTime.now().withHourOfDay(9).withMinuteOfHour(0),
                DateTime.now().withHourOfDay(18).withMinuteOfHour(0)))
    }

    @Test
    fun isWorkingDayFinished_9_21() {
        assertEquals( false,
            isWorkingDayFinished( DateTime.now().withHourOfDay(9).withMinuteOfHour(0),
                DateTime.now().withHourOfDay(21).withMinuteOfHour(0)))
    }
    @Test
    fun isWorkingDayFinished_9_23() {
        assertEquals( false,
            isWorkingDayFinished( DateTime.now().withHourOfDay(9).withMinuteOfHour(0),
                DateTime.now().withHourOfDay(23).withMinuteOfHour(59)))
    }

    @Test
    fun isWorkingDayFinished_9_next_01() {
        assertEquals( false,
            isWorkingDayFinished( DateTime.now().minusDays(1).withHourOfDay(9).withMinuteOfHour(0),
                DateTime.now().withHourOfDay(1).withMinuteOfHour(59)))
    }

    @Test
    fun isWorkingDayFinished_9_next_09() {
        assertEquals( true,
            isWorkingDayFinished( DateTime.now().minusDays(1).withHourOfDay(9).withMinuteOfHour(0),
                DateTime.now().withHourOfDay(9).withMinuteOfHour(0)))
    }

    @Test
    fun isWorkingDayFinished_9_next_08() {
        assertEquals( false,
            isWorkingDayFinished( DateTime.now().minusDays(1).withHourOfDay(9).withMinuteOfHour(0),
                DateTime.now().withHourOfDay(8).withMinuteOfHour(59)))
    }

    @Test
    fun isWorkingDayFinished_9_next_11() {
        assertEquals( true,
            isWorkingDayFinished( DateTime.now().minusDays(1).withHourOfDay(9).withMinuteOfHour(0),
                DateTime.now().withHourOfDay(10).withMinuteOfHour(0)))
    }

    @Test
    fun isWorkingDayFinished_previous_day_9_next_11() {
        assertEquals( true,
            isWorkingDayFinished( DateTime.now().minusDays(2).withHourOfDay(9).withMinuteOfHour(0),
                DateTime.now().withHourOfDay(10).withMinuteOfHour(0)))
    }

    @Test
    fun from_00_09(){
        assertEquals( true,
            isWorkingDayFinished( DateTime.now().withHourOfDay(4).withMinuteOfHour(0),
                DateTime.now().withHourOfDay(18).withMinuteOfHour(0)))
    }

    @Test
    fun isWorkingDayFinished_00_00() {
        assertEquals( false,
            isWorkingDayFinished( DateTime.now().withHourOfDay(9).withMinuteOfHour(0),
                DateTime.now().withHourOfDay(0).withMinuteOfHour(0)))
    }
}