package mobile.computing.group5.moneytracker.fragments.transaction

import android.content.Context
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_transaction.*
import mobile.computing.group5.moneytracker.MainActivity
import mobile.computing.group5.moneytracker.fragments.transaction.Adapter.transactionAdapter
import mobile.computing.group5.moneytracker.R
import mobile.computing.group5.moneytracker.model.DatabaseHelper
import mobile.computing.group5.moneytracker.model.Transaction
//import mobile.computing.group5.moneytracker.createTransactionList
import mobile.computing.group5.moneytracker.fragments.home.HomeViewModel
import mobile.computing.group5.moneytracker.fragments.transaction.Model.transactions_list
import org.w3c.dom.Text

class TransactionFragment : Fragment() {

//    private lateinit var transactionViewModel: TransactionViewModel
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
//        transactionViewModel =
//            ViewModelProviders.of(this).get(TransactionViewModel::class.java)

        var transactions = createTransactionList()


        val root = inflater.inflate(R.layout.fragment_transaction, container, false)

        recyclerView = root.findViewById<View>(R.id.transactionRecyclerView) as RecyclerView
        noTransaction = root.findViewById<View>(R.id.noTransactionText) as TextView
        noTransaction.setVisibility(View.GONE)

        recyclerView.adapter =
            transactionAdapter(
                transactions
            )
        recyclerView.layoutManager = LinearLayoutManager(activity)

        if(transactions.isEmpty()) {
            noTransaction.setVisibility(View.VISIBLE)
        }

        return root
    }

    private fun createTransactionList(): List<transactions_list> {
        val transactionList = mutableListOf<transactions_list>()
        var moneyList = mutableListOf<Transaction>()

        val total = dbHandler.getTotalItems()
        for (i in  0..total-1) {

            moneyList = dbHandler.readData()
            Log.d("dbg frag", moneyList[i].trans_id.toString())
            transactionList.add(
                transactions_list(
                    moneyList[i].desc,
                    moneyList[i].amount.toString(),
                    moneyList[i].date,
                    moneyList[i].trans_id
                )
            )
        }
        return transactionList
    }

}


