package com.ga.kps.debitum

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import com.github.mikephil.charting.utils.MPPointF


class StatsFragment : Fragment() {

    lateinit var debtsChart : PieChart


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_stats, container, false)

        debtsChart = v.findViewById(R.id.debtsPieChart)
        debtsChart.setUsePercentValues(true)
        debtsChart.description.isEnabled = false
        debtsChart.setExtraOffsets(5f,10f,5f,5f)

        debtsChart.dragDecelerationFrictionCoef = 0.95f

        debtsChart.setEntryLabelColor(Color.WHITE)
        debtsChart.setEntryLabelTypeface(Typeface.DEFAULT)
        debtsChart.setEntryLabelTextSize(12f)

        setData(5,10f)
        return v;
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