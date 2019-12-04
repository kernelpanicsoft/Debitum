package com.ga.kps.debitum

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar
import helpcodes.ELEGIR_DEUDA
import helpcodes.MENSUAL
import helpcodes.SEMANAL
import helpers.CalendarHelper
import kotlinx.android.synthetic.main.activity_add_payment_reminder.*
import kotlinx.android.synthetic.main.activity_add_payment_reminder.fechaPagoTV
import kotlinx.android.synthetic.main.activity_add_payment_reminder.tituloDeudaET
import kotlinx.android.synthetic.main.activity_add_payment_reminder.toolbar
import model.Deuda
import model.RecordatorioPago
import room.components.viewModels.DeudaViewModel
import room.components.viewModels.RecordatorioPagoViewModel
import java.lang.Exception
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.DayOfWeek
import java.util.*


class AddPaymentReminderActivity : AppCompatActivity() {

    lateinit var debtViewModel: DeudaViewModel
    lateinit var reminderViewModel: RecordatorioPagoViewModel
    lateinit var  paymentReminder: RecordatorioPago
    var reminderID : Int = 0

    var fecha = ""
    private val calendario: Calendar = Calendar.getInstance()
    private val sdf: DateFormat = SimpleDateFormat.getDateInstance(DateFormat.SHORT)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_payment_reminder)



        setSupportActionBar(toolbar)
        val ab  = supportActionBar
        ab?.setDisplayHomeAsUpEnabled(true)
        title = getString(R.string.anadir_recordatorio_de_pago)

        deudaAsociadaButton.setOnClickListener {
            val nav = Intent(this, SelectDebtActivity::class.java)
            startActivityForResult(nav,helpcodes.ELEGIR_DEUDA)
        }

        val adapterWeekly = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, this.resources.getStringArray(R.array.daysOfWeek))
        val adapterMonthly = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, this.resources.getStringArray(R.array.daysOfMonth))

        debtViewModel = ViewModelProviders.of(this).get(DeudaViewModel::class.java)
        reminderViewModel = ViewModelProviders.of(this).get(RecordatorioPagoViewModel::class.java)


        reminderID = intent.getIntExtra("ID", -1)
        if(reminderID != -1) {
            populateFieldsForEdit()
        }


        paymentReminder = RecordatorioPago(0)
        paymentReminder.tipo = MENSUAL
        saveReminderFAB.setOnClickListener {
            paymentReminder.nota = notaRecordatorioET.text.toString()
            try{
                paymentReminder.monto =  montoRecordatorioET.text.toString().toFloat()
                paymentReminder.fecha = periodicidiadSpinner.selectedItem.toString()
                if(periodicidiadSpinner.adapter.equals(adapterWeekly)){
                    paymentReminder.montoMensual = calculateMonthlyPayment(periodicidiadSpinner.selectedItem.toString(),montoRecordatorioET.text.toString().toFloat())
                    paymentReminder.fecha = setDayOfWeekForReminder(periodicidiadSpinner.selectedItem.toString()).toString()

                }else{
                    paymentReminder.montoMensual = 0f
                }

                if(paymentReminder.deudaID == null) {
                    Snackbar.make(it,getString(R.string.es_necesario_asociar_deuda_recordatori), Snackbar.LENGTH_LONG).show()
                }else{
                    reminderViewModel.insert(paymentReminder)
                    finish()
                }
            }catch (e: Exception){
                Snackbar.make(it,getString(R.string.es_necesario_indicar_monto_recordatorio), Snackbar.LENGTH_LONG).show()

            }

        }

        periodicidiadSpinner.adapter = adapterMonthly

        PeriodicidadGroup.setOnCheckedChangeListener { group, checkedId ->
            when(checkedId){
                R.id.mensualRB ->{
                    periodicidiadSpinner.adapter = adapterMonthly
                    paymentReminder.tipo = MENSUAL
                }
                R.id.semanalRB ->{
                    periodicidiadSpinner.adapter = adapterWeekly
                    paymentReminder.tipo = SEMANAL
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home ->{
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == ELEGIR_DEUDA){
            if(resultCode == Activity.RESULT_OK){
                val debtId = data?.getIntExtra("debtID",-1)
                populateDebtCard(debtId)
                paymentReminder.deudaID = debtId
            }
        }

        super.onActivityResult(requestCode, resultCode, data)
    }



    private fun populateDebtCard(debtID : Int?){
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        val simboloMoneda = prefs.getString("moneySign","NA")
        cardView.visibility = View.VISIBLE

        debtViewModel.getDeuda(debtID!!).observe(this, Observer{ deudaActual ->

            tituloDeudaET.text = deudaActual.titulo
            fechaPagoTV.text = deudaActual.fecha_adquision
            montoPagoTV.text = getString(R.string.simboloMoneda,simboloMoneda,deudaActual.monto)
            deudaPagadaTV.text = getString(R.string.simboloMoneda,simboloMoneda,deudaActual.pagado)
            progressBar.progress = ((deudaActual.pagado * 100f) / deudaActual.monto).toInt()
            porcentajeDeudaTextView.text = getString(R.string.simboloPorcentaje, ((deudaActual.pagado * 100f) / deudaActual.monto).toInt())
            deudaRestanteTV.text =  getString(R.string.simboloMoneda,simboloMoneda,(deudaActual.monto - deudaActual.pagado))

        })
    }

    private fun calculateMonthlyPayment( dayOfWeek: String, amount: Float) : Float{
        val calendarHelper = CalendarHelper()

        val dayList = resources.getStringArray(R.array.daysOfWeek)
        var amountOfMonth = 0f

        when(dayOfWeek){
            dayList[0] -> {
                amountOfMonth = amount * calendarHelper.sundaysInMonth
            }
            dayList[1] -> {
                amountOfMonth = amount * calendarHelper.mondaysInMonth
            }
            dayList[2] -> {
                amountOfMonth = amount * calendarHelper.tuesdaysInMonth
            }
            dayList[3] -> {
                amountOfMonth = amount * calendarHelper.wednesdaysInMonth
            }
            dayList[4] -> {
                amountOfMonth = amount * calendarHelper.thursdaysInMonth
            }
            dayList[5] -> {
                amountOfMonth = amount * calendarHelper.fridaysInMonth
            }
            dayList[6] -> {
                amountOfMonth = amount * calendarHelper.saturdaysInMonth
            }
        }
        return amountOfMonth
    }

    private fun setDayOfWeekForReminder(day: String) : Int{
        val dayList = resources.getStringArray(R.array.daysOfWeek)
        return when(day){
            dayList[0] -> Calendar.SUNDAY
            dayList[1] -> Calendar.MONDAY
            dayList[2] -> Calendar.TUESDAY
            dayList[3] -> Calendar.WEDNESDAY
            dayList[4] -> Calendar.THURSDAY
            dayList[5] -> Calendar.FRIDAY
            dayList[6] -> Calendar.SATURDAY
            else -> Calendar.SUNDAY
        }
    }

    private fun populateFieldsForEdit(){
        reminderViewModel.getRecordatorio(reminderID).observe(this, Observer {
            notaRecordatorioET.setText(it?.nota, TextView.BufferType.EDITABLE)
            montoRecordatorioET.setText(it?.monto.toString(), TextView.BufferType.EDITABLE)

            when (it.tipo) {
                MENSUAL -> {
                    PeriodicidadGroup.check(R.id.mensualRB)

                }
                SEMANAL -> {
                    PeriodicidadGroup.check(R.id.semanalRB)
                }
            }

            populateDebtCard(it.deudaID)


        })
    }

}
