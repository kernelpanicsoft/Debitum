package com.ga.kps.debitum

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import model.RecordatorioPago

class ReminderAdapter (val context: Context) : ListAdapter<RecordatorioPago, ReminderAdapter.ViewHolder>(DIFF_CALLBACK()), View.OnClickListener {
    private var listener : View.OnClickListener? = null

    class DIFF_CALLBACK : DiffUtil.ItemCallback<RecordatorioPago>(){
        override fun areItemsTheSame(
            oldItem: RecordatorioPago,
            newItem: RecordatorioPago
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: RecordatorioPago,
            newItem: RecordatorioPago
        ): Boolean {
            return oldItem.monto == newItem.monto && oldItem.fecha.equals(newItem.fecha)
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
        holder.tituloRecordatorio.text = recordatorioPago.nota
        holder.deudaVinculada.text = recordatorioPago.deudaID.toString()
        holder.fechaRecordatorio.text = recordatorioPago.fecha
        holder.montoRecordatorio.text = simboloMoneda + " " + recordatorioPago.monto.toString()

    }

    fun setOnClickListener(listener: View.OnClickListener){
        this.listener = listener
    }

    override fun onClick(v: View?) {
        listener!!.onClick(v)
    }
}