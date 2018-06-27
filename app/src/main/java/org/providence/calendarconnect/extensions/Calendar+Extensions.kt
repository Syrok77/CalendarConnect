package org.providence.calendarconnect.extensions

import java.util.Calendar

/**
 * Rest current instance to the beginning of the day
 * NOTE: implies [Calendar.setTime] has been called
 */
inline fun Calendar.startOfDay() {
    set(Calendar.HOUR_OF_DAY, 0)
    set(Calendar.MINUTE, 0)
    set(Calendar.SECOND, 0)
    set(Calendar.MILLISECOND, 0)
}