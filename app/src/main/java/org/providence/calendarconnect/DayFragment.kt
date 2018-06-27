package org.providence.calendarconnect

import android.content.res.ColorStateList
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.tomatron.RecyclerCell
import com.tomatron.RecyclerCellAdapter
import kotlinx.android.synthetic.main.day_fragment.recyclerView
import org.providence.calendarconnect.provider.CalendarEvent
import org.providence.calendarconnect.provider.CalendarEventsProvider
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.TimeZone

class DayFragment : Fragment() {
    private val adapter = RecyclerCellAdapter()

    private lateinit var calendarEventsProvider: CalendarEventsProvider
    private lateinit var calenderEvents: List<CalendarEvent>

    lateinit var date: Date

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.day_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Set up recycler view
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter

        // Get calendar events for [date]
        calendarEventsProvider = CalendarEventsProvider(context!!)
        calenderEvents = calendarEventsProvider.eventsForDay(date)

        // Set up data set
        adapter.cells = timeList()
    }

    private fun timeList(): List<TimeCell> {
        val cal = Calendar.getInstance()
        val sdf = SimpleDateFormat("h:mm a")
        val cells = ArrayList<TimeCell>()
        val clicks = { _: View -> Toast.makeText(context, "wow", Toast.LENGTH_SHORT).show() }

        // Create a list of appointments from 10am-8pm every 20min for [date], and check if we have cal conflicts
        cal.time = date
        cal.set(Calendar.HOUR_OF_DAY, 10)

        while (cal.get(Calendar.HOUR_OF_DAY) < 20) {

            cells.add(TimeCell(
                sdf.format(cal.timeInMillis),
                calendarEventsProvider.eventsFor(cal.timeInMillis, 20),
                clicks))
            cal.timeInMillis += 20 * 60 * 1000
        }
        // Add 8pm slot
        cells.add(TimeCell(sdf.format(cal.timeInMillis), calendarEventsProvider.eventsFor(cal.timeInMillis, 20), clicks))

        return cells
    }
}

class TimeCell(private val time: String,
               private val conflictEvents: List<CalendarEvent>,
               private val clicks: (View) -> Unit) : RecyclerCell() {

    override fun createViewHolder(parent: ViewGroup, inflater: LayoutInflater): RecyclerView.ViewHolder {
        return TimeViewHolder(inflater.inflate(R.layout.calendar_row, parent, false))
    }

    override fun bindTo(viewHolder: RecyclerView.ViewHolder) {
        val timeHolder = viewHolder as TimeViewHolder
        timeHolder.time.text = time
        timeHolder.schedule.setOnClickListener(clicks)

        val color = if (conflictEvents.isNotEmpty()) {
            ContextCompat.getColor(viewHolder.itemView.context, R.color.buttonConflict)

        } else {
            ContextCompat.getColor(viewHolder.itemView.context, R.color.colorAccent)
        }
        timeHolder.schedule.backgroundTintList = ColorStateList.valueOf(color)

        if (conflictEvents.isNotEmpty()) {
            timeHolder.conflictMessage.text = conflictEvents.map { it.title }.joinToString(", ")
        }
    }

    class TimeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val time = itemView.findViewById<TextView>(R.id.time)!!
        val schedule = itemView.findViewById<Button>(R.id.schedule)!!
        val conflictMessage = itemView.findViewById<TextView>(R.id.conflictMessage)!!
    }
}

