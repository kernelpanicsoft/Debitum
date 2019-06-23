package com.ga.kps.debitum

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import room.components.viewModels.PagoViewModel

class DebtPaymentsHistoryFragment: Fragment() {
    lateinit var pagosViewModel: PagoViewModel
    lateinit var RV: RecyclerView
    val simboloMoneda = "$"


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?{
        val v = inflater.inflate(R.layout.fragment_debt_payments_history, container,false)
        RV = v.findViewById(R.id.RecViewHistorialDePagos)
        RV.setHasFixedSize(true)

        val mLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL,true)
        mLayoutManager.stackFromEnd = true
        RV.layoutManager = mLayoutManager

        val adapter = DebtPaymentsAdapter(context)
        pagosViewModel = ViewModelProviders.of(this).get(PagoViewModel::class.java)
        pagosViewModel.getAllPagosDeuda((activity as DebtDetailsActivity).debtID).observe(this, Observer {
            adapter.submitList(it)
        })


        adapter.setOnClickListener(View.OnClickListener {
            val selectPayment = adapter.getPaymentAt(RV.getChildAdapterPosition(it))

            val paymentDetails = AlertDialog.Builder(context!!)
            paymentDetails.setTitle(getString(R.string.detalles_de_pago))
            paymentDetails.setView(R.layout.payment_details_dialog)

            val inflater = requireActivity().layoutInflater
            val dialogView : View = inflater.inflate(R.layout.payment_details_dialog,null)
            val montoPagoTextView = dialogView.findViewById<TextView>(R.id.montoPagoTV)
            val fechaPagoTextView = dialogView.findViewById<TextView>(R.id.FechaPagoTV)
            val notaPagoTextView = dialogView.findViewById<TextView>(R.id.NotaTV)

            montoPagoTextView.text = context?.getString(R.string.simboloMoneda,simboloMoneda,selectPayment.monto)
            fechaPagoTextView.text = selectPayment.fecha
            notaPagoTextView.text = selectPayment.nota
            paymentDetails.setView(dialogView)
            paymentDetails.setPositiveButton(getString(R.string.entendido)){ _, _->

            }
            paymentDetails.setNeutralButton(getString(R.string.modificar)){ _, _ ->

            }
            val paymentDetailsDialog = paymentDetails.create()
            paymentDetailsDialog.show()
        })

        RV.adapter = adapter
        return v
    }


}