package org.providence.calendarconnect

import android.content.Intent
import android.content.res.ColorStateList
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.tomatron.RecyclerCell
import com.tomatron.RecyclerCellAdapter
import kotlinx.android.synthetic.main.day_fragment.recyclerView
import org.providence.calendarconnect.provider.CalendarEvent
import org.providence.calendarconnect.provider.CalendarEventsProvider
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

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
        val clicks: (Boolean, Long) -> Unit = { isConflict, time ->
            val activity = requireActivity() as CalendarActivity

            if (isConflict) {
                AlertDialog.Builder(requireContext())
                    .setTitle("")
                    .setMessage("This appointment conflicts with an event in your Calendar. Are you sure you want to schedule this appointment?")
                    .setPositiveButton("Schedule") { _, _ ->
                        activity.showConfirmation()
                    }
                    .setNegativeButton("View Calendar") { _, _ ->
                        val intent = Intent(Intent.ACTION_VIEW)
                            .setData(Uri.parse("content://com.android.calendar/time/$time"))
                        activity.startActivity(intent)
                    }
                    .show()
            } else {
                activity.showConfirmation()
            }
        }

        val cal = Calendar.getInstance()
        val cells = ArrayList<TimeCell>()

        // Create a list of appointments from 10am-8pm every 20min for [date], and check if we have cal conflicts
        cal.time = date
        cal.set(Calendar.HOUR_OF_DAY, 10)

        while (cal.get(Calendar.HOUR_OF_DAY) < 20) {
            cells.add(TimeCell(cal.timeInMillis, calendarEventsProvider.hasEventsFor(cal.timeInMillis, 20), clicks))
            cal.timeInMillis += 20 * 60 * 1000
        }

        // add 8pm slot
        cells.add(TimeCell(cal.timeInMillis, calendarEventsProvider.hasEventsFor(cal.timeInMillis, 20), clicks))

        return cells
    }
}

class TimeCell(private val time: Long,
               private val isConflict: Boolean,
               private val clicks: (Boolean, Long) -> Unit
) : RecyclerCell() {
    private val sdf = SimpleDateFormat("h:mm a", Locale.US)

    override fun createViewHolder(parent: ViewGroup, inflater: LayoutInflater): RecyclerView.ViewHolder {
        return TimeViewHolder(inflater.inflate(R.layout.calendar_row, parent, false))
    }

    override fun bindTo(viewHolder: RecyclerView.ViewHolder) {
        val timeHolder = viewHolder as TimeViewHolder
        timeHolder.time.text = sdf.format(time)
        timeHolder.schedule.setOnClickListener { clicks.invoke(isConflict, time) }

        val color = if (isConflict) {
            ContextCompat.getColor(viewHolder.itemView.context, R.color.buttonConflict)

        } else {
            ContextCompat.getColor(viewHolder.itemView.context, R.color.colorAccent)
        }
        timeHolder.schedule.backgroundTintList = ColorStateList.valueOf(color)

        timeHolder.conflictMessage.visibility = if (isConflict) View.VISIBLE else View.GONE
    }

    class TimeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val time = itemView.findViewById<TextView>(R.id.time)!!
        val schedule = itemView.findViewById<Button>(R.id.schedule)!!
        val conflictMessage = itemView.findViewById<TextView>(R.id.conflictMessage)!!
    }
}

