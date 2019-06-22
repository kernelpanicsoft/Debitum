package com.ga.kps.debitum

import android.app.Activity
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.app.AlertDialog
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import helpcodes.ANADIR_PAGO_DEUDA
import kotlinx.android.synthetic.main.activity_debt_details.*
import model.Deuda
import room.components.viewModels.CuentaViewModel
import room.components.viewModels.DeudaViewModel

class DebtDetailsActivity : AppCompatActivity() {
    var debtID: Int = -1
    private lateinit var adapter : ViewPagerAdapter

        override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_debt_details)

        setSupportActionBar(toolbar)
        val ab = supportActionBar
        ab!!.setDisplayHomeAsUpEnabled(true)
        title = getString(R.string.detalles_de_la_deuda)


        setupViewPager(ViewPagerDetallesDeuda)
        tabLayoutDebtTomas.setupWithViewPager(ViewPagerDetallesDeuda)

        debtID = intent.getIntExtra("DEBT_ID", -1)

        anadirPagoDeudadFAB.setOnClickListener {
            val nav = Intent(this@DebtDetailsActivity, AddDebtPaymentActivity::class.java)
            nav.putExtra("DEBT_ID", debtID)
            startActivityForResult(nav, ANADIR_PAGO_DEUDA)
        }
    }

    private fun setupViewPager(pager: ViewPager){
        adapter = ViewPagerAdapter(supportFragmentManager)
        adapter.addFragment(DebtDetailsFragment(),getString(R.string.resumen))
        adapter.addFragment(DebtPaymentsHistoryFragment(),getString(R.string.historial_de_pagos))

        pager.adapter = adapter
    }

    private inner class ViewPagerAdapter(manager: FragmentManager) : FragmentPagerAdapter(manager){
        private val mFragmentList = ArrayList<Fragment>()
        private val mFragmentTitleList = ArrayList<String>()

        override fun getItem(position: Int) : Fragment{
            return mFragmentList[position]
        }

        override fun getCount(): Int{
            return mFragmentTitleList.size
        }

        fun addFragment(fragment: Fragment, title: String){
            mFragmentList.add(fragment)
            mFragmentTitleList.add(title)
        }

        override fun getPageTitle(position: Int): CharSequence{
            return mFragmentTitleList[position]
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_edit,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home ->{
                onBackPressed()
                return true
            }
            R.id.itemEditDelete ->{
                val builder = AlertDialog.Builder(this@DebtDetailsActivity)
                builder.setItems(R.array.editar){ _, which ->
                    when(which){
                        0 ->{

                        }
                        1 ->{
                            val builder = AlertDialog.Builder(this)
                            builder.setTitle(getString(R.string.eliminar_deuda))
                            builder.setMessage(getString(R.string.esta_seguro_eliminar_deuda))
                            builder.setPositiveButton(getString(R.string.eliminar)){
                                _, _ ->
                                deleteDebt()

                            }

                            builder.setNegativeButton(getString(R.string.cancelar)){
                                _,_ ->
                            }

                            val dialog = builder.create()
                            dialog.show()
                        }
                    }

                }
                val alertDialog = builder.create()
                alertDialog.show()
                return true
            }


        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when(requestCode){
            ANADIR_PAGO_DEUDA ->{
                if(resultCode == Activity.RESULT_OK){
                    Toast.makeText(this,getString(R.string.pago_registrado), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun deleteDebt(){
        val detailsFragment = adapter.getItem(0) as DebtDetailsFragment
        detailsFragment.deleteDebt()
        Toast.makeText(this,getString(R.string.deuda_eliminada), Toast.LENGTH_SHORT).show()
        finish()
    }


}
