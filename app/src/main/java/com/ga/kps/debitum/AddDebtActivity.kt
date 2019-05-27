package com.ga.kps.debitum

import android.app.DatePickerDialog
import android.arch.lifecycle.ViewModelProviders
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.activity_add_debt.*
import model.Deuda
import room.components.viewModels.DeudaViewModel
import java.text.SimpleDateFormat
import java.util.*
import helpcodes.estatusDeuda;
import room.components.viewModels.CuentaViewModel


class AddDebtActivity : AppCompatActivity() {
    lateinit var deudaViewModel : DeudaViewModel
    lateinit var cuentaViewModel: CuentaViewModel
    var tipo = 0
    var fecha = ""
    val calendario = Calendar.getInstance()
    val sdf = SimpleDateFormat.getDateTimeInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_debt)

        setSupportActionBar(toolbar)
        val ab = supportActionBar
        ab!!.setDisplayHomeAsUpEnabled(true)
        title = getString(R.string.anadir_nueva_deuda)

        deudaViewModel = ViewModelProviders.of(this).get(DeudaViewModel::class.java)
        cuentaViewModel = ViewModelProviders.of(this).get(CuentaViewModel::class.java)



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
            val deuda = Deuda(0,tituloDeudaET.text.toString(),tipo,montoPagoET.text.toString().toFloat(),notaPagoET.text.toString(),fechaPagoBT.text.toString(),0f,estatusDeuda.ACTIVA,1)

            deudaViewModel.insert(deuda)
            actualizaDeudaTotal(deuda.monto)
            finish()
        }

    }

    fun actualizaDeudaTotal(monto: Float){
        cuentaViewModel.updateDeudaTotal(monto)
    }
}
