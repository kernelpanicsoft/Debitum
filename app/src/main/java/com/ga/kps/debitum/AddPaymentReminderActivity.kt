package com.ga.kps.debitum

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import helpcodes.ELEGIR_DEUDA
import kotlinx.android.synthetic.main.activity_add_payment_reminder.*
import kotlinx.android.synthetic.main.activity_add_payment_reminder.fechaPagoTV
import kotlinx.android.synthetic.main.activity_add_payment_reminder.tituloDeudaET
import kotlinx.android.synthetic.main.activity_add_payment_reminder.toolbar
import model.Deuda
import model.RecordatorioPago
import room.components.viewModels.DeudaViewModel
import room.components.viewModels.RecordatorioPagoViewModel
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


class AddPaymentReminderActivity : AppCompatActivity() {

    lateinit var debtViewModel: DeudaViewModel
    lateinit var reminderViewModel: RecordatorioPagoViewModel
    lateinit var  paymentReminder: RecordatorioPago

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

        debtViewModel = ViewModelProviders.of(this).get(DeudaViewModel::class.java)
        reminderViewModel = ViewModelProviders.of(this).get(RecordatorioPagoViewModel::class.java)

        paymentReminder = RecordatorioPago(0)
        saveReminderFAB.setOnClickListener {
            paymentReminder.nota = notaRecordatorioET.text.toString()
            paymentReminder.monto =  montoRecordatorioET.text.toString().toFloat()
            paymentReminder.fecha = periodicidiadSpinner.selectedItem.toString()

            reminderViewModel.insert(paymentReminder)
            finish()
        }



        val adapterWeekly = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, this.resources.getStringArray(R.array.daysOfWeek))
        val adapterMonthly = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, this.resources.getStringArray(R.array.daysOfMonth))
        periodicidiadSpinner.adapter = adapterMonthly

        PeriodicidadGroup.setOnCheckedChangeListener { group, checkedId ->
            when(checkedId){
                R.id.mensualRB ->{
                    periodicidiadSpinner.adapter = adapterMonthly
                }
                R.id.semanalRB ->{
                    periodicidiadSpinner.adapter = adapterWeekly
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
        val simboloMoneda = "$"
        cardView.visibility = View.VISIBLE

        debtViewModel.getDeuda(debtID!!).observe(this, Observer{ deudaActual ->

            tituloDeudaET.text = deudaActual.titulo
            fechaPagoTV.text = deudaActual.fecha_adquision
            montoPagoTV.text = getString(R.string.simboloMoneda,simboloMoneda,deudaActual.monto)
            deudaPagadaTV.text = getString(R.string.simboloMoneda,simboloMoneda,deudaActual.pagado)
            progressBar.progress = ((deudaActual.pagado * 100f) / deudaActual.monto).toInt()
            porcentajeDeudaTextView.text = getString(R.string.simboloPorcentaje, ((deudaActual.pagado * 100f) / deudaActual.monto).toInt())

        })
    }




}
