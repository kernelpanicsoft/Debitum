package com.ga.kps.debitum

import android.arch.lifecycle.ViewModelProviders
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_add_debt.*
import kotlinx.android.synthetic.main.activity_add_debt.fechaPagoBT
import kotlinx.android.synthetic.main.activity_add_debt.montoPagoET
import kotlinx.android.synthetic.main.activity_add_debt.notaPagoET
import kotlinx.android.synthetic.main.activity_debt_details.*
import kotlinx.android.synthetic.main.activity_debt_details.toolbar
import kotlinx.android.synthetic.main.add_debt_payment_activity.*
import model.Pago
import room.components.viewModels.CuentaViewModel
import room.components.viewModels.DeudaViewModel
import room.components.viewModels.PagoViewModel

class AddDebtPaymentActivity : AppCompatActivity() {
    lateinit var pagoViewModel: PagoViewModel
    lateinit var deudaViewModel: DeudaViewModel
    lateinit var cuentaViewModel: CuentaViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_debt_payment_activity)

        setSupportActionBar(toolbar)
        val ab = supportActionBar
        ab!!.setDisplayHomeAsUpEnabled(true)
        title = getString(R.string.anadir_pago)

        pagoViewModel = ViewModelProviders.of(this).get(PagoViewModel::class.java)
        deudaViewModel = ViewModelProviders.of(this).get(DeudaViewModel::class.java)
        cuentaViewModel = ViewModelProviders.of(this).get(CuentaViewModel::class.java)

        saveDebtPaymentFAB.setOnClickListener {
            val pago = Pago(0)
            pago.deuda_ID = intent.getIntExtra("DEBT_ID",-1)
            pago.fecha = fechaPagoBT.text.toString()
            pago.monto = montoPagoET.text.toString().toFloat()
            pago.nota = notaPagoET.text.toString()
            savePaymentToDB(pago)
        }
    }

    fun savePaymentToDB(payment: Pago){
        pagoViewModel.insert(payment)
        actualizaDeuda(intent.getIntExtra("DEBT_ID",-1), payment.monto)
        actualizaDeudaTotal(-payment.monto)
        finish()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home ->{
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun actualizaDeuda(id: Int, monto: Float){
        deudaViewModel.updateDeuda(id,monto)
    }

    fun actualizaDeudaTotal(monto: Float){
        cuentaViewModel.updateDeudaTotal(monto)
    }
}
