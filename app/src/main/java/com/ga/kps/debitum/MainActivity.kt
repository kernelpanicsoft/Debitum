package com.ga.kps.debitum

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.view.Menu
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(){


    private var tabLayout: TabLayout? = null
    private var viewPager: ViewPager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)
        title = "Debitum"

        viewPager = findViewById(R.id.ViewPagerPrincipal)
        setupViewPager(viewPager!!)

        tabLayout = findViewById(R.id.TabLayoutPrincipal)
        tabLayout!!.setupWithViewPager(viewPager)



        anadirDeudadFAB.setOnClickListener {
            val nav = Intent(this@MainActivity,AddDebtActivity::class.java)
            startActivity(nav)
        }


    }

    private fun setupViewPager(pager: ViewPager){
        val adapter = ViewPagerAdapter(supportFragmentManager)
        adapter.addFragment(actualDebts(),"Actuales")
        adapter.addFragment(passDebts(),"Pasadas")

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


}
