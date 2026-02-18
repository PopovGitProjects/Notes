package com.popov.dev.notes.presentation.utils

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.concurrent.TimeUnit

object DateFormater {
    val millisInHour = TimeUnit.HOURS.toMillis(1)
    val millisInDay = TimeUnit.DAYS.toMillis(1)
    val formater: DateFormat = SimpleDateFormat.getDateInstance(
        DateFormat.SHORT
    )

    fun formatDateToString(timestamp: Long): String {
        val now = System.currentTimeMillis()
        val diff = now - timestamp
        return when {
            diff < millisInHour -> "Just now"
            diff < millisInDay -> {
                val hours = TimeUnit.MILLISECONDS.toHours(diff)
                "$hours h ago"
            }

            else -> {
                formater.format(timestamp)
            }
        }
    }
}