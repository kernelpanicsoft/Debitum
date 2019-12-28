package com.ga.kps.debitum

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import com.github.mikephil.charting.utils.MPPointF
import room.components.viewModels.DeudaViewModel

class StatsFragment : Fragment() {

    lateinit var debtsChart : PieChart
    lateinit var deudasViewModel:  DeudaViewModel


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_stats, container, false)

        deudasViewModel = ViewModelProviders.of(this).get(DeudaViewModel::class.java)

        deudasViewModel.getCuentaDeTiposDeDeuda().observe(this, Observer {
            Log.d("CuentaTipoDeuda", it.toString())

        })
        val tituloTV = v.findViewById<TextView>(R.id.tituloSeccionTV)


        debtsChart = v.findViewById(R.id.debtsPieChart)

        setupPieChart()

        return v
    }

    private fun setupPieChart() {
        // Populating a list of PieEntries
        val rainFall : FloatArray = floatArrayOf(98.5f,128.8f,161.6f,132.5f)
        val monthNames : Array<String> = arrayOf("Jan", "Fab", "Mar","May")

        val pieEntries = ArrayList<PieEntry>()
        for(a in 1..4){
            pieEntries.add(PieEntry(rainFall[a],monthNames[a]))
        }

        val dataSet = PieDataSet(pieEntries,"Hello world")
        dataSet.setDrawIcons(false)
        dataSet.sliceSpace = 3f
        dataSet.iconsOffset = MPPointF(0f, 40f)
        dataSet.selectionShift = 5f
        val data = PieData(dataSet)

        debtsChart.holeRadius = 58f
        debtsChart.data = data
        debtsChart.invalidate()

    }

    fun setData(count: Int, range: Float){
        val entries = ArrayList<PieEntry>()

        for(i: Int in 1..count){
            entries.add(PieEntry(((Math.random() * range) + range / 5).toFloat()))

        }

        val dataSet = PieDataSet(entries,"Estradas de prueba")

        dataSet.setDrawIcons(false)
        dataSet.sliceSpace = 3f
        dataSet.iconsOffset = MPPointF(0f,40f)
        dataSet.selectionShift = 5f

        val colors: ArrayList<Int> = ArrayList()

        for (c in ColorTemplate.VORDIPLOM_COLORS) colors.add(c)

        for (c in ColorTemplate.JOYFUL_COLORS) colors.add(c)

        for (c in ColorTemplate.COLORFUL_COLORS) colors.add(c)

        for (c in ColorTemplate.LIBERTY_COLORS) colors.add(c)

        for (c in ColorTemplate.PASTEL_COLORS) colors.add(c)

        colors.add(ColorTemplate.getHoloBlue())

        dataSet.colors = colors

        val data = PieData(dataSet)
        data.setValueFormatter(PercentFormatter(debtsChart))
        data.setValueTextSize(11f)
        data.setValueTextColor(Color.WHITE)
        data.setValueTypeface(Typeface.DEFAULT)
        debtsChart.data = data

        debtsChart.highlightValues(null)
        debtsChart.invalidate()

    }
}