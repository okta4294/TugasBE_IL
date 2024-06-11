package com.breens.todoapp.common

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// This function is used to set the time the task created
fun getCurrentTimeAsString(): String {
    val currentTime = Date()
    val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    val currentTimeString = dateFormat.format(currentTime)
    return currentTimeString
}

// This function is used to format the created task date to an intended format
fun convertDateFormat(dateString: String): String {
    val currentDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    val newDateFormat = SimpleDateFormat("EEEE MMMM yyyy, h:mma", Locale.getDefault())
    val date = currentDateFormat.parse(dateString)
    val newDateString = newDateFormat.format(date)
    return newDateString
}
