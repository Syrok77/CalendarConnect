package org.providence.calendarconnect.provider

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.provider.CalendarContract
import android.support.v4.content.ContextCompat
import android.util.Log
import org.providence.calendarconnect.extensions.startOfDay
import java.util.Calendar
import java.util.Date

/**
 * Provides access to the user's events across all calendars
 */
class CalendarEventsProvider constructor(private val context: Context) {

    companion object {
        private const val TAG = "CalendarEventsProvider"
    }

    fun eventsFor(start: Long, duration: Long): List<CalendarEvent> {
        val end = start + duration * 60 * 1000

        // Get calendar matches for events within [start] + [duration] AND where [start] + [duration] is contained within an event
        val selection = "(${CalendarContract.Events.ALL_DAY} = 0 AND (${CalendarContract.Events.DTSTART} >= ? AND ${CalendarContract.Events.DTSTART} <= ?) OR (${CalendarContract.Events.DTEND} >= ? AND ${CalendarContract.Events.DTEND} <= ?) OR (${CalendarContract.Events.DTSTART} < ? AND ${CalendarContract.Events.DTEND} > ?))"
        val selectionArgs = arrayOf(start.toString(), end.toString(), start.toString(), end.toString(), start.toString(), end.toString())
        return parseEvents(selection, selectionArgs)
    }

    fun hasEventsFor(start:Long, duration: Long) = eventsFor(start,duration).size > 0

    @SuppressLint("MissingPermission")
    private fun parseEvents(selection: String, selectionArgs: Array<String>): List<CalendarEvent> {
        val events = ArrayList<CalendarEvent>()
        if (canReadCalendar()) {
            val projection = arrayOf(CalendarContract.Events.DTSTART, CalendarContract.Events.DTEND, CalendarContract.Events.TITLE, CalendarContract.Events.CALENDAR_COLOR)
            val cursor = context.contentResolver.query(
                CalendarContract.Events.CONTENT_URI,
                projection,
                selection,
                selectionArgs,
                null)

            while (cursor.moveToNext()) {
                val calendarEvent = CalendarEvent(
                    start = cursor.getLong(0),
                    end = cursor.getLong(1),
                    title = cursor.getString(2)
                )
                events.add(calendarEvent)
                Log.d(TAG, "Added ${calendarEvent}")
            }
            cursor.close()
        }
        return events
    }

    /**
     * Get all calendar events for [date]
     */
    fun eventsForDay(date: Date): List<CalendarEvent> {
        val cal = Calendar.getInstance()
        cal.timeInMillis = date.time
        cal.startOfDay()
        Log.d(TAG, "Today ${cal.timeInMillis}")

        val nextDay = cal.clone() as Calendar
        nextDay.add(Calendar.DAY_OF_YEAR, 1)
        Log.d(TAG, "Next day is ${nextDay.timeInMillis}")

        // Get calendar matches for events (ignore all day events)
        val selection = "(${CalendarContract.Events.ALL_DAY} = 0 AND (${CalendarContract.Events.DTSTART} >= ? AND ${CalendarContract.Events.DTSTART} <= ?))"
        val selectionArgs = arrayOf(cal.timeInMillis.toString(), nextDay.timeInMillis.toString())
        return parseEvents(selection, selectionArgs)
    }

    private fun canReadCalendar() = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CALENDAR) == PackageManager.PERMISSION_GRANTED
}

data class CalendarEvent(val title: String, val start: Long, val end: Long)
