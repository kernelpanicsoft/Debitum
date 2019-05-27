package com.ga.kps.debitum

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import model.Deuda
import room.components.viewModels.DeudaViewModel

class DebtDetailsFragment: Fragment() {

    lateinit var RV: RecyclerView
    lateinit var deudasViewModel: DeudaViewModel

    var tituloDeudaTextView: TextView? = null
    var fechaDeudaTextView: TextView? = null
    var deudaTotalTextView: TextView? = null



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) : View?{
        val v = inflater.inflate(R.layout.fragment_debt_details, container, false)

        tituloDeudaTextView = v.findViewById(R.id.tituloDeudaTV)
        fechaDeudaTextView = v.findViewById(R.id.fechaDeudaTV)
        deudaTotalTextView = v.findViewById(R.id.montoDeudaTV)



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
        deudaTotalTextView?.text = debt?.monto.toString()

    }


}