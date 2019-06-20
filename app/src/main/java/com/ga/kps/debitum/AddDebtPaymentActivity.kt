package com.ga.kps.debitum

import android.app.DatePickerDialog
import android.arch.lifecycle.ViewModelProviders
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import kotlinx.android.synthetic.main.add_debt_payment_activity.*
import model.Pago
import room.components.viewModels.CuentaViewModel
import room.components.viewModels.DeudaViewModel
import room.components.viewModels.PagoViewModel
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class AddDebtPaymentActivity : AppCompatActivity() {
    lateinit var pagoViewModel: PagoViewModel
    lateinit var deudaViewModel: DeudaViewModel
    lateinit var cuentaViewModel: CuentaViewModel
    private val calendario: Calendar = Calendar.getInstance()
    private val sdf: DateFormat = SimpleDateFormat.getDateInstance(DateFormat.SHORT)
    private var deudaID: Int = 0

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
        deudaID = intent.getIntExtra("DEBT_ID",-1)

        fechaPagoBT.text = sdf.format(calendario.time)
        fechaPagoBT.setOnClickListener {
            val datePickerFragment = DatePickerDialog(this@AddDebtPaymentActivity, DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                calendario.set(Calendar.YEAR, year)
                calendario.set(Calendar.MONTH, month)
                calendario.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                //Toast.makeText(this@AnadirCitaMedicaActivity,"Fecha seleccionada: " + sdf.format(calendario.time), Toast.LENGTH_SHORT).show()
                fechaPagoBT.text = sdf.format(calendario.time)

            }, calendario.get(Calendar.YEAR),calendario.get(Calendar.MONTH), calendario.get(Calendar.DAY_OF_MONTH))
            datePickerFragment.show()
        }

        saveDebtPaymentFAB.setOnClickListener {
            if(montoPagoET.text.isNullOrEmpty()){
                Snackbar.make(it,getString(R.string.especifique_monto_pago),Snackbar.LENGTH_LONG).show()
            }else{
                if(montoPagoET.text.toString().toFloat() <= 0f){
                    Snackbar.make(it,getString(R.string.el_monto_del_pago), Snackbar.LENGTH_LONG).show()
                }else{
                    val pago = Pago(0)
                    pago.deuda_ID = intent.getIntExtra("DEBT_ID",-1)
                    pago.fecha = fechaPagoBT.text.toString()
                    pago.monto = montoPagoET.text.toString().toFloat()
                    pago.nota = notaPagoET.text.toString()
                    savePaymentToDB(pago)
                }
            }


        }
    }

    fun savePaymentToDB(payment: Pago){
        deudaViewModel.getDeuda(deudaID).observe(this, android.arch.lifecycle.Observer {
            Log.d("PAGAS",(payment.monto + it!!.pagado).toString() + "TOTAL: " + it.monto )
           // if(payment.monto + it!!.pagado >= it.monto ){
              //  Toast.makeText(this,"con esto pagas la deuda" + payment.monto + it!!.pagado, Toast.LENGTH_SHORT).show()
            //    Log.d("PAGAS","" +payment.monto + it!!.pagado + " " + it.monto)
              //  title = " " + payment.monto + it!!.pagado
          //  }
        })
        pagoViewModel.insert(payment)
        actualizaDeuda(deudaID, payment.monto)
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
