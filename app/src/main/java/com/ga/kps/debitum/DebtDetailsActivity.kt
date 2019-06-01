package com.ga.kps.debitum

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
import kotlinx.android.synthetic.main.activity_debt_details.*

class DebtDetailsActivity : AppCompatActivity() {
    var debtID: Int = -1

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
            startActivity(nav)
        }
    }

    private fun setupViewPager(pager: ViewPager){
        val adapter = ViewPagerAdapter(supportFragmentManager)
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

}
