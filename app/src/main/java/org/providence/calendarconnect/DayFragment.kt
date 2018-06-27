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

class DayFragment : Fragment() {
    private val adapter = RecyclerCellAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.day_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Set up recycler view
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter

        val clicks = { _: View -> Toast.makeText(context, "wow", Toast.LENGTH_SHORT).show() }

        // Set up data set
        adapter.cells = listOf(
            TimeCell("2:00 PM", false, clicks),
            TimeCell("2:20 PM", true, clicks),
            TimeCell("2:40 PM", false, clicks)
        )
    }
}

class TimeCell(private val time: String,
               private val isConflict: Boolean,
               private val clicks: (View) -> Unit
) : RecyclerCell() {
    override fun createViewHolder(parent: ViewGroup, inflater: LayoutInflater): RecyclerView.ViewHolder {
        return TimeViewHolder(inflater.inflate(R.layout.calendar_row, parent, false))
    }

    override fun bindTo(viewHolder: RecyclerView.ViewHolder) {
        val timeHolder = viewHolder as TimeViewHolder
        timeHolder.time.text = time
        timeHolder.schedule.setOnClickListener(clicks)

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

