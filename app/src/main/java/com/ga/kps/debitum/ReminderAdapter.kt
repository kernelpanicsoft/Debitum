package com.ga.kps.debitum

import android.content.Context
import android.preference.PreferenceManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import helpcodes.MENSUAL
import helpcodes.SEMANAL
import model.JoinDeudaRecordatorio
import model.RecordatorioPago
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class ReminderAdapter (val context: Context) : ListAdapter<JoinDeudaRecordatorio, ReminderAdapter.ViewHolder>(DIFF_CALLBACK()), View.OnClickListener {
    private var listener : View.OnClickListener? = null
    private val calendario: Calendar = Calendar.getInstance()
    private val calendarioRecordatorio: Calendar = Calendar.getInstance()
    private val sdf: DateFormat = SimpleDateFormat.getDateInstance(DateFormat.SHORT)
    class DIFF_CALLBACK : DiffUtil.ItemCallback<JoinDeudaRecordatorio>(){
        override fun areItemsTheSame(
            oldItem: JoinDeudaRecordatorio,
            newItem: JoinDeudaRecordatorio
        ): Boolean {
            return oldItem.recordatorioID == newItem.recordatorioID
        }

        override fun areContentsTheSame(
            oldItem: JoinDeudaRecordatorio,
            newItem: JoinDeudaRecordatorio
        ): Boolean {
            return oldItem.tituloDeuda == newItem.tituloDeuda && oldItem.fechaRecordatorio.equals(newItem.fechaRecordatorio)
        }
    }

    inner class ViewHolder(v: View): RecyclerView.ViewHolder(v){
        val tituloRecordatorio = v.findViewById<TextView>(R.id.TituloRecordatorioTV)
        val deudaVinculada = v.findViewById<TextView>(R.id.DeudaAsociadaTV)
        val fechaRecordatorio = v.findViewById<TextView>(R.id.FechaRecordatorioTV)
        val montoRecordatorio = v.findViewById<TextView>(R.id.MontoRecordatorioTV)
        val diasRestantes = v.findViewById<TextView>(R.id.diasRetantesTV)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_payment_reminder, parent, false)
        v.setOnClickListener(this)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val it = getItem(position)

        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        val simboloMoneda = prefs.getString("moneySign","NA")
        holder.tituloRecordatorio.text = it.notaRecordatorio
        holder.deudaVinculada.text = it.tituloDeuda.toString()
        holder.fechaRecordatorio.text = it.fechaRecordatorio
        holder.montoRecordatorio.text = context.getString(R.string.simboloMoneda,simboloMoneda,it.montoRecordatorio)//simboloMoneda + " " + it.montoRecordatorio.toString()



        when(it.tipoRecordatorio){
            MENSUAL ->{
                try{
                    if(it.fechaRecordatorio.equals(context.getString(R.string.ultimo_dia_mes)) || it.fechaRecordatorio.toInt() != 0){
                        //Mostramos el día de pago

                        //variable auxiliar para almacenar el dia del mes
                        var reminderDayOfMonth = 0

                        //Calculamos el proximo pago

                        val maxMonthDay = calendario.getActualMaximum(Calendar.DAY_OF_MONTH)

                        if (it.fechaRecordatorio.equals(context.getString(R.string.ultimo_dia_mes)) || it.fechaRecordatorio.toInt() == maxMonthDay) {
                            reminderDayOfMonth = maxMonthDay
                        } else {
                            reminderDayOfMonth = it.fechaRecordatorio.toInt()!!
                        }

                        calendarioRecordatorio.set(Calendar.DAY_OF_MONTH, reminderDayOfMonth)

                        when {
                            calendarioRecordatorio.time.compareTo(calendario.time) == 0 -> holder.fechaRecordatorio.text =
                                context.getString(R.string.hoy)
                            calendarioRecordatorio.time.compareTo(calendario.time) < 0 -> {
                                calendarioRecordatorio.add(Calendar.MONTH, 1)
                                holder.fechaRecordatorio.text =
                                    sdf.format(calendarioRecordatorio.time)
                                holder.diasRestantes.text =
                                    (calendarioRecordatorio.get(Calendar.DAY_OF_YEAR) - calendario.get(
                                        Calendar.DAY_OF_YEAR
                                    )).toString()
                            }
                            calendarioRecordatorio.time.compareTo(calendario.time) > 0 -> {
                                holder.fechaRecordatorio.text =
                                    sdf.format(calendarioRecordatorio.time)
                                holder.diasRestantes.text =
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
                val dayList = context.resources.getStringArray(R.array.daysOfWeek)

                when(it.fechaRecordatorio){
                    Calendar.SUNDAY.toString()-> {
                        holder.fechaRecordatorio.text = dayList[0]
                        calendarioRecordatorio.set(Calendar.DAY_OF_WEEK,Calendar.SUNDAY)
                        holder.diasRestantes.text = countDaysBetweenDates(calendarioRecordatorio,calendario).toString()

                    }
                    Calendar.MONDAY.toString() -> {
                        holder.fechaRecordatorio.text = dayList[1]
                        calendarioRecordatorio.set(Calendar.DAY_OF_WEEK,Calendar.MONDAY)

                    }
                    Calendar.TUESDAY.toString() -> {
                        holder.fechaRecordatorio.text = dayList[2]
                        calendarioRecordatorio.set(Calendar.DAY_OF_WEEK,Calendar.TUESDAY)
                    }
                    Calendar.WEDNESDAY.toString() -> {
                        holder.fechaRecordatorio.text = dayList[3]
                        calendarioRecordatorio.set(Calendar.DAY_OF_WEEK,Calendar.WEDNESDAY)
                    }
                    Calendar.THURSDAY.toString() -> {
                        holder.fechaRecordatorio.text = dayList[4]
                        calendarioRecordatorio.set(Calendar.DAY_OF_WEEK,Calendar.THURSDAY)
                    }
                    Calendar.FRIDAY.toString() -> {
                        holder.fechaRecordatorio.text = dayList[5]
                        calendarioRecordatorio.set(Calendar.DAY_OF_WEEK,Calendar.FRIDAY)
                    }
                    Calendar.SATURDAY.toString() -> {
                        holder.fechaRecordatorio.text = dayList[6]
                        calendarioRecordatorio.set(Calendar.DAY_OF_WEEK,Calendar.SATURDAY)
                    }
                }

                holder.diasRestantes.text = countDaysBetweenDates(calendarioRecordatorio,calendario).toString()
            }
        }



    }

    fun setOnClickListener(listener: View.OnClickListener){
        this.listener = listener
    }

    override fun onClick(v: View?) {
        listener!!.onClick(v)
    }

    fun getReminderAt(position: Int): JoinDeudaRecordatorio{
        return getItem(position)
    }

    private fun countDaysBetweenDates(reminderCalendar: Calendar, calendar: Calendar) : Int{
        when {
            reminderCalendar.get(Calendar.DAY_OF_WEEK) == calendar.get(Calendar.DAY_OF_WEEK) -> {
                return 0
            }
            reminderCalendar.get(Calendar.DAY_OF_WEEK) > calendar.get(Calendar.DAY_OF_WEEK) -> return reminderCalendar.get(Calendar.DAY_OF_WEEK) - calendar.get(Calendar.DAY_OF_WEEK)
            reminderCalendar.get(Calendar.DAY_OF_WEEK) < calendar.get(Calendar.DAY_OF_WEEK) -> {
                val days =  reminderCalendar.get(Calendar.DAY_OF_WEEK) - calendar.get(Calendar.DAY_OF_WEEK)

                return if(days < 1){
                    days + 7
                }else{
                    days
                }
            }
            else -> return -1
        }
    }
}