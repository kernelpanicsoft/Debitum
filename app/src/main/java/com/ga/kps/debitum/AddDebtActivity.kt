package com.ga.kps.debitum

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_add_debt.*


class AddDebtActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_debt)

        setSupportActionBar(toolbar)
        val ab = supportActionBar
        ab!!.setDisplayHomeAsUpEnabled(true)
        title = getString(R.string.anadir_nueva_deuda)

        tipoDeudaSP.adapter = ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item,this.resources.getStringArray(R.array.tipo_deuda))
        tipoDeudaSP.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
               Toast.makeText(this@AddDebtActivity,"TeST", Toast.LENGTH_SHORT).show()
            }

        }

    }
}
