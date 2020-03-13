package mobile.computing.group5.moneytracker.fragments.transaction

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
import mobile.computing.group5.moneytracker.fragments.transaction.Adapter.transactionAdapter
import mobile.computing.group5.moneytracker.fragments.transaction.Model.transactions
import mobile.computing.group5.moneytracker.R
//import mobile.computing.group5.moneytracker.createTransactionList
import mobile.computing.group5.moneytracker.fragments.home.HomeViewModel

class TransactionFragment : Fragment() {

    private lateinit var transactionViewModel: TransactionViewModel
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        transactionViewModel =
            ViewModelProviders.of(this).get(TransactionViewModel::class.java)

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
}

public fun createTransactionList() : List<transactions> {
    val transactionList = mutableListOf<transactions>()

    for( i in 1..100) {
        transactionList.add(
            transactions(
                "current_date",
                "transaction #$i",
                (i * 100).toString()
            )
        )
    }

    return transactionList
}