package org.providence.calendarconnect

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.confirmation_fragment.toolbar

class CalendarActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.calendar_activity)

        // Add Calendar Fragment
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, CalendarFragment())
            .commit()
    }

    fun showConfirmation() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, ConfirmationFragment())
            .setCustomAnimations(R.anim.fade_out, R.anim.fade_in)
            .addToBackStack(null)
            .commit()
    }
}

class ConfirmationFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.confirmation_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolbar.title = "Confirmation"
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp)
        toolbar.setNavigationOnClickListener { requireActivity().onBackPressed() }
    }
}
