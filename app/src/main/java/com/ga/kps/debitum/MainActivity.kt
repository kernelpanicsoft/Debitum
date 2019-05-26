package com.ga.kps.debitum

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_main.*
import model.Cuenta
import model.Deuda
import room.components.viewModels.CuentaViewModel
import room.components.viewModels.DeudaViewModel

class MainActivity : AppCompatActivity(){



    lateinit var deudaViewModel : DeudaViewModel
    lateinit var cuentaViewModel: CuentaViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)
        title = "Debitum"


        setupViewPager(ViewPagerPrincipal)

        TabLayoutPrincipal.setupWithViewPager(ViewPagerPrincipal)

        cuentaViewModel = ViewModelProviders.of(this).get(CuentaViewModel::class.java)
        deudaViewModel = ViewModelProviders.of(this).get(DeudaViewModel::class.java)




        anadirDeudadFAB.setOnClickListener {
            val nav = Intent(this@MainActivity,AddDebtActivity::class.java)
            startActivity(nav)

            //deudaViewModel.insert(Deuda(0,"Sears",1,123.0f,"Prueba de deuda","12/5/2019",23.5f,1,1))
        }

        cuentaViewModel.getCuenta(1).observe(this, Observer {
            val simboloMoneda = "$"
            cantidadDeudaTotalTV.text = getString(R.string.simboloMoneda,simboloMoneda,it?.deuda_total)
        })



    }

    private fun setupViewPager(pager: ViewPager){
        val adapter = ViewPagerAdapter(supportFragmentManager)
        adapter.addFragment(ActualDebtsFragment(),getString(R.string.actuales))
        adapter.addFragment(PassDebtsFragment(),getString(R.string.pasadas))

        pager.adapter = adapter


    }

    private inner class ViewPagerAdapter(manager: FragmentManager) : FragmentPagerAdapter(manager){
        private val mFragmentList = ArrayList<Fragment>()
        private val mFragmentTitleList = ArrayList<String>()


        override fun getItem(position: Int): Fragment{
            return mFragmentList[position]
        }

        override fun getCount(): Int {
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
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            R.id.itemSettings ->{
                val cuentaViewmodel = ViewModelProviders.of(this).get(CuentaViewModel::class.java)
                cuentaViewmodel.insert(Cuenta(0,0f))
            }
        }
        return super.onOptionsItemSelected(item)
    }

}
