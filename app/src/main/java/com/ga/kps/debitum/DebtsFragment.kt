package com.ga.kps.debitum

import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager.widget.ViewPager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_main.*
import room.components.viewModels.DeudaViewModel
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
class DebtsFragment : Fragment() {
    lateinit var deudaViewModel: DeudaViewModel

    private lateinit var collectionPagerAdapter: CollectionPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        MobileAds.initialize(context){}

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_debts,container, false)
        val viewPager = v.findViewById<ViewPager>(R.id.ViewPagerPrincipal)
        val tabLayout = v.findViewById<TabLayout>(R.id.TabLayoutPrincipal)
        //val cantidadDeudaTV = v.findViewById<TextView>(R.id.cantidadDeudaTotalTV)

        

        //setupViewPager(viewPager)
        collectionPagerAdapter = CollectionPagerAdapter(childFragmentManager)
        viewPager.adapter = collectionPagerAdapter
        tabLayout.setupWithViewPager(viewPager)

        deudaViewModel = ViewModelProviders.of(this).get(DeudaViewModel::class.java)

        deudaViewModel.getSumaDeudas().observe(this, Observer {
            val prefs = PreferenceManager.getDefaultSharedPreferences(context)
            val simboloMoneda = prefs.getString("moneySign","$")
            if(it == null){
        //        cantidadDeudaTV.text = getString(R.string.simboloMoneda, simboloMoneda, 0f)
            }else{
        //        cantidadDeudaTV.text = getString(R.string.simboloMoneda, simboloMoneda, it)
            }

        })

        val fab = v.findViewById<FloatingActionButton>(R.id.addNewDebtFAB)
        fab.setOnClickListener {
            val nav = Intent(context,AddDebtActivity::class.java)
            startActivity(nav)
        }

        return v
    }


    private  val ARG_OBJECT = "object"

    private inner class CollectionPagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm){
        override fun getCount(): Int = 4

        override fun getItem(i: Int): Fragment{
            val fragment = ActualDebtsFragment()
            fragment.arguments = Bundle().apply {
                putInt(ARG_OBJECT,i + 1)
            }
            return fragment
        }

        override fun getPageTitle(position: Int): CharSequence{
            return "OBJECT ${(position + 1)}"
        }
    }
}