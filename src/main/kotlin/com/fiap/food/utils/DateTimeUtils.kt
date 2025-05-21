package com.fiap.food.utils

import java.text.SimpleDateFormat
import java.time.Duration
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.util.*

fun LocalDateTime.toBrazilianTime(): String{
    val dateMilli = this.toInstant(ZoneOffset.UTC).toEpochMilli()
    val simpleDateFormat = SimpleDateFormat("yyy-MM-dd HH:mm:ss")
    simpleDateFormat.timeZone = TimeZone.getTimeZone(ZoneId.of("America/Sao_Paulo"))
    return simpleDateFormat.format(dateMilli)
}

fun formatDuration(duration: Duration): String {
    val minutes = duration.toMinutes()
    val seconds = duration.seconds % 60
    return if (minutes > 0) {
        "$minutes minuto${if (minutes > 1) "s" else ""} ${if (seconds > 0) "$seconds segundo${if (seconds > 1) "s" else ""}" else ""}".trim()
    } else {
        "$seconds segundo${if (seconds > 1) "s" else ""}"
    }
}