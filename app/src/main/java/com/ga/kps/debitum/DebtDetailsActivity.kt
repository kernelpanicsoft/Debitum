package com.ga.kps.debitum

import android.app.Activity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import androidx.appcompat.app.AlertDialog
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import helpcodes.ANADIR_PAGO_DEUDA
import helpcodes.EstatusDeuda
import kotlinx.android.synthetic.main.activity_debt_details.*
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

        override fun getItem(position: Int) : Fragment {
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
                builder.setTitle(getString(R.string.opciones_de_deuda))
                builder.setItems(R.array.editar){ _, which ->
                    when(which){
                        0 ->{
                            val nav = Intent(this, AddDebtActivity::class.java)
                            nav.putExtra("ID", debtID)
                            startActivity(nav)
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
            R.id.itemLockDebt->{
                val builder = AlertDialog.Builder(this)
                builder.setTitle(getString(R.string.cerrar_deuda))
                    .setMessage(getString(R.string.cerrar_la_deuda_dialog))
                    .setPositiveButton(getString(R.string.cerrar)){ _, _ ->
                        val detailsFragment = adapter.getItem(0) as DebtDetailsFragment
                        detailsFragment.changeDebtStatus(debtID,EstatusDeuda.PAGADA)
                    }
                    .setNegativeButton(getString(R.string.cancelar)){ _,_ ->
                    }
                val alertDialog = builder.create()
                alertDialog.show()
            }
            R.id.itemUnlockDebt->{
                val builder = AlertDialog.Builder(this)
                builder.setTitle(getString(R.string.abrir_deuda))
                builder.setMessage(getString(R.string.abrir_la_deuda_dialog))
                builder.setPositiveButton(getString(R.string.abrir)){ _, _ ->
                    val detailsFragment = adapter.getItem(0) as DebtDetailsFragment
                    detailsFragment.changeDebtStatus(debtID,EstatusDeuda.SEGUIR)
                }
                builder.setNegativeButton(getString(R.string.cancelar)){ _, _ ->

                }

                val alertDialog = builder.create()
                alertDialog.show()

            }


        }
        return super.onOptionsItemSelected(item)
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        val lockDebtItem = menu?.findItem(R.id.itemLockDebt)
        val unlockDebtItem = menu?.findItem(R.id.itemUnlockDebt)

        val deudaViewModel = ViewModelProviders.of(this).get(DeudaViewModel::class.java)
        deudaViewModel.getDeuda(debtID).observe(this, Observer {
            if(it?.estado == EstatusDeuda.PAGADA){
                lockDebtItem?.isVisible = false
                unlockDebtItem?.isVisible = true
            }else{
                lockDebtItem?.isVisible = true
                unlockDebtItem?.isVisible = false
            }
        })

        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
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
