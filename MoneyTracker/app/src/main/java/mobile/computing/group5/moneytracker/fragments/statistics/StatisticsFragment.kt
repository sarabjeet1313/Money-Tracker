package mobile.computing.group5.moneytracker.fragments.statistics

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import kotlinx.android.synthetic.main.fragment_statistics.*
import mobile.computing.group5.moneytracker.model.DatabaseHelper
import mobile.computing.group5.moneytracker.R
//import mobile.computing.group5.moneytracker.fragments.transaction.TransactionViewModel
import com.github.mikephil.charting.utils.ColorTemplate
//import sun.jvm.hotspot.utilities.IntArray
import android.graphics.DashPathEffect
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.LegendEntry


class StatisticsFragment : Fragment() {
    private lateinit var statisticsViewModel: StatisticsViewModel

    var type = listOf<String>("Expense" , "Income")

    private lateinit var dbHandler: DatabaseHelper


    override fun onAttach(context: Context) {
        super.onAttach(context)

        dbHandler = DatabaseHelper( context, null)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        statisticsViewModel =
            ViewModelProviders.of(this).get(StatisticsViewModel::class.java)

        val root = inflater.inflate(R.layout.fragment_statistics, container, false)
        val pie:PieChart = root.findViewById<PieChart>(R.id.pieChart)

        statisticsViewModel.text.observe(this, Observer {
            Log.i("message", it)
        })


//        pie.setDescription("Monthly Statistics from Monet Tracker")
        if(pie != null) {
            pie.contentDescription = "Monthly Statistics from Monet Tracker"
            pie.setRotationEnabled(true)
            pie.setHoleRadius(15f)
            pie.setTransparentCircleAlpha(0)
            pie.setCenterText("Statistics")
            pie.setCenterTextColor(Color.BLACK)
            pie.setCenterTextSize(10f)
            pie.setDrawEntryLabels(true)
        }

        addDataToPie(pie)

        var l:Legend = pie.getLegend()

        val expenseEntry:LegendEntry = LegendEntry("Expense", Legend.LegendForm.CIRCLE,20f,20f,null,Color.RED)
        val incomeEntry:LegendEntry = LegendEntry("Income", Legend.LegendForm.CIRCLE,20f,20f,null,Color.GREEN)
        l.setCustom(listOf(expenseEntry,incomeEntry))

        return root
    }

    private fun addDataToPie(pie: PieChart) {

        var amountEntry : MutableList<PieEntry> = ArrayList<PieEntry>()
        var typeEntry : MutableList<String> = ArrayList<String>()

        val expenseAmount :Float = dbHandler.getTotalExpense()
        val incomeAmount :Float = dbHandler.getTotalIncome()

        Log.d("dbg", expenseAmount.toString())
        Log.d("dbg", incomeAmount.toString())

        var amount = listOf<Float>(dbHandler.getTotalExpense(), dbHandler.getTotalIncome())

        for( i in amount) {
            amountEntry.add(PieEntry(i))
        }

        for( j in type) {
            typeEntry.add(j)
        }

        var dataSet : PieDataSet = PieDataSet(amountEntry, "Monthly Stats")
        dataSet.setSliceSpace(5f)
        dataSet.setValueTextSize(20f)

        var colors:ArrayList<Int> = ArrayList<Int>()

        colors.add(Color.RED)
        colors.add(Color.GREEN)

        dataSet.setColors(colors)


        var pieData : PieData = PieData(dataSet)
        pie.setData(pieData)
        pie.invalidate()

    }

}