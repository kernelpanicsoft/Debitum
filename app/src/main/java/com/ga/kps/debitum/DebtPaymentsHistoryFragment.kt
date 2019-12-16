package com.ga.kps.debitum

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.preference.PreferenceManager
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import room.components.viewModels.CuentaViewModel
import room.components.viewModels.DeudaViewModel
import room.components.viewModels.PagoViewModel

class DebtPaymentsHistoryFragment: Fragment() {
    lateinit var pagosViewModel: PagoViewModel
    lateinit var deudaViewModel: DeudaViewModel
    lateinit var cuentaViewModel: CuentaViewModel
    lateinit var RV: RecyclerView
    var simboloMoneda = "$"


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?{
        val v = inflater.inflate(R.layout.fragment_debt_payments_history, container,false)
        RV = v.findViewById(R.id.RecViewHistorialDePagos)
        RV.setHasFixedSize(true)

        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        simboloMoneda = prefs.getString("moneySign","$")

        val mLayoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.VERTICAL,
            true
        )
        mLayoutManager.stackFromEnd = true
        RV.layoutManager = mLayoutManager

        val adapter = DebtPaymentsAdapter(context)
        pagosViewModel = ViewModelProviders.of(this).get(PagoViewModel::class.java)
        deudaViewModel = ViewModelProviders.of(this).get(DeudaViewModel::class.java)
        cuentaViewModel = ViewModelProviders.of(this).get(CuentaViewModel::class.java)

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
            paymentDetails.setNeutralButton(getString(R.string.eliminar)){ _, _ ->
                val builder = AlertDialog.Builder(context!!)
                builder.setTitle(getString(R.string.esta_seguro_eliminar_pago))
                builder.setMessage(getString(R.string.eliminar_pago_es_irreversible))
                builder.setPositiveButton(getString(R.string.eliminar)){_, _ ->
                    actualizaDeuda((activity as DebtDetailsActivity).debtID, -selectPayment.monto)
                    actualizaDeudaTotal(-selectPayment.monto)
                    pagosViewModel.delete(selectPayment)
                }
                builder.setNegativeButton(getString(R.string.cancelar)){ _, _ ->
                }
                val innerDialog = builder.create()
                innerDialog.show()
            }
            val paymentDetailsDialog = paymentDetails.create()
            paymentDetailsDialog.show()
        })

        RV.adapter = adapter
        return v
    }

    private fun actualizaDeuda(id: Int, monto: Float){
        deudaViewModel.updateDeuda(id,monto)
    }

    private fun actualizaDeudaTotal(monto: Float){
        cuentaViewModel.updateDeudaTotal(monto)
    }

}