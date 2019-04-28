package com.ga.kps.debitum

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class passDebts: Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?{
        val v = inflater!!.inflate(R.layout.fragment_pass_debts,container, false)

        return v

    }
}