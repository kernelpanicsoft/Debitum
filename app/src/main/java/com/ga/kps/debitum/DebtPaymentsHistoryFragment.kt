package com.ga.kps.debitum

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import room.components.viewModels.PagoViewModel

class DebtPaymentsHistoryFragment: Fragment() {
    lateinit var pagosViewModel: PagoViewModel
    lateinit var RV: RecyclerView

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

        RV.adapter = adapter
        return v
    }


}