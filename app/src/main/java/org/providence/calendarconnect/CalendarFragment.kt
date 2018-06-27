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

class CalendarFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.calendar_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbar.title = "Calendar Connect"

        viewPager.adapter = CalendarAdapter(fragmentManager!!)
        tabs.setupWithViewPager(viewPager)
    }
}

class CalendarAdapter(fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager) {
    override fun getItem(index: Int): Fragment {
        return DayFragment()
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return if (position == 0) "Today" else "Tomorrow"
    }

    override fun getCount() = 2
}