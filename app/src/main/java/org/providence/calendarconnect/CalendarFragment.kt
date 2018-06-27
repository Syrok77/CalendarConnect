package org.providence.calendarconnect

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.calendar_fragment.tabs
import kotlinx.android.synthetic.main.calendar_fragment.toolbar
import kotlinx.android.synthetic.main.calendar_fragment.viewPager
import org.providence.calendarconnect.extensions.startOfDay
import java.util.Calendar

class CalendarFragment : Fragment() {
     private lateinit var adapter: CalendarAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = CalendarAdapter(childFragmentManager)
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.calendar_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbar.title = "Calendar Connect"

        viewPager.adapter = adapter
        tabs.setupWithViewPager(viewPager)
    }
}

class CalendarAdapter(fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager) {

    private val cal = Calendar.getInstance()

    init {
        // Set the calendar to the start of the day
        cal.startOfDay()
    }

    override fun getItem(index: Int): Fragment {
        val frag = DayFragment()

        // Set date for fragment based on index
        frag.date = if (index == 0) {
            // Today
            cal.time
        } else {
            // Tomorrow
            cal.add(Calendar.DAY_OF_YEAR, 1)
            cal.time
        }
        return frag
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return if (position == 0) "Today" else "Tomorrow"
    }

    override fun getCount() = 2
}