package mobile.computing.group5.moneytracker.fragments.transaction

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import mobile.computing.group5.moneytracker.R
import mobile.computing.group5.moneytracker.fragments.transaction.Adapter.transactionAdapter
import mobile.computing.group5.moneytracker.fragments.transaction.Model.transactions_list
import mobile.computing.group5.moneytracker.model.DatabaseHelper
import mobile.computing.group5.moneytracker.model.Transaction

class TransactionFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var dbHandler: DatabaseHelper
    private lateinit var noTransaction: TextView

    override fun onAttach(context: Context) {
        super.onAttach(context)
        dbHandler = DatabaseHelper( context, null)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val transactions = createTransactionList()
        val root = inflater.inflate(R.layout.fragment_transaction, container, false)

        recyclerView = root.findViewById<View>(R.id.transactionRecyclerView) as RecyclerView
        noTransaction = root.findViewById<View>(R.id.noTransactionText) as TextView
        noTransaction.visibility = View.GONE

        recyclerView.adapter =
            transactionAdapter(
                transactions
            )
        recyclerView.layoutManager = LinearLayoutManager(activity)

        if(transactions.isEmpty()) {
            noTransaction.visibility = View.VISIBLE
        }

        return root
    }

    private fun createTransactionList(): List<transactions_list> {
        val transactionList = mutableListOf<transactions_list>()
        var moneyList: MutableList<Transaction>

        val total = dbHandler.getTotalItems()
        for (i in 0 until total) {
            moneyList = dbHandler.readData()
            Log.d("dbg frag", moneyList[i].trans_id.toString())
            transactionList.add(
                transactions_list(
                    moneyList[i].date,
                    moneyList[i].desc,
                    moneyList[i].amount.toString(),
                    moneyList[i].trans_id,
                    moneyList[i].type
                )
            )
        }
        return transactionList
    }
}