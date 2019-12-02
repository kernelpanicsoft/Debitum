package com.ga.kps.debitum

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import helpcodes.MENSUAL
import helpcodes.SEMANAL
import kotlinx.android.synthetic.main.activity_reminder_details.*

import model.RecordatorioPago
import room.components.viewModels.DeudaViewModel
import room.components.viewModels.RecordatorioPagoViewModel
import java.lang.Exception
import java.lang.NumberFormatException
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.ZoneId
import java.util.*
import java.time.temporal.ChronoUnit


class ReminderDetailsActivity : AppCompatActivity() {
    lateinit var debtViewModel: DeudaViewModel
    lateinit var reminderViewModel: RecordatorioPagoViewModel
    lateinit var reminderActualLive : LiveData<RecordatorioPago>
    var reminderID : Int? = null
    var simboloMoneda = "$"
    private val calendario: Calendar = Calendar.getInstance()
    private val calendarioRecordatorio: Calendar = Calendar.getInstance()
    private val sdf: DateFormat = SimpleDateFormat.getDateInstance(DateFormat.SHORT)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reminder_details)

        setSupportActionBar(toolbar)
        val ab = supportActionBar
        ab?.setDisplayHomeAsUpEnabled(true)
        title = getString(R.string.detalles_de_recordatorio)

        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        simboloMoneda = prefs.getString("moneySign","NA")

        debtViewModel = ViewModelProviders.of(this).get(DeudaViewModel::class.java)
        reminderViewModel = ViewModelProviders.of(this).get(RecordatorioPagoViewModel::class.java)

        reminderID = intent.getIntExtra("REMINDER_ID",-1)

        populateReminderUI(reminderID)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home ->{
                onBackPressed()

            }
            R.id.itemEditDelete ->{
                val builder = AlertDialog.Builder(this)
                builder.setTitle(getString(R.string.opciones_recordatorio))
                builder.setItems(R.array.editar){ _, which ->
                    when(which){
                        0 ->{

                        }
                        1 ->{
                            val builder = AlertDialog.Builder(this)
                            builder.setTitle(getString(R.string.eliminar_recordatorio))
                            builder.setMessage(getString(R.string.esta_seguro_eliminar_recordatorio))
                            builder.setPositiveButton(getString(R.string.eliminar)){
                                    _, _ ->

                                deleteReminder()
                            }

                            builder.setNegativeButton(getString(R.string.cancelar)){
                                    _,_ ->
                            }

                            val dialog = builder.create()
                            dialog.show()
                        }
                    }

                }
                val alertDialog = builder.create()
                alertDialog.show()

            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun populateDebtCard(debtID : Int?){

        debtViewModel.getDeuda(debtID!!).observe(this, Observer{ deudaActual ->

            tituloDeudaET.text = deudaActual.titulo
            fechaPagoTV.text = deudaActual.fecha_adquision
            montoPagoTV.text = getString(R.string.simboloMoneda,simboloMoneda,deudaActual.monto)
            deudaPagadaTV.text = getString(R.string.simboloMoneda,simboloMoneda,deudaActual.pagado)
            progressBar.progress = ((deudaActual.pagado * 100f) / deudaActual.monto).toInt()
            porcentajeDeudaTextView.text = getString(R.string.simboloPorcentaje, ((deudaActual.pagado * 100f) / deudaActual.monto).toInt())
            deudaRestanteTV.text = getString(R.string.simboloMoneda,simboloMoneda,(deudaActual.monto - deudaActual.pagado))

        })
    }

    private fun populateReminderUI(reminderID : Int?){
        reminderActualLive = reminderViewModel.getRecordatorio(reminderID!!)

        reminderActualLive.observe(this, Observer {
            notaRecordatioTV.text = it.nota
            montoRecodatorioTV.text = getString(R.string.simboloMoneda,simboloMoneda,it.monto)

            when(it.tipo){
                MENSUAL ->{
                    try{
                        if(it.fecha.equals(getString(R.string.ultimo_dia_mes)) || it.fecha?.toInt() != 0){
                            //Mostramos el día de pago
                            periodoPagoTV.text = getString(R.string.mensual)

                            //variable auxiliar para almacenar el dia del mes
                            var reminderDayOfMonth = 0

                            //Calculamos el proximo pago
                            //calendarioRecordatorio.set(Calendar.DAY_OF_MONTH,31)
                            val maxMonthDay = calendario.getActualMaximum(Calendar.DAY_OF_MONTH)

                            if (it.fecha.equals(getString(R.string.ultimo_dia_mes)) || it.fecha?.toInt() == maxMonthDay) {
                                reminderDayOfMonth = maxMonthDay
                            } else {
                                reminderDayOfMonth = it.fecha?.toInt()!!
                            }

                            calendarioRecordatorio.set(Calendar.DAY_OF_MONTH, reminderDayOfMonth)

                            when {
                                calendarioRecordatorio.time.compareTo(calendario.time) == 0 -> fechaPagoRecordatorioTV.text =
                                    getString(R.string.hoy)
                                calendarioRecordatorio.time.compareTo(calendario.time) < 0 -> {
                                    calendarioRecordatorio.add(Calendar.MONTH, 1)
                                    fechaPagoRecordatorioTV.text =
                                        sdf.format(calendarioRecordatorio.time)
                                    cuentaDiasPagoTV.text =
                                        (calendarioRecordatorio.get(Calendar.DAY_OF_YEAR) - calendario.get(
                                            Calendar.DAY_OF_YEAR
                                        )).toString()
                                }
                                calendarioRecordatorio.time.compareTo(calendario.time) > 0 -> {
                                    fechaPagoRecordatorioTV.text =
                                        sdf.format(calendarioRecordatorio.time)
                                    cuentaDiasPagoTV.text =
                                        (calendarioRecordatorio.get(Calendar.DAY_OF_YEAR) - calendario.get(
                                            Calendar.DAY_OF_YEAR
                                        )).toString()
                                }

                            }
                        }
                    }catch (e: Exception){

                    }
                }
                SEMANAL ->{
                    periodoPagoTV.text = getString(R.string.semanal)

                    val dayList = resources.getStringArray(R.array.daysOfWeek)

                    when(it.fecha){
                        Calendar.SUNDAY.toString()-> {
                            fechaPagoRecordatorioTV.text = dayList[0]
                            calendarioRecordatorio.set(Calendar.DAY_OF_WEEK,Calendar.SUNDAY)
                            cuentaDiasPagoTV.text = countDaysBetweenDates(calendarioRecordatorio,calendario).toString()


                        }
                        Calendar.MONDAY.toString() -> {
                            fechaPagoRecordatorioTV.text = dayList[1]
                            calendarioRecordatorio.set(Calendar.DAY_OF_WEEK,Calendar.MONDAY)

                        }
                        Calendar.TUESDAY.toString() -> {
                            fechaPagoRecordatorioTV.text = dayList[2]

                            calendarioRecordatorio.set(Calendar.DAY_OF_WEEK,Calendar.TUESDAY)
                        }
                        Calendar.WEDNESDAY.toString() -> {
                            fechaPagoRecordatorioTV.text = dayList[3]
                            calendarioRecordatorio.set(Calendar.DAY_OF_WEEK,Calendar.WEDNESDAY)
                        }
                        Calendar.THURSDAY.toString() -> {
                            fechaPagoRecordatorioTV.text = dayList[4]
                            calendarioRecordatorio.set(Calendar.DAY_OF_WEEK,Calendar.THURSDAY)
                        }
                        Calendar.FRIDAY.toString() -> {
                            fechaPagoRecordatorioTV.text = dayList[5]
                            calendarioRecordatorio.set(Calendar.DAY_OF_WEEK,Calendar.FRIDAY)
                        }
                        Calendar.SATURDAY.toString() -> {
                            fechaPagoRecordatorioTV.text = dayList[6]
                            calendarioRecordatorio.set(Calendar.DAY_OF_WEEK,Calendar.SATURDAY)
                        }
                    }

                    cuentaDiasPagoTV.text = countDaysBetweenDates(calendarioRecordatorio,calendario).toString()
                }
            }


            populateDebtCard(it.deudaID)

        })
    }

    private fun countDaysBetweenDates(reminderCalendar: Calendar, calendar: Calendar) : Int{
        when {
            reminderCalendar.get(Calendar.DAY_OF_WEEK) == calendar.get(Calendar.DAY_OF_WEEK) -> {
                fechaPagoRecordatorioTV.text = getString(R.string.hoy)
                return 0
            }
            reminderCalendar.get(Calendar.DAY_OF_WEEK) > calendar.get(Calendar.DAY_OF_WEEK) -> return reminderCalendar.get(Calendar.DAY_OF_WEEK) - calendar.get(Calendar.DAY_OF_WEEK)
            reminderCalendar.get(Calendar.DAY_OF_WEEK) < calendar.get(Calendar.DAY_OF_WEEK) -> {
                Log.d("DIAS: " ,""+  calendar.get(Calendar.DAY_OF_WEEK) + " # " + reminderCalendar.get(Calendar.DAY_OF_WEEK))
                var days =  reminderCalendar.get(Calendar.DAY_OF_WEEK) - calendar.get(Calendar.DAY_OF_WEEK)

                return if(days < 1){
                    days + 7
                }else{
                    days
                }
            }
            else -> return -1
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_single_edit, menu)
        return super.onCreateOptionsMenu(menu)
    }

    private fun deleteReminder(){
        if(reminderActualLive.hasActiveObservers()){
            reminderActualLive.removeObservers(this)
            val reminderAux = RecordatorioPago(reminderID!!)
            reminderViewModel.delete(reminderAux)
        }
        finish()
    }


}
