package com.ga.kps.debitum

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
import model.Deuda
import room.components.viewModels.DeudaViewModel

class DebtDetailsFragment: Fragment() {

    lateinit var RV: RecyclerView
    lateinit var deudasViewModel: DeudaViewModel
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

        deudasViewModel.getDeuda((activity as DebtDetailsActivity).debtID).observe(this, Observer {
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
        tipoDeudaTextView?.text =  tipoDeudaArray?.get(debt?.tipo!!)


        if((debt!!.pagado >= debt.monto) && debt.estado == EstatusDeuda.ACTIVA){
            val builder = AlertDialog.Builder(context!!)
            builder.setTitle("Deuda pagada")
                .setMessage("La deuda ha sido pagada en su totalidad, ¿Desea cambiar su estado a \"Pagada\"? ")
                .setPositiveButton("Cambiar estado"){_, _ ->
                    deudasViewModel.updateEstatusDeuda((activity as DebtDetailsActivity).debtID, EstatusDeuda.PAGADA)
                }
                .setNegativeButton("Seguir Pagando"){ _, _ ->

                }
            val dialog = builder.create()
            dialog.show()


        }

    }


}