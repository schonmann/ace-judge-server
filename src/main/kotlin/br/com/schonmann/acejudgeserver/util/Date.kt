package br.com.schonmann.acejudgeserver.util

import java.text.SimpleDateFormat
import java.util.*

fun Date.toString(format : String, locale : Locale = Locale.getDefault()) : String {
    val formatter = SimpleDateFormat(format, locale)
    return formatter.format(this)
}

fun Date.sumTimeString(timeString : String) : Date {
    val hourString = timeString.split(":")[0]
    val minuteString = timeString.split(":")[1]

    val hourInt = Integer.parseInt(hourString)
    val minuteInt = Integer.parseInt(minuteString)

    val dateCal = Calendar.getInstance()

    dateCal.time = this

    dateCal.add(Calendar.HOUR, hourInt)
    dateCal.add(Calendar.MINUTE, minuteInt)

    return dateCal.time
}