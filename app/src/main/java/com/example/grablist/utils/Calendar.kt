package com.example.grablist.utils

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.CalendarContract
import com.example.grablist.utils.CalendarVariables.Companion.oneDay
import com.example.grablist.utils.CalendarVariables.Companion.oneHour
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone


class CalendarVariables{
    companion object{
        val oneHour: Long = 60 * 60 * 1000L
        val oneDay: Long = oneHour * 24
        val oneWeek: Long = oneDay * 7
    }
}


fun getPrimaryCalendarId(context: Context): Long? {
    val projection = arrayOf(
        CalendarContract.Calendars._ID,
        CalendarContract.Calendars.IS_PRIMARY,
        CalendarContract.Calendars.VISIBLE
    )
    val uri = CalendarContract.Calendars.CONTENT_URI
    val selection = "${CalendarContract.Calendars.VISIBLE} = 1"
    val cursor: Cursor? = context.contentResolver.query(uri, projection, selection, null, null)
    cursor?.use {
        while (it.moveToNext()) {
            val id = it.getLong(it.getColumnIndexOrThrow(CalendarContract.Calendars._ID))
            val isPrimary = it.getInt(it.getColumnIndexOrThrow(CalendarContract.Calendars.IS_PRIMARY))
            if (isPrimary == 1) {
                return id
            }
        }
        if (it.moveToFirst()) {
            return it.getLong(it.getColumnIndexOrThrow(CalendarContract.Calendars._ID))
        }
    }
    return null
}


fun addCalendarEvent(
    date: String,
    ctx: Context,
    title: String,
    description: String,
    calendarId: Long?
): Uri?{

    val formatter = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
    val dateInMillis = requireNotNull(formatter.parse(date)?.time)



    val values = ContentValues().apply {
        put(CalendarContract.Events.DTSTART, dateInMillis + oneDay)
        put(CalendarContract.Events.DTEND, dateInMillis + oneDay * 2)
        put(CalendarContract.Events.ALL_DAY, 1)
        put(CalendarContract.Events.TITLE, title)
        put(CalendarContract.Events.DESCRIPTION, description)
        put(CalendarContract.Events.CALENDAR_ID, calendarId)
        put(CalendarContract.Events.EVENT_TIMEZONE, TimeZone.getDefault().id)
    }

    val uri: Uri? = ctx.contentResolver.insert(
        CalendarContract.Events.CONTENT_URI,
        values
    )

    return uri
}