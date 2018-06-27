package org.providence.calendarconnect

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.tomatron.RecyclerCell
import com.tomatron.RecyclerCellAdapter
import kotlinx.android.synthetic.main.day_fragment.list

class DayFragment : Fragment() {
    private val adapter = RecyclerCellAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.day_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Set up recycler view
        list.layoutManager = LinearLayoutManager(context)
        list.adapter = adapter

        // Set up data set
        adapter.cells = listOf(TimeCell(), TimeCell(), TimeCell())
    }
}

class TimeCell : RecyclerCell() {
    override fun createViewHolder(parent: ViewGroup, inflater: LayoutInflater): RecyclerView.ViewHolder {
        return TimeViewHolder(inflater.inflate(R.layout.calendar_row, parent, false))
    }

    override fun bindTo(viewHolder: RecyclerView.ViewHolder) {
        val timeHolder = viewHolder as TimeViewHolder
        timeHolder.time.text = "2:40 PM"
    }

    class TimeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val time = itemView.findViewById<TextView>(R.id.time)!!
    }
}

