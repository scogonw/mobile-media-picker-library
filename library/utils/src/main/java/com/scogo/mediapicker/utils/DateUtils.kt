package com.scogo.mediapicker.utils

import android.content.Context
import java.text.SimpleDateFormat
import java.util.*

fun convertTimeToStringDate(
    context: Context,
    time: Long
): String {
    val date = Date(time * 100)
    val c = Calendar.getInstance()
    c.time = date

    val lastMonth = Calendar.getInstance().apply {
        add(Calendar.DAY_OF_MONTH,-Calendar.DAY_OF_MONTH)
    }
    val lastWeek = Calendar.getInstance().apply {
        add(Calendar.DAY_OF_MONTH,-7)
    }
    val recent = Calendar.getInstance().apply {
        add(Calendar.DAY_OF_MONTH,-2)
    }
    return if(c.before(lastMonth)) {
        SimpleDateFormat("MMMM", Locale.ENGLISH).format(date)
    }else if(c.after(lastMonth) && c.before(lastWeek)) {
        "Last Month"
    }else if(c.after(lastWeek) && c.before(recent)) {
        "Last Week"
    } else {
        "Recent"
    }
}