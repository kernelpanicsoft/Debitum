package com.ga.kps.debitum

import android.content.Context
import android.support.v7.recyclerview.extensions.ListAdapter
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import model.Pago

class DebtPaymentsAdapter(val context: Context?) : ListAdapter<Pago, DebtPaymentsAdapter.ViewHolder>(DIFF_CALLBACK()), View.OnClickListener  {
    private var listener: View.OnClickListener? = null

    class DIFF_CALLBACK: DiffUtil.ItemCallback<Pago>(){
        override fun areItemsTheSame(oldItem: Pago, newItem: Pago): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Pago, newItem: Pago): Boolean {
            return oldItem.fecha == newItem.fecha && oldItem.monto == newItem.monto
        }
    }

    inner class ViewHolder(v: View): RecyclerView.ViewHolder(v){
        val montoPago = v.findViewById<TextView>(R.id.montoPagoTV)
        val fechaPago = v.findViewById<TextView>(R.id.fechaPagoTV)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_payment_details, parent, false)

        v.setOnClickListener(this)

        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val pagoActual = getItem(position)
        val simboloMoneda = "$"
        holder.montoPago.text = context?.getString(R.string.simboloMoneda,simboloMoneda,pagoActual.monto)
        holder.fechaPago.text = pagoActual.fecha

    }

    fun getPaymentAt(position: Int): Pago{
        return getItem(position)
    }

    fun setOnClickListener(listener: View.OnClickListener){
        this.listener = listener
    }

    override fun onClick(v: View?){
        listener?.onClick(v)
    }
}