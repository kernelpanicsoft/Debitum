package com.ga.kps.debitum

import android.content.Context
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView

import model.Deuda

class DebtsAdapter(val context: Context?) : ListAdapter<Deuda, DebtsAdapter.ViewHolder>(DIFF_CALLBACK()), View.OnClickListener {
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
        val montoTotalDeuda = v.findViewById<TextView>(R.id.montoPagoTV)
        val montoPagadoDeuda = v.findViewById<TextView>(R.id.deudaPagadaTV)
        val porcentajePagadoDeuda = v.findViewById<TextView>(R.id.porcentajeDeudaTextView)
        val montoRestanteDeuda = v.findViewById<TextView>(R.id.deudaRestanteTV)
        val progresoDeuda = v.findViewById<ProgressBar>(R.id.progressBar)
        val fechaDeuda = v.findViewById<TextView>(R.id.fechaPagoTV)
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_saved_debt,parent, false)
        v.setOnClickListener(this)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val deudaActual = getItem(position)
        val simboloMoneda = "$"
        holder.tituloDeuda.text = deudaActual.titulo
        holder.montoTotalDeuda.text = context?.getString(R.string.simboloMoneda,simboloMoneda,deudaActual.monto)
        holder.montoPagadoDeuda.text = context?.getString(R.string.simboloMoneda,simboloMoneda,deudaActual.pagado)
        holder.fechaDeuda.text = deudaActual.fecha_adquision
        holder.montoRestanteDeuda.text = context?.getString(R.string.simboloMoneda,simboloMoneda,(deudaActual.monto - deudaActual.pagado))
        holder.porcentajePagadoDeuda.text = context?.getString(R.string.simboloPorcentaje, ((deudaActual.pagado * 100f) / deudaActual.monto).toInt())
        holder.progresoDeuda.progress = ((deudaActual.pagado * 100f) / deudaActual.monto).toInt()


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