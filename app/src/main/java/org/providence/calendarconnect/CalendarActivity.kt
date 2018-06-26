package org.providence.calendarconnect

import android.os.Bundle
import android.support.v7.app.AppCompatActivity

class CalendarActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.calendar_activity)

        // Add Calendar Fragment
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, CalendarFragment())
            .commit()
    }
}
