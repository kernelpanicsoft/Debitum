package com.ga.kps.debitum

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import model.JoinDeudaRecordatorio
import model.RecordatorioPago

class ReminderAdapter (val context: Context) : ListAdapter<JoinDeudaRecordatorio, ReminderAdapter.ViewHolder>(DIFF_CALLBACK()), View.OnClickListener {
    private var listener : View.OnClickListener? = null

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
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_payment_reminder, parent, false)
        v.setOnClickListener(this)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val recordatorioPago = getItem(position)
        val simboloMoneda = "$"
        holder.tituloRecordatorio.text = recordatorioPago.notaRecordatorio
        holder.deudaVinculada.text = recordatorioPago.tituloDeuda.toString()
        holder.fechaRecordatorio.text = recordatorioPago.fechaRecordatorio
        holder.montoRecordatorio.text = simboloMoneda + " " + recordatorioPago.montoRecordatorio.toString()

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
}