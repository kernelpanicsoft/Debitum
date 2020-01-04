package com.ga.kps.debitum

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import helpcodes.EstatusDeuda
import room.components.viewModels.DeudaViewModel

class PassDebtsFragment: Fragment() {
    lateinit var deudasViewModel: DeudaViewModel
    lateinit var RV: RecyclerView
    lateinit var mAdView : AdView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?{
        val v = inflater.inflate(R.layout.fragment_pass_debts,container, false)
        RV = v.findViewById(R.id.RecViewDeudasPasadas)
        RV.setHasFixedSize(true)

        MobileAds.initialize(context) {}
        mAdView = v.findViewById(R.id.adViewActuallDebts)
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)


        return v

    }

    override fun onResume() {
        super.onResume()

        val mLayoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.VERTICAL,
            true
        )
        mLayoutManager.stackFromEnd = true
        RV.layoutManager = mLayoutManager


        val adapter = DebtsAdapter(context)
        deudasViewModel = ViewModelProviders.of(this).get(DeudaViewModel::class.java)
        deudasViewModel.getAllDeudasByState(EstatusDeuda.PAGADA).observe(this, Observer {
            adapter.submitList(it)
        })
        adapter.setOnClickListener(View.OnClickListener {
            val nav = Intent(context, DebtDetailsActivity::class.java)
            val selectDebt = adapter.getDebtAt(RV.getChildAdapterPosition(it))
            nav.putExtra("DEBT_ID", selectDebt.id)
            startActivity(nav)

        })


        RV.adapter = adapter
    }
}