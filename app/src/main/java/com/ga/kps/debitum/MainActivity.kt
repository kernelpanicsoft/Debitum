package com.ga.kps.debitum

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.tabs.TabLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import android.view.Menu
import android.view.MenuItem
import android.widget.LinearLayout
import com.facebook.ads.AdSize
import com.facebook.ads.AdView
import kotlinx.android.synthetic.main.activity_main.*
import model.Cuenta
import model.Deuda
import room.components.viewModels.CuentaViewModel
import room.components.viewModels.DeudaViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var adView: AdView
    lateinit var deudaViewModel: DeudaViewModel
    lateinit var cuentaViewModel: CuentaViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)
        title = "Debitum"

        val manager = supportFragmentManager

        cuentaViewModel = ViewModelProviders.of(this).get(CuentaViewModel::class.java)
        deudaViewModel = ViewModelProviders.of(this).get(DeudaViewModel::class.java)

        adView = AdView(this, "189318145820313_189320145820113", AdSize.BANNER_HEIGHT_50)
        val adContainer = findViewById<LinearLayout>(R.id.banner_container)
        adContainer.addView(adView)
        adView.loadAd()

        if(savedInstanceState == null){
            val transaction = manager.beginTransaction()
            transaction.replace(R.id.fragmentContainer, DebtsFragment(), "Estadisticas").commit()
        }

        bnv.setOnNavigationItemSelectedListener {item ->
            val transaction = manager.beginTransaction()

            when(item.itemId){
                R.id.debtsItem ->{
                    transaction.replace(R.id.fragmentContainer, DebtsFragment(), "Deudas").commit()
                }
                R.id.calendarItem ->{
                    transaction.replace(R.id.fragmentContainer, ScheduleFragment(), "Calendario").commit()
                }
                R.id.statsItem ->{
                    transaction.replace(R.id.fragmentContainer, StatsFragment(), "Estadisticas").commit()
                }
            }
            return@setOnNavigationItemSelectedListener true
        }
    }



    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            R.id.itemSettings ->{
                val settingIntent = Intent(this,SettingsActivity::class.java)
                startActivity(settingIntent)

            }
        }
        return super.onOptionsItemSelected(item)
    }

}
