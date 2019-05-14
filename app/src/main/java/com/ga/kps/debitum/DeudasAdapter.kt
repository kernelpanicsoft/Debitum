package com.ga.kps.debitum

import android.support.v7.recyclerview.extensions.ListAdapter
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import model.Deuda

class DeudasAdapter : ListAdapter<Deuda, DeudasAdapter.ViewHolder>(DIFF_CALLBACK()), View.OnClickListener {
    private var listener : View.OnClickListener? = null

    class DIFF_CALLBACK: DiffUtil.ItemCallback<Deuda>(){
        override fun areItemsTheSame(oldItem: Deuda, newITem: Deuda): Boolean {
            return oldItem.id == newITem.id
        }

        override fun areContentsTheSame(oldItem: Deuda, newITem: Deuda): Boolean {
            return oldItem.monto == newITem.monto
        }
    }

    inner class ViewHolder(v: View): RecyclerView.ViewHolder(v){
        val tituloDeuda = v.findViewById<TextView>(R.id.tituloDeudaET)
        val montoRestanteDeuda = v.findViewById<TextView>(R.id.DeudaTV)
        val fechaDeuda = v.findViewById<TextView>(R.id.fechaDeudaTV)
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_saved_debt,parent, false)
        v.setOnClickListener(this)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val deudaActual = getItem(position)
        holder.tituloDeuda.text = deudaActual.titulo
        holder.montoRestanteDeuda.text = deudaActual.monto.toString()
        holder.fechaDeuda.text = deudaActual.fecha_adquision
    }

    fun getDebtAt(position: Int): Deuda{
        return getItem(position)
    }

    fun setOnClickListener(listAdapter: View.OnClickListener){
        this.listener = listAdapter
    }

    override fun onClick(v: View?) {
        listener!!.onClick(v)
    }
}