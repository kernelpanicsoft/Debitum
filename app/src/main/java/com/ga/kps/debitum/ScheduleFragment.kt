package com.ga.kps.debitum

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import androidx.fragment.app.Fragment
import java.util.*

class ScheduleFragment : Fragment() {
    lateinit var calendario : CalendarView
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_schedule, container, false)
        calendario = v.findViewById(R.id.calendarView)

        val currentMonth = Calendar.getInstance()

        return v
    }
}