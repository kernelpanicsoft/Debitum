package com.ga.kps.debitum

import android.app.DatePickerDialog
import androidx.lifecycle.ViewModelProviders
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.android.material.snackbar.Snackbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_add_debt.*
import model.Deuda
import room.components.viewModels.DeudaViewModel
import java.text.SimpleDateFormat
import java.util.*
import helpcodes.EstatusDeuda
import room.components.viewModels.CuentaViewModel
import java.text.DateFormat


class AddDebtActivity : AppCompatActivity() {
    lateinit var deudaViewModel : DeudaViewModel
    lateinit var cuentaViewModel: CuentaViewModel
    lateinit var deuda: Deuda
    var tipo = 0
    var fecha = ""
    private val calendario: Calendar = Calendar.getInstance()
    private val sdf: DateFormat = SimpleDateFormat.getDateInstance(DateFormat.SHORT)

    var debtID: Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_debt)

        setSupportActionBar(toolbar)
        val ab = supportActionBar
        ab!!.setDisplayHomeAsUpEnabled(true)
        title = getString(R.string.anadir_nueva_deuda)

        deudaViewModel = ViewModelProviders.of(this).get(DeudaViewModel::class.java)
        cuentaViewModel = ViewModelProviders.of(this).get(CuentaViewModel::class.java)

        debtID = intent.getIntExtra("ID", -1)
        deuda = Deuda(0)
        if(debtID != -1){
            populateFieldsForEdit()
        }


        tipoDeudaSP.adapter = ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item,this.resources.getStringArray(R.array.tipo_deuda))
        tipoDeudaSP.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
               tipo = position
            }

        }

        fechaPagoBT.text = sdf.format(calendario.time)

        fechaPagoBT.setOnClickListener {
            val datePickerFragment = DatePickerDialog(this@AddDebtActivity, DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                calendario.set(Calendar.YEAR, year)
                calendario.set(Calendar.MONTH, month)
                calendario.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                //Toast.makeText(this@AnadirCitaMedicaActivity,"Fecha seleccionada: " + sdf.format(calendario.time), Toast.LENGTH_SHORT).show()
                fechaPagoBT.text = sdf.format(calendario.time)

            }, calendario.get(Calendar.YEAR),calendario.get(Calendar.MONTH), calendario.get(Calendar.DAY_OF_MONTH))
            datePickerFragment.show()

        }



        guardarDeudaFAB.setOnClickListener {
            if(tituloDeudaET.text.isNullOrEmpty() || montoPagoET.text.isNullOrEmpty()){
                Snackbar.make(it,getString(R.string.titulo_y_monto_requerido), Snackbar.LENGTH_LONG).show()
            }else {
                if((montoPagoET.text.toString().toFloat()) == 0.0f){
                    Snackbar.make(it, getString(R.string.monto_mayor_cero), Snackbar.LENGTH_LONG).show()
                }else{
                    deuda.titulo = tituloDeudaET.text.toString()
                    deuda.tipo = tipo
                    deuda.monto = montoPagoET.text.toString().toFloat()
                    deuda.fecha_adquision = fechaPagoBT.text.toString()
                    deuda.estado = EstatusDeuda.ACTIVA
                    deuda.nota = notaPagoET.text.toString()
                    deuda.cuenta_id = 1

                    if(deuda.id == 0){
                        deudaViewModel.insert(deuda)
                        actualizaDeudaTotal(deuda.monto)
                    }else{
                        deudaViewModel.update(deuda)
                    }

                    finish()
                }
            }
        }

    }

    private fun actualizaDeudaTotal(monto: Float){
        cuentaViewModel.updateDeudaTotal(monto)
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

    private fun populateFieldsForEdit(){
        val debtTypes = this.resources.getStringArray(R.array.tipo_deuda)
        deudaViewModel.getDeuda(debtID).observe(this, androidx.lifecycle.Observer {
            tituloDeudaET.setText(it?.titulo,TextView.BufferType.EDITABLE)
            tipoDeudaSP.setSelection(it.tipo)
            montoPagoET.setText(it.monto.toString(), TextView.BufferType.EDITABLE)
            notaPagoET.setText(it.nota, TextView.BufferType.EDITABLE)
            fechaPagoBT.setText(it.fecha_adquision)

            deuda.id = it.id
            deuda.pagado = it.pagado


        })
    }
}
