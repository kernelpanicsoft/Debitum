package com.ga.kps.debitum

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.CalendarView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import room.components.viewModels.RecordatorioPagoViewModel
import java.util.*

class ScheduleFragment : Fragment() {

    lateinit var RV : RecyclerView
    lateinit var recordatorioViewModel : RecordatorioPagoViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_schedule, container, false)
        val fab = v.findViewById<FloatingActionButton>(R.id.addReminderFAB)

        RV = v.findViewById(R.id.RecViewRecordatorios)
        RV.setHasFixedSize(true)

        val mLayoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.VERTICAL,
            true
        )
        mLayoutManager.stackFromEnd = true
        RV.layoutManager = mLayoutManager

        val adapter = ReminderAdapter(context!!)
        recordatorioViewModel = ViewModelProviders.of(this).get(RecordatorioPagoViewModel::class.java)
        recordatorioViewModel.getAllRecordatorios().observe(this, androidx.lifecycle.Observer {
            adapter.submitList(it)

        })

        adapter.setOnClickListener(View.OnClickListener {

        })

        fab.setOnClickListener {
            val nav = Intent(context, AddPaymentReminderActivity::class.java)
            startActivity(nav)
        }

        RV.adapter = adapter
        return v
    }


}