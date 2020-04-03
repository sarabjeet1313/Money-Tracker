package mobile.computing.group5.moneytracker.fragments.statistics

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.LegendEntry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import mobile.computing.group5.moneytracker.R
import mobile.computing.group5.moneytracker.model.DatabaseHelper

/**
 * main class for statistics fragment to inflate the fragment_statistics.xml
 */
class StatisticsFragment : Fragment() {

    // local private variables.
    private var type = listOf("Expense" , "Income")
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

        val mInflater = activity!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view: View = mInflater.inflate(R.layout.fragment_statistics, container, false)
        setHasOptionsMenu(true)

        val pie:PieChart = view.findViewById(R.id.pieChart)  // getting view object

        // adding features
        pie.isRotationEnabled = true
        pie.holeRadius = 0f
        pie.setTransparentCircleAlpha(1)
        pie.setDrawEntryLabels(true)

        addDataToPie(pie)

        // adding legend information
        val l:Legend = pie.legend

        val expenseEntry = LegendEntry("Expense", Legend.LegendForm.CIRCLE,20f,20f,null,Color.parseColor("#F44336"))
        val incomeEntry = LegendEntry("Income", Legend.LegendForm.CIRCLE,20f,20f,null,Color.parseColor("#46A049"))
        l.setCustom(listOf(expenseEntry,incomeEntry))

        return view
    }

    // inflate the view with data
    private fun addDataToPie(pie: PieChart) {

        val amountEntry : MutableList<PieEntry> = ArrayList()
        val typeEntry : MutableList<String> = ArrayList()

        // accessing database
        val amount = listOf(dbHandler.getTotalExpense(), dbHandler.getTotalIncome())

        for( i in amount) {
            amountEntry.add(PieEntry(i))
        }

        for( j in type) {
            typeEntry.add(j)
        }

        val dataSet = PieDataSet(amountEntry, "Statistics")
        dataSet.sliceSpace = 0f
        dataSet.valueTextSize = 20f

        val colors:ArrayList<Int> = ArrayList()
        colors.add(Color.parseColor("#F44336"))
        colors.add(Color.parseColor("#46A049"))
        dataSet.colors = colors

        val pieData = PieData(dataSet)
        pie.data = pieData
        pie.invalidate()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == 16908332){     // back button functionality
            findNavController().navigate(R.id.action_navigation_statistics_to_navigation_more)
            return true
        }
        return super.onOptionsItemSelected(item)
    }

}