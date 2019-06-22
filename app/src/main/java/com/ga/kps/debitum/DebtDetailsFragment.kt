package com.ga.kps.debitum

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.github.lzyzsd.circleprogress.ArcProgress
import helpcodes.EstatusDeuda
import helpcodes.EstatusDeuda.ACTIVA
import model.Deuda
import room.components.viewModels.CuentaViewModel
import room.components.viewModels.DeudaViewModel

class DebtDetailsFragment: Fragment() {

    lateinit var RV: RecyclerView
    lateinit var deudasViewModel: DeudaViewModel
    lateinit var deudaActualLive : LiveData<Deuda>
    val simboloMoneda = "$"

    var tituloDeudaTextView: TextView? = null
    var fechaDeudaTextView: TextView? = null
    var deudaTotalTextView: TextView? = null
    var montoPagadoTextView: TextView? = null
    var montoRestanteTextView: TextView? = null
    var tipoDeudaTextView: TextView? = null
    var arcoProgresso: ArcProgress? = null




    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) : View?{
        val v = inflater.inflate(R.layout.fragment_debt_details, container, false)

        tituloDeudaTextView = v.findViewById(R.id.tituloDeudaTV)
        fechaDeudaTextView = v.findViewById(R.id.fechaDeudaTV)
        deudaTotalTextView = v.findViewById(R.id.montoDeudaTV)
        montoPagadoTextView = v.findViewById(R.id.montoPagadoTV)
        montoRestanteTextView = v.findViewById(R.id.montoRestanteTV)
        tipoDeudaTextView = v.findViewById(R.id.tipoDeudaTV)
        arcoProgresso = v.findViewById(R.id.arc_progress)



        deudasViewModel = ViewModelProviders.of(this).get(DeudaViewModel::class.java)

        deudaActualLive = deudasViewModel.getDeuda((activity as DebtDetailsActivity).debtID)
        deudaActualLive.observe(this, Observer {
            populateDebtUI(it)
        })

        //Toast.makeText(context,"ID de la toma: " + (activity as DebtDetailsActivity).debtID, Toast.LENGTH_SHORT).show()
        return v
    }

    fun populateDebtUI(debt: Deuda?){
        tituloDeudaTextView?.text = debt?.titulo.toString()
        fechaDeudaTextView?.text = debt?.fecha_adquision
        deudaTotalTextView?.text = context?.getString(R.string.simboloMoneda,simboloMoneda,debt?.monto)
        montoPagadoTextView?.text = context?.getString(R.string.simboloMoneda,simboloMoneda,debt?.pagado)
        montoRestanteTextView?.text = context?.getString(R.string.simboloMoneda,simboloMoneda,(debt!!.monto - debt.pagado))
        arcoProgresso?.progress = ((debt!!.pagado * 100f) / debt.monto).toInt()

        val tipoDeudaArray = context?.resources?.getStringArray(R.array.tipo_deuda)
        tipoDeudaTextView?.text =  tipoDeudaArray?.get(debt.tipo)


        if((debt.pagado >= debt.monto) && debt.estado == EstatusDeuda.ACTIVA){
            val builder = AlertDialog.Builder(context!!)
            val negativeButton = builder.setTitle(getString(R.string.deuda_pagado))
                .setMessage(getString(R.string.deuda_ha_sido_pagada))
                .setPositiveButton(getString(R.string.cambiar_estado)) { _, _ ->
                    deudasViewModel.updateEstatusDeuda((activity as DebtDetailsActivity).debtID, EstatusDeuda.PAGADA)
                }
                .setNegativeButton(getString(R.string.seguir_pagando)) { _, _ ->

                }
            val dialog = builder.create()
            dialog.show()


        }

    }

    fun deleteDebt(){
        if(deudaActualLive.value?.estado == ACTIVA) {
            actualizaDeudaTotal(deudaActualLive.value?.monto!! - deudaActualLive.value?.pagado!!)
        }
        removeObservers()
        val deudaAux = Deuda((activity as DebtDetailsActivity).debtID)
        deudasViewModel.delete(deudaAux)
    }

    private fun removeObservers(){

        if(deudaActualLive.hasActiveObservers()){
            deudaActualLive.removeObservers(this)
        }
        Log.d("TEXT", "EStas llamando a removeObservers " + deudaActualLive.value?.monto)
    }

    private fun actualizaDeudaTotal(monto: Float){
        var cuentaViewModel: CuentaViewModel = ViewModelProviders.of(this).get(CuentaViewModel::class.java)
        cuentaViewModel.updateDeudaTotal(-monto)
    }

}