package com.ga.kps.debitum

import android.content.ComponentCallbacks
import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.preference.PreferenceManager
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.github.lzyzsd.circleprogress.ArcProgress
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.google.android.material.floatingactionbutton.FloatingActionButton
import helpcodes.EstatusDeuda.ACTIVA
import helpcodes.ANADIR_PAGO_DEUDA
import helpcodes.EstatusDeuda.PAGADA
import model.Deuda
import room.components.viewModels.CuentaViewModel
import room.components.viewModels.DeudaViewModel

class DebtDetailsFragment: Fragment() {

    lateinit var RV: RecyclerView
    lateinit var deudasViewModel: DeudaViewModel
    lateinit var deudaActualLive : LiveData<Deuda>
    lateinit var mAdView : AdView
    var simboloMoneda = "$"

    var tituloDeudaTextView: TextView? = null
    var fechaDeudaTextView: TextView? = null
    var deudaTotalTextView: TextView? = null
    var montoPagadoTextView: TextView? = null
    var montoRestanteTextView: TextView? = null
    var tipoDeudaTextView: TextView? = null
    var arcoProgresso: ArcProgress? = null
    var notaDeudaTextView: TextView? = null

    internal var callback: DebtStatusExposer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        simboloMoneda = prefs.getString("moneySign","$")

    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) : View?{
        val v = inflater.inflate(R.layout.fragment_debt_details, container, false)


        tituloDeudaTextView = v.findViewById(R.id.tituloDeudaTV)
        fechaDeudaTextView = v.findViewById(R.id.fechaDeudaTV)
        deudaTotalTextView = v.findViewById(R.id.montoDeudaTV)
        montoPagadoTextView = v.findViewById(R.id.montoPagadoTV)
        montoRestanteTextView = v.findViewById(R.id.montoRestanteTV)
        tipoDeudaTextView = v.findViewById(R.id.tipoDeudaTV)
        arcoProgresso = v.findViewById(R.id.arc_progress)
        notaDeudaTextView = v.findViewById(R.id.notaDeudaTV)

        MobileAds.initialize(context) {}
        mAdView = v.findViewById(R.id.adViewDebtDetails)
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)

        val fab = (activity as DebtDetailsActivity).findViewById<FloatingActionButton>(R.id.anadirPagoDeudadFAB)
        fab.setOnClickListener {
            val nav = Intent(context, AddDebtPaymentActivity::class.java)
            nav.putExtra("DEBT_ID", (activity as DebtDetailsActivity).debtID)
            startActivityForResult(nav, ANADIR_PAGO_DEUDA)

        }

        deudasViewModel = ViewModelProviders.of(this).get(DeudaViewModel::class.java)

        deudaActualLive = deudasViewModel.getDeuda((activity as DebtDetailsActivity).debtID)
        deudaActualLive.observe(this, Observer {
            populateDebtUI(it)

            callback?.getDebtStatus(it.estado)

            if(it.estado == PAGADA){
                fab.hide()
            }else{
                fab.show()
            }
        })


        return v
    }


    fun populateDebtUI(debt: Deuda?){
        tituloDeudaTextView?.text = debt?.titulo.toString()
        fechaDeudaTextView?.text = debt?.fecha_adquision
        deudaTotalTextView?.text = context?.getString(R.string.simboloMoneda,simboloMoneda,debt?.monto)
        montoPagadoTextView?.text = context?.getString(R.string.simboloMoneda,simboloMoneda,debt?.pagado)
        montoRestanteTextView?.text = context?.getString(R.string.simboloMoneda,simboloMoneda,(debt!!.monto - debt.pagado))
        arcoProgresso?.progress = ((debt!!.pagado * 100f) / debt.monto).toInt()
        notaDeudaTextView?.text = debt?.nota

        val tipoDeudaArray = context?.resources?.getStringArray(R.array.tipo_deuda)
        tipoDeudaTextView?.text =  tipoDeudaArray?.get(debt.tipo)


    }

    fun deleteDebt(){
        if(deudaActualLive.value?.estado == ACTIVA) {
            updateTotalDebt(deudaActualLive.value?.monto!! - deudaActualLive.value?.pagado!!)
        }
        removeObservers()
        val deudaAux = Deuda((activity as DebtDetailsActivity).debtID)
        deudasViewModel.delete(deudaAux)
        Toast.makeText(context,getString(R.string.deuda_eliminada), Toast.LENGTH_SHORT).show()
        (activity as DebtDetailsActivity).finish()
    }

    private fun removeObservers(){

        if(deudaActualLive.hasActiveObservers()){
            deudaActualLive.removeObservers(this)
        }
        //Log.d("TEXT", "EStas llamando a removeObservers " + deudaActualLive.value?.monto)
    }

    private fun updateTotalDebt(monto: Float){
        var cuentaViewModel: CuentaViewModel = ViewModelProviders.of(this).get(CuentaViewModel::class.java)
        cuentaViewModel.updateDeudaTotal(-monto)
    }

    fun changeDebtStatus(id: Int, status: Int){
        deudasViewModel.updateEstatusDeuda(id,status)

        if(status == PAGADA){
            deudasViewModel.updateMontoOriginalDeuda(id,deudaActualLive.value?.pagado!!)

        }
    }

    fun setDebtStatusExporserListener(callbacks: DebtStatusExposer){
        callback = callbacks
    }

    interface DebtStatusExposer{
        fun getDebtStatus(debtStatus: Int?)
    }




}