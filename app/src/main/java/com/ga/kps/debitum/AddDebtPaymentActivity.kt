package com.ga.kps.debitum

import android.app.Activity
import android.app.DatePickerDialog
import androidx.lifecycle.ViewModelProviders
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.LiveData
import kotlinx.android.synthetic.main.add_debt_payment_activity.*
import model.Pago
import room.components.viewModels.CuentaViewModel
import room.components.viewModels.DeudaViewModel
import room.components.viewModels.PagoViewModel
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import androidx.lifecycle.Observer
import helpcodes.EstatusDeuda
import model.Deuda

class AddDebtPaymentActivity : AppCompatActivity() {
    lateinit var pagoViewModel: PagoViewModel
    lateinit var deudaViewModel: DeudaViewModel
    lateinit var cuentaViewModel: CuentaViewModel
    private val calendario: Calendar = Calendar.getInstance()
    private val sdf: DateFormat = SimpleDateFormat.getDateInstance(DateFormat.SHORT)
    private var deudaID: Int = 0
    lateinit var deuda : LiveData<Deuda>

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
        deuda = deudaViewModel.getDeuda(deudaID)
        deuda.observe(this, Observer{
            
        })

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

        saveDebtPaymentFAB.setOnClickListener { it ->

            if(montoPagoET.text.isNullOrEmpty()){
                Snackbar.make(it,getString(R.string.especifique_monto_pago),
                    Snackbar.LENGTH_LONG).show()
            }else{
                if(montoPagoET.text.toString().toFloat() <= 0f){
                    Snackbar.make(it,getString(R.string.el_monto_del_pago), Snackbar.LENGTH_LONG).show()
                }
                else{
                   // Toast.makeText(this,"Valor: " + deuda.value?.titulo, Toast.LENGTH_SHORT).show()

                        val pago = Pago(0)
                        pago.deuda_ID = intent.getIntExtra("DEBT_ID",-1)
                        pago.fecha = fechaPagoBT.text.toString()
                        pago.monto = montoPagoET.text.toString().toFloat()
                        pago.nota = notaPagoET.text.toString()


                    when {
                        pago.monto + deuda.value!!.pagado > deuda.value!!.monto && deuda.value!!.estado == EstatusDeuda.ACTIVA -> {
                            val builder = AlertDialog.Builder(this)
                            builder.setTitle(getString(R.string.liquidacion_de_la_deuda))
                            builder.setMessage(getString(R.string.pago_ingresado_liquida_superando_monto_inicial))
                            builder.setPositiveButton(getString(R.string.cerrar)){ _, _ ->
                                deudaViewModel.updateEstatusDeuda(deudaID, EstatusDeuda.PAGADA)
                                deudaViewModel.updateMontoOriginalDeuda(deudaID,pago.monto + deuda.value!!.pagado)
                                savePaymentToDB(pago)
                            }
                            builder.setNegativeButton(getString(R.string.continuar_pagando)){ _, _ ->
                                deudaViewModel.updateEstatusDeuda(deudaID, EstatusDeuda.SEGUIR)
                                deudaViewModel.updateMontoOriginalDeuda(deudaID,pago.monto + deuda.value!!.pagado)
                                savePaymentToDB(pago)

                            }
                            val alertDialog = builder.create()
                            alertDialog.show()

                        }
                        pago.monto + deuda.value!!.pagado == deuda.value!!.monto -> {
                            val builder = AlertDialog.Builder(this)
                            builder.setTitle(getString(R.string.liquidacion_de_la_deuda))
                            builder.setMessage(getString(R.string.pago_ingresado_liquida_deuda))
                            builder.setPositiveButton(getString(R.string.cerrar)){ _, _ ->
                                deudaViewModel.updateEstatusDeuda(deudaID, EstatusDeuda.PAGADA)
                                savePaymentToDB(pago)
                            }
                            builder.setNegativeButton(getString(R.string.continuar_pagando)){ _, _ ->
                                deudaViewModel.updateEstatusDeuda(deudaID, EstatusDeuda.SEGUIR)
                                savePaymentToDB(pago)
                            }
                            val alertDialog = builder.create()
                            alertDialog.show()
                        }
                        pago.monto + deuda.value!!.pagado > deuda.value!!.monto && deuda.value!!.estado == EstatusDeuda.SEGUIR -> {
                            deudaViewModel.updateMontoOriginalDeuda(deudaID,pago.monto + deuda.value!!.pagado)
                            savePaymentToDB(pago)
                        }
                        else -> savePaymentToDB(pago)
                    }
                }
            }
        }
    }

    private fun savePaymentToDB(payment: Pago){
        pagoViewModel.insert(payment)
        actualizaDeuda(deudaID, payment.monto)
        actualizaDeudaTotal(-payment.monto)
        setResult(Activity.RESULT_OK)
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

    private fun actualizaDeuda(id: Int, monto: Float){
        deudaViewModel.updateDeuda(id,monto)
    }

    private fun actualizaDeudaTotal(monto: Float){
        cuentaViewModel.updateDeudaTotal(monto)
    }
}
