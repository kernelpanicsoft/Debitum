package com.ga.kps.debitum

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import android.content.Context.JOB_SCHEDULER_SERVICE
import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import helpers.CalendarHelper
import kotlinx.android.synthetic.main.fragment_schedule.*
import notifications.AlarmHelper
import notifications.NotificationsManager
import room.components.viewModels.RecordatorioPagoViewModel
import schedulers.ReminderNotificationsJobService

class ScheduleFragment : Fragment() {

    lateinit var RV : RecyclerView
    lateinit var recordatorioViewModel : RecordatorioPagoViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_schedule, container, false)
        //val fab = v.findViewById<FloatingActionButton>(R.id.addReminderFAB)

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
        recordatorioViewModel.getRecordatoriosPagoDeuda().observe(this, androidx.lifecycle.Observer {
            adapter.submitList(it)

        })

        recordatorioViewModel.getSumaRecordatorioPagos().observe(this, androidx.lifecycle.Observer {
            val prefs = PreferenceManager.getDefaultSharedPreferences(context)
            val simboloMoneda = prefs.getString("moneySign","$")
            if(it == null){
                cantidadGastoMensualTV.text =  getString(R.string.simboloMoneda, simboloMoneda, 0f)
            }else{
                cantidadGastoMensualTV.text =  getString(R.string.simboloMoneda, simboloMoneda, it)
            }
        })

        adapter.setOnClickListener(View.OnClickListener {
            val nav = Intent(context, ReminderDetailsActivity::class.java)
            val selectReminder = adapter.getReminderAt(RV.getChildAdapterPosition(it))
            nav.putExtra("REMINDER_ID",selectReminder.recordatorioID)
            startActivity(nav)
        })


        RV.adapter = adapter
        return v
    }


    fun scheduleJob(){
        val componentName = ComponentName(context, ReminderNotificationsJobService::class.java)
        val info = JobInfo.Builder(123,componentName)
            .setRequiresCharging(true)
            .setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)
            .setPersisted(true)
            .setPeriodic(15 * 60 * 1000)
            .build()

        val scheduler = context?.getSystemService(JOB_SCHEDULER_SERVICE) as JobScheduler
        val resultCode = scheduler.schedule(info)
        if(resultCode == JobScheduler.RESULT_SUCCESS){
            Log.d("EXITO", "Job Schedule")

        }else{
            Log.d("EXITO", "Job scheduling failed")
        }
    }

    fun cancelJob(){
        val scheduler = context?.getSystemService(JOB_SCHEDULER_SERVICE) as JobScheduler
        scheduler.cancel(123)
        Log.d("EXITO", "Job cancelled")
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_add_payment_reminder, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.itemAddPaymentReminder ->{
                val nav = Intent(context, AddPaymentReminderActivity::class.java)
                startActivity(nav)
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}