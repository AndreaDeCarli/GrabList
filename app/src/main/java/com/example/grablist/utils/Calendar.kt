package com.example.grablist.utils

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.CalendarContract
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

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

    val oneHour = 60 * 60 * 1000L

    val values = ContentValues().apply {
        put(CalendarContract.Events.DTSTART, dateInMillis + oneHour*14)
        put(CalendarContract.Events.DTEND, dateInMillis + oneHour *16)
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