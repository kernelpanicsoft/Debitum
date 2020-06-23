package com.ga.kps.debitumag

import android.content.Context
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import model.CantidadTipoDeuda
import androidx.recyclerview.widget.ListAdapter



class DebtByTypeAdapter(val context: Context?) : ListAdapter<CantidadTipoDeuda, DebtByTypeAdapter.ViewHolder>(DIFF_CALLBACK()), View.OnClickListener{
    private var listener : View.OnClickListener? = null

    class DIFF_CALLBACK: DiffUtil.ItemCallback<CantidadTipoDeuda>(){
        override fun areItemsTheSame(
            oldItem: CantidadTipoDeuda,
            newItem: CantidadTipoDeuda
        ): Boolean {
            return oldItem.tipo == newItem.tipo
        }

        override fun areContentsTheSame(
            oldItem: CantidadTipoDeuda,
            newItem: CantidadTipoDeuda
        ): Boolean {
            return oldItem.cantidadTipo == newItem.cantidadTipo && oldItem.deudaPorTipo == newItem.deudaPorTipo && oldItem.pagadoPorTipo == newItem.pagadoPorTipo
        }
    }

    inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v){
        val categoria = v.findViewById<TextView>(R.id.categoriaLabelTV)
        val numeroDeudas = v.findViewById<TextView>(R.id.numeroDeudasLabelTV)
        val cantidadPagado = v.findViewById<TextView>(R.id.cantidadPagadoLabelTV)
        val cantidadTotal = v.findViewById<TextView>(R.id.cantidadTotalLabelTV)
        val cantidadRestante = v.findViewById<TextView>(R.id.cantidadRestanteTV)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_debt_stat_type, parent, false)
        v.setOnClickListener(this)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val debCat = getItem(position)
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        val simboloMoneda = prefs.getString("moneySign","$")

        holder.numeroDeudas.text = debCat.cantidadTipo.toString()
        holder.cantidadPagado.text = context?.getString(R.string.simboloMoneda,simboloMoneda,debCat.pagadoPorTipo)
        holder.cantidadTotal.text = context?.getString(R.string.simboloMoneda,simboloMoneda,debCat.deudaPorTipo)
        holder.cantidadRestante.text = context?.getString(R.string.simboloMoneda,simboloMoneda,debCat.deudaPorTipo!! - debCat.pagadoPorTipo!!)
        holder.categoria.text = getNameOfType(debCat.tipo)
    }

    fun getCatAt(position: Int): CantidadTipoDeuda{
        return getItem(position)
    }

    fun setOnClickListener(listAdapter: View.OnClickListener){
        this.listener = listAdapter
    }

    override fun onClick(v: View?){
        listener!!.onClick(v)
    }

    private fun getNameOfType(value: Int?) : String{
        val tipoDeudaArray = context?.resources?.getStringArray(R.array.tipo_deuda)
        return tipoDeudaArray!![value!!]
    }
}