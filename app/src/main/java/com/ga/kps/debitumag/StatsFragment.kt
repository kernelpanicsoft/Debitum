package com.ga.kps.debitumag

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.utils.MPPointF

import model.CantidadTipoDeuda
import room.components.viewModels.DeudaViewModel

class StatsFragment : Fragment() {

    lateinit var debtsChart : PieChart
    lateinit var deudasViewModel:  DeudaViewModel
    lateinit var RV: RecyclerView


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_stats, container, false)



        RV = v.findViewById(R.id.RecViewEstadisticas)
        RV.setHasFixedSize(true)

        val mLayoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.VERTICAL,
            true
        )
        mLayoutManager.stackFromEnd = true
        RV.layoutManager = mLayoutManager

        val adapter = DebtByTypeAdapter(context)


        deudasViewModel = ViewModelProviders.of(this).get(DeudaViewModel::class.java)

        deudasViewModel.getCuentaDeTiposDeDeuda().observe(viewLifecycleOwner, Observer {
            Log.d("CuentaTipoDeuda", it.toString())
            setupPieChart(it)
            adapter.submitList(it)
        })

        adapter.setOnClickListener(View.OnClickListener {

        })

        RV.adapter = adapter
        debtsChart = v.findViewById(R.id.debtsPieChart)



        return v
    }

    private fun setupPieChart(debtsList: List<CantidadTipoDeuda>) {

        val pieEntries = ArrayList<PieEntry>()
        for(a in debtsList.indices){
            if((debtsList[a].deudaPorTipo!! - debtsList[a].pagadoPorTipo!!) > 0f) {
                pieEntries.add(
                    PieEntry(
                        (debtsList[a].deudaPorTipo!! - debtsList[a].pagadoPorTipo!!),
                        getNameOfType(debtsList[a].tipo)
                    )
                )
            }
        }

        val dataSet = PieDataSet(pieEntries,"")
        dataSet.setDrawIcons(false)
        dataSet.sliceSpace = 3f
        dataSet.iconsOffset = MPPointF(0f, 40f)
        dataSet.selectionShift = 5f
        dataSet.valueTextSize = 12f
        dataSet.valueTextColor = Color.WHITE
        dataSet.valueFormatter = PercentFormatter(debtsChart)


        val colors: ArrayList<Int> = ArrayList()
        val colorsArray = resources.getIntArray(R.array.default_rainbow)
        for(a in colorsArray)  colors.add(a)
        dataSet.colors = colors
        val data = PieData(dataSet)


        debtsChart.setEntryLabelTextSize(14f)
        debtsChart.description.isEnabled = false
        debtsChart.legend.isEnabled = true
        debtsChart.isDrawHoleEnabled = false
        debtsChart.setUsePercentValues(true)
        debtsChart.holeRadius = 0f
        debtsChart.data = data
        debtsChart.invalidate()


        debtsChart.legend.verticalAlignment = Legend.LegendVerticalAlignment.TOP
        debtsChart.legend.horizontalAlignment = Legend.LegendHorizontalAlignment.LEFT
        debtsChart.legend.orientation = Legend.LegendOrientation.VERTICAL
        debtsChart.legend.setDrawInside(false)
    }


    private fun getNameOfType(value: Int?) : String{
        val tipoDeudaArray = context?.resources?.getStringArray(R.array.tipo_deuda)
        return tipoDeudaArray!![value!!]
    }
}