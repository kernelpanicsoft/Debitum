package com.ga.kps.debitum

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_main.*
import room.components.viewModels.DeudaViewModel

class DebtsFragment : Fragment() {
    lateinit var deudaViewModel: DeudaViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_debts,container, false)
        val viewPager = v.findViewById<ViewPager>(R.id.ViewPagerPrincipal)
        val tabLayout = v.findViewById<TabLayout>(R.id.TabLayoutPrincipal)
        val cantidadDeudaTV = v.findViewById<TextView>(R.id.cantidadDeudaTotalTV)

        setupViewPager(viewPager)
        tabLayout.setupWithViewPager(viewPager)

        deudaViewModel = ViewModelProviders.of(this).get(DeudaViewModel::class.java)

        deudaViewModel.getSumaDeudas().observe(this, Observer {
            val simboloMoneda = "$"
            cantidadDeudaTV.text =
                getString(R.string.simboloMoneda, simboloMoneda, it)
        })

        return v
    }

    private fun setupViewPager(pager: ViewPager){
        val adapter = ViewPagerAdapter(childFragmentManager)
        adapter.addFragment(ActualDebtsFragment(),getString(R.string.actuales))
        adapter.addFragment(PassDebtsFragment(),getString(R.string.pasadas))

        pager.adapter = adapter


    }

    private inner class ViewPagerAdapter(manager: FragmentManager) : FragmentPagerAdapter(manager){
        private val mFragmentList = ArrayList<Fragment>()
        private val mFragmentTitleList = ArrayList<String>()


        override fun getItem(position: Int): Fragment {
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
}