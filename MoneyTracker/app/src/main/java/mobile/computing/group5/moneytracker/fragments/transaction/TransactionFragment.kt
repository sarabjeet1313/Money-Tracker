package mobile.computing.group5.moneytracker.fragments.transaction

import android.content.Context
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_transaction.*
import mobile.computing.group5.moneytracker.MainActivity
import mobile.computing.group5.moneytracker.fragments.transaction.Adapter.transactionAdapter
import mobile.computing.group5.moneytracker.fragments.transaction.Model.transactions
import mobile.computing.group5.moneytracker.R
import mobile.computing.group5.moneytracker.Model.DatabaseHelper
import mobile.computing.group5.moneytracker.Model.money
//import mobile.computing.group5.moneytracker.createTransactionList
import mobile.computing.group5.moneytracker.fragments.home.HomeViewModel

class TransactionFragment : Fragment() {

    private lateinit var transactionViewModel: TransactionViewModel
    private lateinit var recyclerView: RecyclerView
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
        transactionViewModel =
            ViewModelProviders.of(this).get(TransactionViewModel::class.java)

        fillDatabase()

        var transactions = createTransactionList()


        val root = inflater.inflate(R.layout.fragment_transaction, container, false)

        recyclerView = root.findViewById<View>(R.id.transactionRecyclerView) as RecyclerView
        recyclerView.adapter =
            transactionAdapter(
                transactions
            )
        recyclerView.layoutManager = LinearLayoutManager(activity)

        return root
    }

    private fun createTransactionList(): List<transactions> {
        val transactionList = mutableListOf<transactions>()
        var moneyList = mutableListOf<money>()


        for (i in 0..99) {

            moneyList = dbHandler.readData()
            Log.d("dbg frag", moneyList[i].trans_id.toString())
            transactionList.add(
                transactions(
                    moneyList[i].date,
                    moneyList[i].desc,
                    moneyList[i].amount.toString(),
                    moneyList[i].trans_id
                )
            )
        }
        return transactionList
    }


    private fun fillDatabase() {
        dbHandler.clearDatabase()

        for (i in 0..60) {
            dbHandler.insertData(money(i, "description " + i.toString(), (i).toFloat(), "current_date", "Expense"))
        }
        for (i in 61..100) {
            dbHandler.insertData(money(i, "description " + i.toString(), (i).toFloat(), "current_date", "Income"))
        }
    }
}


